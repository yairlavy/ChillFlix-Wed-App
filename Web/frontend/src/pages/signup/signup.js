import React, { useState, useEffect } from 'react';
import './signup.css';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import axios from '../../utils/api'; // Axios instance
import logo from '../../Assets/logo.png';
import avatar1 from '../../Assets/avatars/avatar1.png';
import avatar2 from '../../Assets/avatars/avatar2.png';
import avatar3 from '../../Assets/avatars/avatar3.png';

const Signup = () => {
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [passwordVerification, setPasswordVerification] = useState('');
  const [name, setName] = useState('');
  const [selectedAvatar, setSelectedAvatar] = useState('avatar1.png'); 
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    const emailFromQuery = queryParams.get('email');
    if (emailFromQuery) {
      setEmail(emailFromQuery);
    }
  }, [location]);

  const handleSignup = async (e) => {
    e.preventDefault();

    if (password !== passwordVerification) {
      setError('Passwords do not match');
      return;
    }

    try {
      await axios.post('/users', {
        email,
        username,
        password,
        name,
        profilePicture: selectedAvatar, 
      });
      alert('Signup successful! Please log in.');
      navigate('/login');
    } catch (err) {
      setError(err.response?.data?.error || 'Signup failed');
      resetForm(); 
    }
  };

  // Function to reset the form fields
  const resetForm = () => {
    setEmail('');
    setUsername('');
    setPassword('');
    setPasswordVerification('');
    setName('');
    setSelectedAvatar('avatar1.png'); // Reset to the default avatar
  };

  // Map avatar names with the .png extension to their image sources
  const avatarImages = {
    'avatar1.png': avatar1,
    'avatar2.png': avatar2,
    'avatar3.png': avatar3,
  };

  return (
    <div className="signup">
      <Link to="/">
        <img src={logo} className="signup-logo" alt="Netflix Logo" />
      </Link>
      <div className="signup-form">
        <h1>Sign Up</h1>
        <form onSubmit={handleSignup}>
          <input
            type="email"
            placeholder="Email Address"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            minLength="8"
            onChange={(e) => setPassword(e.target.value)}
          />
          <input
            type="password"
            placeholder="Password Verification"
            value={passwordVerification}
            onChange={(e) => setPasswordVerification(e.target.value)}
          />
          <input
            type="text"
            placeholder="Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <div className="avatar-selection">
            <p>Select your avatar:</p>
            <div className="avatar-options">
              {Object.keys(avatarImages).map((avatarName) => (
                <img
                  key={avatarName}
                  src={avatarImages[avatarName]}
                  alt={avatarName}
                  className={selectedAvatar === avatarName ? 'selected-avatar' : ''}
                  onClick={() => setSelectedAvatar(avatarName)} 
                />
              ))}
            </div>
          </div>
          {error && <p className="error">{error}</p>}
          <button type="submit">Sign Up</button>
        </form>
        <div className="form-switch">
          <span>Already have an account? </span>
          <Link to="/login" className="sign-in">Sign In</Link>
        </div>
      </div>
    </div>
  );
};

export default Signup;
