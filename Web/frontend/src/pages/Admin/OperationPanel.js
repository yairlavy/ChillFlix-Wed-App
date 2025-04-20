import React from "react";
import AddOperation from "./AddOperation";
import EditDeleteOperation from "./EditDeleteOperation";
import "./AdminDashboard.css";

const OperationPanel = ({ operation }) => {
    return (
        <div className="operation-panel">
            {operation.action === "create" ? (
                <AddOperation operation={operation} />
            ) : (
                <EditDeleteOperation operation={operation} />
            )}
        </div>
    );
};

export default OperationPanel;
