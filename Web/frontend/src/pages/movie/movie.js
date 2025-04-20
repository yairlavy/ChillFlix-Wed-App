import React, { useState, useEffect } from "react";
import { useParams, useNavigate} from "react-router-dom";
import axios from '../../utils/api'; 
import Navbar from "../../components/navbar/Navbar";
import RecMovie from "../../components/recMovie/recMovie";
import "./movie.css";

function Movie() {
  const { movieId } = useParams(); // Get the movieId from the URL params
  const navigate = useNavigate(); // Initialize useNavigate for routing
  const [movie, setMovie] = useState(null);
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isInWatchlist, setIsInWatchlist] = useState(false); // Track if movie is in watchlist
  const token = localStorage.getItem("token");

  useEffect(() => {
    if (!token) {
      window.location.href = "/";
    }
  }, [token]);

    // Fetch movie details
    useEffect(() => {
    const fetchMovieDetails = async () => {
      try {
        const response = await axios.get(`/movies/${movieId}`);
        setMovie(response.data); // Set the movie details
        console.log("movie data:", response.data);
      } catch (err) {
        setError("Failed to fetch movie details");
      } finally {
        setLoading(false);
      }
    };

    fetchMovieDetails();
  }, [movieId, token]);


  // Fetch movie recommendations
  useEffect(() => {
    const fetchRecommendations = async () => {
      try {
        const response = await axios.get(`/movies/${movieId}/recommend/`);

        const recommendedMoviesIds = response.data.MovieRecommendations || [];
        if (recommendedMoviesIds.length === 0) {
          setRecommendations([]); // Set an empty array if no recommendations
          return;
        }

        const recommendedMovies = await Promise.all(
          recommendedMoviesIds.map(async (id) => {
            const movieResponse = await axios.get(`/movies/${id}`);
            return movieResponse.data;
          })
        );

        setRecommendations(recommendedMovies);
      } catch (err) {
        if (err.response && err.response.status === 404) {
          setRecommendations([]); // No recommendations found
          console.log("No recommendations found for this movie.");
        } else {
          console.error("Error fetching recommendations:", err.message);
        }
      }
    };

    if (movieId) {
      fetchRecommendations();
    }
  }, [movieId, token]);

  // Check if movie is in user's watchlist
  useEffect(() => {
      const checkIfInWatchlist = async () => {
      try {
        const decodedToken = JSON.parse(atob(token.split('.')[1]));
        const userId = decodedToken.id;
    
        // Fetch user data by ID
        const response = await axios.get(`/users/${userId}`);

        const movieInWatchlist = Object.values(response.data.watchlist)
        console.log(" this the watchlist:", movieInWatchlist);

        if (movieInWatchlist.includes(movieId)) {
          setIsInWatchlist(true);
        } else {
          setIsInWatchlist(false);
        }

      } catch (err) {
        console.error("Error fetching watchlist:", err);
      }
    };

    checkIfInWatchlist();
  }, [movieId, token]);

  // Add or remove movie from watchlist
  const handleWatchlistToggle = async () => {
    try {
      if (isInWatchlist) {
        // Remove from watchlist
        await axios.delete(`/movies/${movieId}/recommend`);
        setIsInWatchlist(false);
        alert("Movie removed from watchlist");
      } else {
        // Add to watchlist
        await axios.post(`/movies/${movieId}/recommend`);
        setIsInWatchlist(true);
        alert("Movie added to watchlist!");
      }
    } catch (err) {
      alert("Failed to update watchlist");
    }
  };

  // Render loading or error states
  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;
  if (!movie) return <div>No movie details available</div>;

  return (
    <div className="movie-page">
      <Navbar />
      <div className="movie-container">
        <div className="movie-details">
          <img
            src={movie?.poster_path.startsWith('/420x631/') ? `https://dummyjson.com/image${movie?.poster_path}` 
                                          :`${axios.defaults.baseURL}/Assets/movieAssets/${movie?.poster_path}`}
            alt={movie?.title}
            className="movie-poster"
          />
          <div className="movie-info">
            <h1>{movie?.title}</h1>
            <p>Genres: {movie?.genres?.join(", ") || "N/A"}</p>
            <p>Runtime: {movie?.runtime || "N/A"} minutes</p>
            <p>{movie?.overview || "No overview available."}</p>
            <div className="movie-actions">
              <button className="add-to-watchlist" onClick={handleWatchlistToggle}>
                {isInWatchlist ? "- Remove from Watchlist" : "+ Add to Watchlist"}
              </button>
              <button
                className="play-button"
                onClick={() =>
                  navigate(`/movies/${movieId}/play`, {
                    state: {
                      trailer: movie?.trailer || '',
                      title: movie?.title,
                      subtitlesUrl: movie?.subtitles_url || '',
                      movieId: movieId
                    },
                  })
                }
              >
                Play
              </button>
            </div>
            <div className="recommendations">
              <h2>Recommended Movies:</h2>
              <div className="recommendations-container">
                {recommendations && recommendations.length > 0 ? (
                  recommendations.map((recMovie, key) => (
                    <RecMovie {...recMovie} key={key}></RecMovie>
                  ))
                ) : (
                  <p id="emptyRec">No Movies to recommend</p>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Movie;
