import React, { useState, useEffect, useRef } from "react";
import { Navigate } from "react-router-dom";
import axios from '../../utils/api'; 
import "./browse.css"; 
import Navbar from "../../components/navbar/Navbar";
import BrowseMovieCard from "../../components/browseMovieCard/browseMovieCard";
import RandomMovie from "../../components/randomMovie/randomMovie";

function Browse() {
  const [categories, setCategories] = useState([]); // Store categories and movie data
  const [randomMovie, setRandomMovie] = useState(null); // Store a random movie
  const movieRef = useRef([]); // To store references for the movie grids (for scroll control)
  const token = localStorage.getItem("token"); 

  // Fetch categories and movie details
  useEffect(() => {
    // If there's no token, redirect to the homepage
    if (!token) {
      return <Navigate to="/" />;
    }
    
    const fetchCategories = async () => {
      try {
        console.log("Sending request to fetch categories...");
        
        const response = await axios.get("/movies/");

        console.log("Response received:", response.data);

        const categoriesData = response.data.categoriesWithMovies;
        const watchedMoviesData = response.data.watchedMovies;

        console.log("Categories data:", categoriesData);
        console.log("Watched movies data:", watchedMoviesData);

        // Fetch all movie details for categories
        const categoriesWithMovies = await Promise.all(
          categoriesData.map(async (category) => {
            const movieDetails = await Promise.all(
              category.movieList.map(async (movie) => {
                const movieResponse = await axios.get(`/movies/${movie._id}`);
                return movieResponse.data;
              })
            );
            return { ...category, movieDetails };
          })
        );

        console.log("Categories with movies:", categoriesWithMovies);

        // Fetch watched movies
        const watchedMoviesDetails = await Promise.all(
          watchedMoviesData.map(async (movieId) => {
            const movieResponse = await axios.get(`/movies/${movieId}`);
            return movieResponse.data;
          })
        );

        console.log("Watched movies details:", watchedMoviesDetails);

        // Add Watchlist category
        const watchlistCategory = {
          category: { name: "Watchlist", _id: "watchlist" },
          movieDetails: watchedMoviesDetails,
        };

        // Prepend the Watchlist category and append the rest
        const finalCategories = [
          watchlistCategory,
          ...categoriesWithMovies.filter((category) => category.movieDetails.length > 0),
        ];

        console.log("Final categories:", finalCategories);

        setCategories(finalCategories);

        // Randomly select a movie
        const allMovies = finalCategories.flatMap((category) => category.movieDetails);
        const randomIndex = Math.floor(Math.random() * allMovies.length);
        setRandomMovie(allMovies[randomIndex]);

      } catch (error) {
        console.error("Error fetching categories or movies:", error);
        console.log("Error details:", error.response);
      }
    };

    fetchCategories();
  }, [token]);

  // scroll arrows of the movie grid
  const scrollLeft = (index) => {
    const container = movieRef.current[index];
    container.scrollBy({
      left: -800,
      behavior: "smooth",
    });
  };

  const scrollRight = (index) => {
    const container = movieRef.current[index];
    container.scrollBy({
      left: 800,
      behavior: "smooth",
    });
  };


  return (
    <div className="browse">
      <Navbar />
      {/* Display the random movie */}
      {randomMovie && (
        <div className="random">
          <RandomMovie {...randomMovie} />
        </div>
      )}

      {/* Movie categories */}
      <div className = "categoriesContiner">
      {categories.map((category, categoryIndex) => (
        <div key={category.category._id} className="category-section">
          <h2 className="category-title">{category.category.name}</h2>
          <div className="movie-slider">
            <button className="scroll-arrow left" onClick={() => scrollLeft(categoryIndex)}>
              &#8592;
            </button>

            <div ref={(el) => (movieRef.current[categoryIndex] = el)} className="movie-grid">
              {category.movieDetails.map((movie, key) => (
                  <BrowseMovieCard {...movie} key={key} />
              ))}
            </div>

            <button className="scroll-arrow right" onClick={() => scrollRight(categoryIndex)}>
              &#8594;
            </button>
          </div>
        </div>
      ))}
      </div>
    </div>
  );
}

export default Browse;
