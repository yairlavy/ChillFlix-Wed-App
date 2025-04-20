const categoryService = require('../services/category');
const Movie = require('../services/movie');

const createCategory = async (req, res) => {
    if(req.user.isAdmin) {
        try {
            const category = await categoryService.createCategory(req.body);
            res.status(201).json(category);
        } catch (err) {
            console.error(err);
            res.status(400).json({ error: 'Bad Request' });
        }
    } 
    else {
        res.status(403).json({ error: 'Not authorized to create categories' });
    }
};

const getAllCategories = async (req, res) => {
    try {
        const categories = await categoryService.getAllCategories();
        res.status(200).json(categories);
    } catch (err) {
        console.error(err);
        res.status(404).json({ error: 'Categories not found' });
    }
};

const getCategoryById = async (req, res) => {
    try {
        const category = await categoryService.getCategoryById(req.params.id);
        if (!category) {
            return res.status(404).json({ error: 'Category not found' });
        }

      // Fetch movies in the same genre and their count
        const movies = await Movie.findMovieBasedOnKey({genres: category.name});

      // Return the category along with movies and the movie count
        res.status(200).json({ category, movies});

    } catch (err) {
        console.error(err);
        res.status(404).json({ error: 'Category not found' });
    }
};

const updateCategory = async (req, res) => {
    if(req.user.isAdmin) {
        try {
            const category = await categoryService.updateCategory(req.params.id, req.body);
            if (!category) {
                return res.status(404).json({ error: 'Category not found' });
            }
            res.status(204).json(category);
        } catch (err) {
            console.error(err);
            res.status(400).json({ error: 'Bad request' });
        }
    }
    else {
        res.status(403).json({ error: 'Not authorized to update categories' });
    }
};

const deleteCategory = async (req, res) => {
    if(req.user.isAdmin) {
        try {
            const category = await categoryService.deleteCategory(req.params.id);
            if (!category) {
                return res.status(404).json({ error: 'Category not found' });
            }
            res.status(204).json(category);
        } catch (err) {
            console.error(err);
            res.status(400).json({ error: 'Bad request' });
        }
    }
    else {
        res.status(403).json({ error: 'Not authorized to delete categories' });
    }
};

module.exports = { 
    createCategory, 
    getAllCategories, 
    getCategoryById, 
    updateCategory, 
    deleteCategory
};
