const express = require('express');
const router = express.Router();
const userController = require('../controllers/user');
const verify = require('../controllers/verify');

router.route('/')
  .post(userController.createUser); // Public: Signup

router.route('/all')
  .get(verify, userController.getAllUsers); // Protected: Only logged-in users

router.route('/:id')
  .get(verify, userController.getUserById) // Protected: Only logged-in users
  .patch(verify, userController.updateUserDetails); // Protected: Only logged-in users

module.exports = router;
