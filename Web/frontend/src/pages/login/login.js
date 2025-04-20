import React, { useState } from 'react';
import './login.css';
import { useNavigate, Link } from 'react-router-dom';
import axios from '../../utils/api'; // Axios instance
import logo from '../../Assets/logo.png';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('/tokens', { username, password });
      const { token } = response.data;
  
      localStorage.setItem('token', token); // Save token to localStorage
      alert('Login successful!');
      navigate('/browse'); // Redirect to home page
    } catch (err) {
      setError(err.response?.data?.error || 'Login failed');
    }
  };

  return (
    <div className="login">
      <Link to="/">
        <img src={logo} className="login-logo" alt="Netflix Logo" />
      </Link>
      <div className="login-form">
        <h1>Sign In</h1>
        <form onSubmit={handleLogin}>
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
            onChange={(e) => setPassword(e.target.value)}
          />
          {error && <p className="error">{error}</p>}
          <button type="submit">Sign In</button>
        </form>
        <div className="form-switch">
          <span>New to Netflix? </span>
          <Link to="/signup" className="sign-up">Sign Up now</Link>
        </div>
      </div>
    </div>
  );
};

export default Login;
