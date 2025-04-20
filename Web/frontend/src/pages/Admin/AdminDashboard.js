import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; 
import Sidebar from "./Sidebar";
import AnalyticsPanel from "./AnalyticsPanel";
import AddOperationPanel from "./AddOperation";
import EditDeleteOperationPanel from "./EditDeleteOperationPanel";
import "./AdminDashboard.css";

const AdminDashboard = () => {
    const [activePanel, setActivePanel] = useState("analytics");
    const [operation, setOperation] = useState({});
    const navigate = useNavigate(); 
    const token = localStorage.getItem("token");

    // Check if the user is an admin
    useEffect(() => {
        if (token) {
            try {
                const decodedToken = JSON.parse(atob(token.split(".")[1]));
                if (!decodedToken.isAdmin) {
                    navigate("/browse"); // Redirect if not an admin
                }
            } catch (error) {
                console.error("Error decoding token:", error);
                navigate("/"); 
            }
        } else {
            // Redirect if no token is found
            navigate("/"); 
        }
    }, [token, navigate]);

    const renderPanel = () => {
        if (activePanel === "analytics") {
            return <AnalyticsPanel />;
        } else if (operation.action === "create") {
            return <AddOperationPanel target={operation.target} />;
        } else {
            return (
                <EditDeleteOperationPanel
                    target={operation.target}
                    action={operation.action}
                />
            );
        }
    };

    return (
        <div className="admin-dashboard">
            <Sidebar setActivePanel={setActivePanel} setOperation={setOperation} />
            <div className="dashboard-content">
                {renderPanel()}
            </div>
        </div>
    );
};

export default AdminDashboard;
