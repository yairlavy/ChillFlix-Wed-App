const jwt = require('jsonwebtoken');
const userService = require('../services/user');
const secretKey = 'your_secret_key';

async function verify(req, res, next) {
  const authHeader = req.headers['authorization'];
  if (!authHeader) {
    return res.status(403).json({ error: 'Authorization header not provided' });
  }

  const token = authHeader.split(' ')[1]; // Extract token
  try {
    const decoded = jwt.verify(token, secretKey); // Verify token
    const user = await userService.getUserById(decoded.id); // Fetch user details
    if (!user) {
      return res.status(404).json({ error: 'User not found' });
    }

    req.user = user; // Attach user info to the request
    next();
  } catch (error) {
    console.error('Token verification failed:', error.message);
    res.status(401).json({ error: 'Invalid or expired token' });
  }
}

module.exports = verify;
