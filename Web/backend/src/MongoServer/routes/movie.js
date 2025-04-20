const express = require('express');
const router = express.Router();
const movieController = require('../controllers/movie');
const verify = require('../controllers/verify');

router.route('/')
    .get(verify, movieController.getAllPromotedCategories)
    .post(verify, movieController.createMovie);

router.route('/all')
    .get(verify, movieController.getAllMovies);
    
router.route('/:id')
    .get(verify, movieController.getMovieById)
    .put(verify, movieController.updateMovie)
    .delete(verify, movieController.deleteMovie);
    
router.route('/search/:query')
    .get(verify, movieController.GetMovieBasedOnQuery);

router.route('/:id/recommend')
    .get(verify, movieController.getRecommendedMovies)
    .post(verify, movieController.addRecommendedMovies)
    .delete(verify, movieController.removeRecommendedMovies);

module.exports = router;