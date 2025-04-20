const express = require('express');
const router = express.Router();
const categoryController = require('../controllers/category');
const verify = require('../controllers/verify');


router.route('/')
    .get(verify, categoryController.getAllCategories)
    .post(verify, categoryController.createCategory);
    
router.route('/:id')
    .get(verify, categoryController.getCategoryById)
    .patch(verify, categoryController.updateCategory)
    .delete(verify, categoryController.deleteCategory);

module.exports = router;