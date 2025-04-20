const Category = require('../models/category');
const Movie = require('../models/movie');
const User = require('../services/user');

const createMovie = async (movieData) => {
    const movie = new Movie(movieData);
  
    if (!movieData.short_id) {
      const shortId = await User.generateShortId(movie._id, Movie);
      movie.short_id = shortId; 
    }
  
    // Add new categories (genres) after the movie has been saved
    await addNewCategories(movie.genres);
  
    return await movie.save();
};
  
// Add new categories to the database if they do not already exist
const addNewCategories = async (genres) => {
    // Process genres asynchronously
    for (const genre of genres) {
        if (genre) {
            // Check if the category already exists
            const existingCategory = await Category.findOne({ name: genre });

            // If not found, create a new category
            if (!existingCategory) {
                const newCategory = new Category({ name: genre });
                await newCategory.save();
            }
        }
    }
};

const getMovieById = async (id) => {
    return await Movie.findById(id);
};

const getAllMovies = async () => {
    return await Movie.find({});
};

//find movies base on objects of the schema or by string, option to find them randomly and how many to find
const findMovieBasedOnKey = async (searchParams, useAggregation = false, sample = 20) => {
    let query;

    // If searchParams is a string, perform a search across all text-based fields
    if (typeof searchParams === 'string' && searchParams.trim() !== '') {
        // Get all valid string fields from the Movie schema
        const validFields = Object.keys(Movie.schema.paths)
            .filter(field => Movie.schema.paths[field].instance === 'String');  // Only include String fields

        // Add fields to search inside embedded arrays like `age_Rating.description`
        const nestedFields = [
            'actors', 
            'creators',
            'age_Rating',
            'age_Rating.age',
            'age_Rating.description',
            'genres'
        ];

        // Combine the valid fields and nested fields
        const searchFields = [...validFields, ...nestedFields];

        // Create a regex-based query for all valid string fields and nested fields
        const regexQuery = {
            $or: searchFields.map(field => ({
                [field]: { $regex: searchParams, $options: 'i' } // case-insensitive search ("dog" = "DOG" = "Dog")
            }))
        };

        query = regexQuery;
    
    } else if (typeof searchParams === 'object' && Object.keys(searchParams).length > 0) {
        // If searchParams is an object, use it directly as the query
        query = searchParams;
    } else {
        throw new Error('Invalid search parameters. Must be a string or a valid object.');
    }

    // If useAggregation is true, use the aggregation pipeline
    if (useAggregation) {
        const aggregationPipeline = [
            { $match: query },  // Match based on the query
            { $sample: { size: sample } },  // Randomly sample `sample` documents (default 20)
            { $project: { _id: 1 } }  // Only return the _id field
        ];

        // Perform the aggregation query
        const movies = await Movie.aggregate(aggregationPipeline);
        const movieCount = movies.length;

        return {
            movieCount: movieCount,  // The total number of movies found
            movieList: movies  // The list of movie documents (just IDs)
        };
    }

    // If useAggregation is false, use find()
    const movies = await Movie.find(query).select('_id');  // Only return the _id field
    const movieCount = movies.length;

    return {
        movieCount: movieCount,  // The total number of movies
        movieList: movies  // The list of movie IDs
    };
};

// Update movie and if you enter a new category also create it
const updateMovie = async (id, movieData) => {
    // Find and update the movie by its ID
    const movie = await Movie.findByIdAndUpdate(
        id,
        { $set: movieData },  // Update the movie with movieData
        { new: true, overwrite: true }  // Return updated document
    );
    await addNewCategories(movie.genres);

    return movie;  // Return the updated movie
};


const deleteMovie = async (id) => {
    const movie = await getMovieById(id);
    if (!movie) return null;
    await movie.deleteOne();
    return movie;
};

module.exports = { 
    createMovie, 
    getAllMovies, 
    getMovieById,
    updateMovie, 
    deleteMovie, 
    findMovieBasedOnKey
};