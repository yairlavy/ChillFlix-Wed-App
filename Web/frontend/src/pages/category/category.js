import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from '../../utils/api'; 
import Navbar from "../../components/navbar/Navbar";
import MovieCard from "../../components/movieCard/movieCard";
import "./category.css";

function Category() {
  const { categoryId } = useParams(); // Get the categoryId from the URL params
  const [category, setCategory] = useState(null);
  const [movies, setMovies] = useState([]);
  const [error, setError] = useState(null); // Add error state
  const token = localStorage.getItem("token");

  // Fetch category and movie IDs
  useEffect(() => {
    // If there's no token, redirect to the homepage
    if (!token) {
      window.location.href = "/";
    }

    const fetchCategoryAndMovies = async () => {
      try {
        const response = await axios.get(`/categories/${categoryId}`);

        const { category, movies } = response.data;
        setCategory(category);
        console.log(category);
        console.log(movies);
        console.log("the response is", response.data);

        // Fetch the movie details using movie IDs from movieList
        const movieDetails = await Promise.all(
          movies.movieList.map(async (movie) => {
            const movieResponse = await axios.get(`/movies/${movie._id}`);
            return movieResponse.data;
          })
        );

        setMovies(movieDetails);
        console.log(movieDetails);
      } catch (err) {
        setError("Failed to fetch category or movies data");
      }
    };

    if (categoryId) {
      fetchCategoryAndMovies();
    }
  }, [categoryId, token]);

  // Render loading or error states
  if (error) {
    return <div>{error}</div>;
  }

  if (!category) {
    return <div>Loading category details...</div>;
  }

  return (
    <div className="category-page">
      <Navbar />
      <div className="category-container">
        <h2>{category.name}</h2>
      </div>
      <div className="movies-container">
        {movies && movies.length > 0 ? (
          movies.map((movie, key) => (
            <MovieCard {...movie} key={key} />
          ))
        ) : (
          <p>No movies in this category</p>
        )}
      </div>
    </div>
  );
}

export default Category;
