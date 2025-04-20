import React, { useState, useEffect } from 'react';
import axios from '../../utils/api'; 
import './AdminDashboard.css';

const AnalyticsPanel = () => {
    const [totalMovies, setTotalMovies] = useState(0);
    const [totalUsers, setTotalUsers] = useState(0);
    const [totalCategories, setTotalCategories] = useState(0);
    const token = localStorage.getItem('token');

    useEffect(() => {
        const fetchData = async () => {
            try {
                // Fetch movies
                const moviesResponse = await axios.get('/movies/all');
                setTotalMovies(moviesResponse.data ? moviesResponse.data.length : 0); // Ensure data exists

                // Fetch users
                const usersResponse = await axios.get('/users/all');
                setTotalUsers(usersResponse.data ? usersResponse.data.length : 0); // Ensure data exists

                // Fetch categories
                const categoriesResponse = await axios.get('/categories');
                setTotalCategories(categoriesResponse.data ? categoriesResponse.data.length : 0); // Ensure data exists
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, [token]); 

    return (
        <div className="analytics-panel">
            <div className="card">
                <h3>Total Movies</h3>
                <p>{totalMovies}</p>
            </div>
            <div className="card">
                <h3>Categories</h3>
                <p>{totalCategories}</p>
            </div>
            <div className="card">
                <h3>Total Users</h3>
                <p>{totalUsers}</p>
            </div>
        </div>
    );
};

export default AnalyticsPanel;
