import React, { useState } from 'react';
import './home.css';
import { useNavigate } from 'react-router-dom';
import logo from '../../Assets/logo.png';

const Home = () => {
  const [email, setEmail] = useState('');
  const navigate = useNavigate();

  const handleStart = (e) => {
    e.preventDefault();
    navigate(`/signup?email=${encodeURIComponent(email)}`);
  };

  return (
    <div className="home">
      <header className="home-header">
        <img src={logo} className="home-logo" alt="Netflix Logo" />
        <div className="home-buttons">
          <button onClick={() => navigate('/login')} className="home-login">Login</button>
          <button onClick={() => navigate('/signup')} className="home-signup">Sign Up</button>
        </div>
      </header>
      <div className="home-content">
        <h1 className="home-title">Movies, TV Shows, and More</h1>
        <h2 className="home-subtitle">Watch anywhere, anytime, without limits.</h2>
        <p className="home-email-note">Enter your email to create or restart your membership:</p>
        <form className="home-form" onSubmit={handleStart}>
          <input
            type="email"
            placeholder="Email Address"
            className="home-email-input"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <button type="submit" className="home-start-button">Get Started</button>
        </form>
      </div>
    </div>
  );
};

export default Home;

