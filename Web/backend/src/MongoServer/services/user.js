const User = require("../models/user");
const Movie = require('../models/movie');

// Function to generate a unique short ID
const generateShortId = async (_id, type, modulo = 10000000) => {
  let unique = false;
  let shortId;

  // Take the last 8 characters of the ObjectId
  const hexId = _id.toString().slice(-8);

  while (!unique) {

    // Add some randomness to ensure uniqueness
    const random = Math.floor(Math.random() * 10000);
    const combined = `${hexId}${random.toString(16)}`;

    // Convert combined string to an integer using base 16 (hex to decimal) and apply modulo
    shortId = parseInt(combined, 16) % modulo;

    // Check database for uniqueness if not unique try again
    const existingUser = await type.findOne({ short_id: shortId });
    if (!existingUser) {
      unique = true;
    }
  }

  return shortId;
}

// Create a new user
const createUser = async (userData) => {
  try {
    const user = new User(userData);

    // generate the shortId
    const shortId = await generateShortId(user._id, User);
    user.short_id = shortId;

    console.log(`Generated shortId: ${shortId}`);

    return await user.save();

  } catch (error) {
    if (error.code === 11000) {
      throw new Error(error, "Username or email already exists");
    }
    throw error;
  }
};

// Find user by username
const findUserByUsername = async (username) => {
  return await User.findOne({ username });
};

// Find user by username and password
const findUserByUsernameAndPassword = async (username, password) => {
  return await User.findOne({ username, password });
};

// Get user by ID
const getUserById = async (id) => {
  const user = await User.findById(id);
  if (!user) throw new Error("User not found");
  return user;
};

// Get all users
const getAllUsers = async () => {
  return await User.find();
};

// Update user details
const updateUserDetails = async (id, updates) => {
  try {
    const user = await User.findById(id);
    if (!user) {
      throw new Error("User not found");
    }

    Object.keys(updates).forEach((key) => {
      user[key] = updates[key];
    });

    await user.save();
    return user;
  } catch (error) {
    if (error.code === 11000) {
      throw new Error("Username or email already exists");
    }
    throw new Error("An error occurred while updating user details");
  }
};

// Add movie to user's watchlist
const addMovieToUser = async (userId, shortId, movieId) => {
  try {
    const user = await User.findByIdAndUpdate(
      userId,
      { $set: { [`watchlist.${shortId}`]: movieId } },
      { new: true }
    );
    if (!user) {
      throw new Error("User not found");
    }
    return user;
  } catch (error) {
    throw new Error("Failed to add movie to user's watchlist");
  }
};

// Remove movie from a single user's watchlist
const removeMovieFromUser = async (userId, shortId) => {
  try {
    const user = await User.findByIdAndUpdate(
      userId,
      { $unset: { [`watchlist.${shortId}`]: "" } },
      { new: true }
    );
    if (!user) {
      throw new Error("User not found");
    }
    return user;
  } catch (error) {
    throw new Error(`Failed to remove movie from user ${userId}'s watchlist`);
  }
};

module.exports = {
  createUser,
  getAllUsers,
  getUserById,
  updateUserDetails,
  findUserByUsername,
  findUserByUsernameAndPassword,
  addMovieToUser,
  removeMovieFromUser,
  generateShortId
};
