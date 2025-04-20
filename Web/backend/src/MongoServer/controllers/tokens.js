const jwt = require('jsonwebtoken');
const userService = require('../services/user');
const secretKey = 'your_secret_key'; 

const authentication = async (req, res) => {
  try {
    const { username, password } = req.body;

    // Validate input
    if (!username || !password) {
      return res.status(400).json({ error: 'Missing required fields: username, password' });
    }

    // Find user and verify password
    const user = await userService.findUserByUsernameAndPassword(username, password);
    if (!user) {
      return res.status(401).json({ error: 'Invalid username or password' });
    }

    // Generate JWT token
    const token = jwt.sign(
      { id: user.id ,
        isAdmin: user.isAdmin
      },
      secretKey,
      { expiresIn: '1h' } // Token expires in 1 hour
    );

    res.status(200).json({ token });
  } catch (error) {
    console.error('Error in authentication:', error.message);
    res.status(500).json({ error: 'Failed to authenticate user' });
  }
};

module.exports = { authentication };
