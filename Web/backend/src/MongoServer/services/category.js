const Category = require('../models/category');
const Movie = require('../services/movie');

const createCategory = async (categoryData) => {
    const { name, promoted } = categoryData;
    const category = new Category({ name, promoted });
    return await category.save();
};

const getCategoryById = async (id) => {
    const category = await Category.findById(id);
    if (!category) return null;

    return category;
};

const getAllCategories = async () => {
    return await Category.find({});
};

// Helper function to update or remove the category from the movie genres
const updateMovieGenres = async (movieId, categoryName, action) => {
    const Remove = 0;
    const Add = 1;    
    
    const movie = await Movie.getMovieById(movieId);
    if (!movie) return;

    let updatedGenres = [...movie.genres];

    if (action === Add) {
        // Add the new category name if it's not already in the genres
        if (!updatedGenres.includes(categoryName.trim())) {
            updatedGenres.push(categoryName.trim());
        }
    } else if (action === Remove) {
        // Remove the category name from the genres array
        updatedGenres = updatedGenres.filter(genre => genre.trim() !== categoryName.trim());
    }

    // Update the movie with the new genres array
    await Movie.updateMovie(movieId, { genres: updatedGenres });
};

// Update category details and update movie genres if necessary
const updateCategory = async (id, categoryData) => {
    const { name, promoted } = categoryData;
    const category = await getCategoryById(id);
    if (!category) return null;

    const Remove = 0;
    const Add = 1;

    const oldCategoryName = category.name;
    if (name) category.name = name;
    if (promoted !== undefined) category.promoted = promoted;

    await category.save();

    // If the category name is being changed, update the movies as well
    if (category.name !== oldCategoryName) {
        // Remove the old category from movies' genres and then add the new category
        const { movieCount, movieList } = await Movie.findMovieBasedOnKey({ genres: { $in: [oldCategoryName] } });
        
        if (movieCount > 0) {
            for (const movie of movieList) {
                await updateMovieGenres(movie._id, oldCategoryName, Remove);
                await updateMovieGenres(movie._id, name, Add);
            }
        }
    }

    return category;
};

// Delete category and remove it from all movies that it is in their genres
const deleteCategory = async (id) => {
    const category = await getCategoryById(id);
    if (!category) return null;

    // Fetch movies with the genre that matches category name
    const { movieCount, movieList } = await Movie.findMovieBasedOnKey({ genres: category.name });

    const Remove = 0;

    if (movieCount > 0) {
        for (const movie of movieList) {
            // Remove the category from the movie's genres
            await updateMovieGenres(movie._id, category.name, Remove); 
        }
    }

    // Delete the category after updating movies
    await category.deleteOne();
    return category;
};

const getAllPromotedCategories = async () => {
    const category = await Category.find({ promoted: true });
    if (!category) return null;
    return category;
};

module.exports = { 
    createCategory,
    getAllCategories, 
    getCategoryById, 
    updateCategory, 
    deleteCategory, 
    getAllPromotedCategories 
};