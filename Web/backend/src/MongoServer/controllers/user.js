const userService = require('../services/user');

// Create user
const createUser = async (req, res) => {
  try {
      const { name, email, username, password, watchlist } = req.body;
  
      if (!name || !email || !username || !password) {
        return res.status(400).json({ error: 'All fields are required' });
      }

      if (watchlist) {
        return res.status(400).json({ error: 'Cannot add movies to the watchlist during user creation' });
      }
  
      const user = await userService.createUser(req.body);
      res.status(201).json({ message: 'User created successfully', user });
    } catch (error) {
      res.status(400).json({ error: error.message });
  }
};
  
// Get all users
const getAllUsers = async (req, res) => {
  if(req.user.isAdmin) {
    try {
      const users = await userService.getAllUsers();
      res.status(200).json(users);
    } catch (error) {
      res.status(400).json({ error: error.message });
    }
  } else {
    res.status(401).json({ error: 'Not authorized to see all users' });
  }
};

// Get user by ID
const getUserById = async (req, res) => {
  try {
    const user = await userService.getUserById(req.params.id);
    if (!user) {
      return res.status(404).json({ error: 'User not found' });
    }
    res.status(200).json(user);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Update user details
const updateUserDetails = async (req, res) => {
  if (req.user.id === req.params.id || req.user.isAdmin) {
    try {
      const updates = req.body;
      const user = await userService.updateUserDetails(req.params.id, updates);
      if (!user) {
        return res.status(404).json({ error: 'User not found' });
      }
      if (updates.watchlist) {
        return res.status(400).json({ error: 'Cannot add movies to the watchlist during user update' });
      }
      res.status(200).json({ message: 'User updated successfully', user });
    } catch (error) {
      res.status(500).json({ error: error.message });
    }
  }
  else {
    res.status(403).json({ error: 'Not authorized to update user' });
  }
};

module.exports = {
  createUser,
  getAllUsers,
  getUserById,
  updateUserDetails,
};