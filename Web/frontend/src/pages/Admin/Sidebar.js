import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; // For navigation

import "./AdminDashboard.css";

const Sidebar = ({ setActivePanel, setOperation }) => {
    const [isSidebarOpen, setIsSidebarOpen] = useState(true);
    const navigate = useNavigate();

    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };

    return (
        <div className={`sidebar ${isSidebarOpen ? "" : "collapsed"}`}>
            <div className="sidebar-header">
                <button className="sidebar-toggle" onClick={toggleSidebar}>
                    {isSidebarOpen ? "⇦" : "⇨"}
                </button>
                <button
                    className="sidebar-toggle" id = "sidebar-button-go-back-button"
                    onClick={() => navigate("/browse")}
                >
                    Go Back
                </button>
            </div>
            {isSidebarOpen && (
                <>
                    <h2>Admin Panel</h2>
                    <div className="sidebar-buttons">
                        {/* Category Management */}
                        <button
                            className="sidebar-button"
                            onClick={() => {
                                setActivePanel("operation");
                                setOperation({ action: "create", target: "category" });
                            }}
                        >
                            Add Category
                        </button>
                        <button
                            className="sidebar-button"
                            onClick={() => {
                                setActivePanel("operation");
                                setOperation({ action: "edit", target: "category" });
                            }}
                        >
                            Edit Category
                        </button>
                        <button
                            className="sidebar-button"
                            onClick={() => {
                                setActivePanel("operation");
                                setOperation({ action: "delete", target: "category" });
                            }}
                        >
                            Delete Category
                        </button>

                        {/* Movie Management */}
                        <button
                            className="sidebar-button"
                            onClick={() => {
                                setActivePanel("operation");
                                setOperation({ action: "create", target: "movie" });
                            }}
                        >
                            Add Movie
                        </button>
                        <button
                            className="sidebar-button"
                            onClick={() => {
                                setActivePanel("operation");
                                setOperation({ action: "edit", target: "movie" });
                            }}
                        >
                            Edit Movie
                        </button>
                        <button
                            className="sidebar-button"
                            onClick={() => {
                                setActivePanel("operation");
                                setOperation({ action: "delete", target: "movie" });
                            }}
                        >
                            Delete Movie
                        </button>

                        {/* Analytics */}
                        <button
                            className="sidebar-button"
                            onClick={() => setActivePanel("analytics")}
                        >
                            View Analytics
                        </button>
                    </div>
                </>
            )}
        </div>
    );
};

export default Sidebar;
