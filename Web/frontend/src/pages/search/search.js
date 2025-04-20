import React, { useState, useEffect, useCallback } from "react";
import MovieCard from "../../components/movieCard/movieCard";
import axios from "../../utils/api"; 
import "./search.css";
import Navbar from "../../components/navbar/Navbar";
import { useLocation } from "react-router-dom";

function Search() {
  const [movies, setMovies] = useState([]);
  const [error, setError] = useState("");
  const token = localStorage.getItem("token");
  const location = useLocation();

  // Memoize the function so its reference is stable
  const getQueryFromURL = useCallback(() => {
    return new URLSearchParams(location.search).get("query");
  }, [location.search]);

  useEffect(() => {
    if (!token) {
      window.location.href = "/"; // Redirect if no token is found
      return;
    }

    const queryParam = getQueryFromURL();
    if (queryParam) {
      const fetchMovies = async () => {
        try {
          const response = await axios.get(`/movies/search/${queryParam}`);
          const movieDetails = await Promise.all(
            response.data.movieList.map(async (movie) => {
              const movieResponse = await axios.get(`/movies/${movie._id}`);
              return movieResponse.data;
            })
          );
          setMovies(movieDetails);
          setError("");
        } catch (err) {
          setError("Failed to fetch movies. Please try again.");
          console.error(err);
        }
      };

      fetchMovies();
    } else {
      setMovies([]); // Clear previous results if query is empty
    }
  }, [location.search, token, getQueryFromURL]); // Added getQueryFromURL as a dependency

  return (
    <div className="search-page">
      <Navbar />
      <div className="search-container">
        <h2>Search Results for "{getQueryFromURL()}"</h2>
        {error && <p className="error-message">{error}</p>}
      </div>
      <div className="movies-container">
        {movies && movies.length > 0 ? (
          movies.map((movie, key) => (
            <MovieCard {...movie} key={key} />
          ))
        ) : (
          <p className="no-search-results">
            No results found for "{getQueryFromURL()}".
          </p>
        )}
      </div>
    </div>
  );
}

export default Search;
