const movieService = require("../services/movie");
const User = require("../models/user");
const categoryService = require("../services/category");
const userService = require("../services/user");
const Movie = require("../models/movie");
const net = require("net");

const createMovie = async (req, res) => {
  if (req.user.isAdmin) {
    try {
      const movie = await movieService.createMovie(req.body);
      res.status(201).json(movie);
    } catch (err) {
      res.status(400).json({ error: "Bad request" });
    }
  } else {
    res.status(403).json("Not authorized to create movies");
  }
};

const getAllMovies = async (req, res) => {
  try {
    const movies = await movieService.getAllMovies();
    res.status(200).json(movies);
  } catch (err) {
    console.error(err);
    res.status(404).json({ error: "movies not found" });
  }
};

// Get all movies that in on of thier string field have the query
const GetMovieBasedOnQuery = async (req, res) => {
  try {
    const query = req.params.query;
    const movies = await movieService.findMovieBasedOnKey(query);
    res.status(200).json(movies);
  } catch (err) {
    console.error(err);
    res.status(400).json({ error: "Bad request" });
  }
};

// get all promoted categories and 20 random unwatched movies in the same genre
// and 20 random watched movies from the user's watchlist
const getAllPromotedCategories = async (req, res) => {
  try {
    // Fetch all promoted categories
    const categories = await categoryService.getAllPromotedCategories();

    if (!categories || categories.length === 0) {
      return res.status(404).json({ error: "No promoted categories found" });
    }

    const categoriesWithMovies = [];
    const userWatchlist = req.user.watchlist;

    // Convert the Map values (ObjectIds) into an array
    const longIdsInWatchlist = Array.from(userWatchlist.values()); // Get all ObjectIds from the Map

    // Iterate over each category and fetch 20 random unwatched movies in the same genre
    for (let category of categories) {
      const Movies = await movieService.findMovieBasedOnKey({genres: category.name});

      // Filter out the movies that are already in the user's watchlist (watched movies)
      let unwatchedMovies = Movies.movieList.filter(
        (movie) => !longIdsInWatchlist.includes(movie._id)
      );

      // Randomize and select 20 unwatched movies
      unwatchedMovies = unwatchedMovies
        .sort(() => 0.5 - Math.random())
        .slice(0, 20);

      categoriesWithMovies.push({
        category,
        movieCount: unwatchedMovies.length,
        movieList: unwatchedMovies,
      });
    }

    // Fetch 20 random watched movies from the user's watchlist
    const watchedMovies = Array.from(userWatchlist.values())
      .sort(() => 0.5 - Math.random())
      .slice(0, 20);

    // Return the categories with their unwatched movies, and the random watched movies from the user's watchlist
    res.status(200).json({
      categoriesWithMovies,
      watchedMoviesCount: watchedMovies.length,
      watchedMovies: watchedMovies,
    });
  } catch (err) {
    console.error(err);
    res.status(400).json({ error: "Bad Request" });
  }
};

const getMovieById = async (req, res) => {
    try {
        const movie = await movieService.getMovieById(req.params.id);
        if (!movie) {
        return res.status(404).json({ error: "Movie not found" });
        }
        res.status(200).json(movie);
    } catch (err) {
        console.error(err);
        res.status(404).json({ error: "Movie not found" });
    }
};

const updateMovie = async (req, res) => {
    if (req.user.isAdmin) {
        try {
        const movie = await movieService.updateMovie(req.params.id, req.body);
        if (!movie) {
            return res.status(404).json({ error: "Movie not found" });
        }
        res.status(204).json(movie);
        } catch (err) {
        console.error(err);
        res.status(400).json({ error: "Bad request" });
        }
    } else {
        res.status(403).json("Not authorized to update movies");
    }
};

const deleteMovie = async (req, res) => {
    if (!req.user.isAdmin) {
        return res.status(403).json("Not authorized to delete movies");
    }

    try {
        const movieId = req.params.id;

        // Retrieve the movie to get its short_id
        const movie = await movieService.getMovieById(movieId);
        if (!movie) {
            return res.status(404).json({ error: "Movie not found" });
        }

        const movieShortId = movie.short_id;
       
        const cppServerHost = process.env.RECOMMEND_HOST; // Use environment variable for host
        const cppServerPort = process.env.RECOMMEND_PORT; // Use environment variable for port


        // Step 1: Get all users that watched this movie
        const users = await User.find({ [`watchlist.${movieShortId}`]: { $exists: true } });

        //console.log("Users watching the movie:", users);

        // Step 2: For each user, send DELETE command to C++ server and remove movie from watchlist
        const deletePromises = users.map((user) => {
            return new Promise((resolve, reject) => {
                const userId = user._id; 
                const userShortId = user.short_id; 
                const command = `DELETE ${userShortId} ${movieShortId}\n`; // Command to send to C++ server

                const client = new net.Socket();
                let buffer = ""; // Buffer to collect data from C++ server
                let responseHandled = false; // Flag to ensure response is handled once

                client.connect(cppServerPort, cppServerHost, () => {
                    client.write(command); // Send DELETE command to C++ server
                });

                client.on("data", async (data) => {
                    buffer += data.toString();

                    if (!responseHandled) {
                        responseHandled = true;
                        const response = buffer.trim();

                        if (response.startsWith("204")) {
                            // Movie deleted for this user in C++ server
                            // Now remove the movie from user's watchlist in MongoDB
                            try {
                                await userService.removeMovieFromUser(userId, movieShortId);
                                resolve(); // Resolve the promise
                            } catch (err) {
                                console.error(`Error removing movie from user ${userId} watchlist:`, err.message);
                                reject(err); // Reject the promise
                            }
                        } else {
                            console.error(`Error from C++ server for user ${userId}:`, response);
                            reject(new Error(`C++ server error for user ${userId}: ${response}`));
                        }

                        client.destroy(); // Close connection to C++ server
                    }
                });

                client.on("error", (err) => {
                    console.error(`Error communicating with C++ server for user ${userId}:`, err.message);
                    reject(err);
                });

                client.on("close", () => {
                    console.log(`Connection to C++ server closed for user ${userId}`);
                });
            });
        });

        // Wait for all users to be processed
        await Promise.all(deletePromises);

        // Step 3: Delete movie from movies collection
        const deletedMovie = await movieService.deleteMovie(movieId);
        if (!deletedMovie) {
            return res.status(404).json({ error: "Movie not found in database" });
        }

        // Respond with success
        res.status(200).json({ message: "Movie deleted successfully from both systems" });
    } catch (err) {
        console.error("Error deleting movie:", err.message);
        res.status(500).json({ error: "Failed to delete movie" });
    }
};

// Get movie recommendtions from the RecommendSystem
const getRecommendedMovies = async (req, res) => {
    try {
        const movieId = req.params.id;
        
        const movie = await movieService.getMovieById(movieId); 
        const user = req.user; 

        if (!movie || !user) {
            return res.status(404).json({ error: "Movie or User not found" });
        }

        const shortMovieId = movie.short_id;
        const userShortId = user.short_id;

        const command = `GET ${userShortId} ${shortMovieId}`;

        const cppServerHost = process.env.RECOMMEND_HOST; // Use environment variable for host
        const cppServerPort = process.env.RECOMMEND_PORT; // Use environment variable for port


        const client = new net.Socket();
        let buffer = ""; // Buffer to store incoming data
        let responseSent = false; // Flag to prevent multiple responses

        client.connect(cppServerPort, cppServerHost, () => {
            client.write(command); // Send command to C++ server
        });

        client.on("data", async (data) => {
            buffer += data.toString(); // Append incoming data to buffer

            const response = buffer.trim(); // Trim unnecessary whitespace
            console.log("Received from C++ server:", response);

            if (!responseSent) { // Ensure response is sent only once
                if (response.startsWith("400")) {
                    res.status(400).json({ error: "Bad Request" });
                } else if (response.startsWith("404")) {
                    res.status(404).json({ error: "Not Found" });
                } else if (response.startsWith("200")) {
                    //if there is movies to recommend
                    if (response.includes("\n")) {
                        // Extract everything after the first \n\n
                        const moviesData = response.split("\n\n")[1]; 
                        
                        // Split by spaces and trim each movie ID
                        const recommendedShortIds = moviesData.split(" ").map((id) => id.trim());

                        console.log("Recommended short IDs:", recommendedShortIds);

                        // Fetch `_id`s for the recommended movies from the database
                        const recommendedMovies = await Movie.find({
                            short_id: { $in: recommendedShortIds }
                        }).select('_id'); // Only retrieve `_id`

                        // Return the list of `_id`s
                        const recommendedMovieIds = recommendedMovies.map((movie) => movie._id);
                        res.status(200).json({ MovieRecommendations: recommendedMovieIds });

                    } else {
                        res.status(200).json({ MovieRecommendations: null }); // No movies to recommend
                    }
                } else {
                    res.status(500).json({ error: "Unknown Error", data: buffer.trim() });
                }
                responseSent = true; // Mark response as sent
            }
            client.destroy(); // Close client connection
        });

        client.on("error", (err) => {
            console.error("Error:", err.message);
            if (!responseSent) { // Ensure error response is sent only once
                res.status(500).json({ error: "Failed to communicate with C++ server" });
                responseSent = true;
            }
        });

        client.on("close", () => {
            console.log("Connection closed"); // Log connection closure
        });
    } catch (err) {
        console.error(err.message); // Log unexpected error
        res.status(500).json({ error: "Unexpected error occurred" });
    }
};

// Add movie to user watchlist in the RecommendSystem and in the MongoServer
const addRecommendedMovies = async (req, res) => {
    try {
        const movieId = req.params.id;
        
        const movie = await movieService.getMovieById(movieId); 
        const user = req.user;

        if (!movie || !user) {
            return res.status(404).json({ error: "Movie or User not found" });
        }

        const shortMovieId = movie.short_id;
        const userShortId = user.short_id;

        // Check if the user's watchlist is empty if so use POST else use PATCH
        let command;
        if (user.watchlist && user.watchlist.size > 0) {
            command = `PATCH ${userShortId} ${shortMovieId}`;
        } else {
            command = `POST ${userShortId} ${shortMovieId}`;
        }

        console.log("Sending command to C++ server:", command);

        const cppServerHost = process.env.RECOMMEND_HOST;
        const cppServerPort = process.env.RECOMMEND_PORT;

        const addMovie = await userService.addMovieToUser(user.id, shortMovieId, movieId);

        if (!addMovie) {
            return res.status(400).json({ error: "Failed to add movie to user's watchlist" });
        }

        const client = new net.Socket();
        let buffer = "";
        let responseSent = false;

        client.connect(cppServerPort, cppServerHost, () => {
            client.write(command);
            console.log("Command sent to C++ server:", command);
        });
    
        client.on("data", (data) => {
            buffer += data.toString();  // Append the received data to the buffer
        
            // Trim the buffer to remove any unwanted newlines or spaces
            const response = buffer.trim();
        
            console.log("Received from C++ server:", response);  // Log the response
        
            if (!responseSent) {
                // Process the response based on its status
                if (response.startsWith("400")) {
                    res.status(400).json({ message: "Bad Request" });
                } else if (response.startsWith("404")) {
                    res.status(404).json({ message: "Not Found" });
                } else if (response.startsWith("201")) {
                    res.status(201).json({ message: "Created", data: response });
                } else if (response.startsWith("204")) {
                    res.status(204).json({ message: "No Content", data: response });
                } else {
                    res.status(500).json({ message: "Unknown Error", data: response });
                }
                responseSent = true;
            }
        
            client.destroy();  // Close the connection
        });


        client.on("error", (err) => {
            console.error("Error:", err.message);
            if (!responseSent) {
                res.status(500).json({ error: "Failed to communicate with C++ server" });
                responseSent = true;
            }
        });

        client.on("close", () => {
            console.log("Connection closed");
        });

    } catch (err) {
        console.error(err.message);
        res.status(400).json({ message: "Bad Request" });
    }
};


const removeRecommendedMovies = async (req, res) => {
    try {
      const movieId = req.params.id;
  
      const movie = await movieService.getMovieById(movieId);
      const user = req.user;
  
      if (!movie || !user) {
        return res.status(404).json({ error: "Movie or User not found" });
      }
  
      const shortMovieId = movie.short_id;
      const userShortId = user.short_id;
  
      const command = `DELETE ${userShortId} ${shortMovieId}`;
  
      console.log("Sending command to C++ server:", command);
  
      const cppServerHost = process.env.RECOMMEND_HOST;
      const cppServerPort = process.env.RECOMMEND_PORT;
  
      // Remove the movie from the user's watchlist in the database
      const removeMovie = await userService.removeMovieFromUser(user.id, shortMovieId);
  
      if (!removeMovie) {
        return res.status(400).json({ error: "Failed to remove movie from user's watchlist" });
      }
  
      const client = new net.Socket();
      let buffer = "";
      let responseSent = false;
  
      client.connect(cppServerPort, cppServerHost, () => {
        client.write(command);
        console.log("Command sent to C++ server:", command);
      });
  
      client.on("data", (data) => {
        buffer += data.toString();
  
        const response = buffer.trim();
  
        console.log("Received from C++ server:", response);
  
        if (!responseSent) {
          if (response.startsWith("204")) {
            res.status(204).json({ message: "Movie removed successfully" });
          } else if (response.startsWith("404")) {
            res.status(404).json({ error: "Movie not found in C++ server" });
          } else {
            res.status(500).json({ error: "Unknown error from C++ server", data: response });
          }
          responseSent = true;
        }
  
        client.destroy();
      });
  
      client.on("error", (err) => {
        console.error("Error:", err.message);
        if (!responseSent) {
          res.status(500).json({ error: "Failed to communicate with C++ server" });
          responseSent = true;
        }
      });
  
      client.on("close", () => {
        console.log("Connection closed");
      });
  
    } catch (err) {
      console.error(err.message);
      res.status(400).json({ error: "Bad Request" });
    }
  };
  
  module.exports = {
    createMovie,
    getAllMovies,
    getMovieById,
    updateMovie,
    deleteMovie,
    getAllPromotedCategories,
    GetMovieBasedOnQuery,
    getRecommendedMovies,
    addRecommendedMovies,
    removeRecommendedMovies,
  };
