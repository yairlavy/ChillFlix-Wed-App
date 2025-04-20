const express = require('express');
const router = express.Router();
const tokensController = require('../controllers/tokens');

// POST /api/tokens for user login
router.post('/', tokensController.authentication);

module.exports = router;
