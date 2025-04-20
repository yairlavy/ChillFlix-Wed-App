import React, { useState } from "react";
import axios from '../../utils/api'; 
import "./AdminDashboard.css";

const AddOperationPanel = ({ target }) => {
    const [inputData, setInputData] = useState({});
    const [files, setFiles] = useState({});
    const [isLoading, setIsLoading] = useState(false);

    const handleInputChange = (e) => {
        const { name, value, type, checked } = e.target;

        if (name === "genres") {
            const genresArray = value.split(",");
            setInputData((prevData) => ({
                ...prevData,
                [name]: genresArray,
            }));
        } else {
            setInputData((prevData) => ({
                ...prevData,
                [name]: type === "checkbox" ? checked : value,
            }));
        }
    };

    const handleFileChange = (e) => {
        setFiles({
            ...files,
            [e.target.name]: e.target.files[0],
        });
    };

    const handleAdd = async () => {
        try {
            setIsLoading(true);

            // Step 1: Upload movie assets (poster_path, backdrop_path, trailer)
            const formData = new FormData();
            if (files.poster_path) formData.append("poster_path", files.poster_path);
            if (files.backdrop_path) formData.append("backdrop_path", files.backdrop_path);
            if (files.trailer) formData.append("trailer", files.trailer);

        console.log([...formData.entries()]);

            let uploadResponse;
            if (Object.keys(files).length > 0) {
                uploadResponse = await axios.post(
                    "/upload/movieAsset",
                    formData,
                    {
                        headers: {
                            "Content-Type": "multipart/form-data",
                        },
                    }
                );
            }

            const { poster_path, backdrop_path, trailer } = uploadResponse
                ? uploadResponse.data.storageNames
                : {};

            // Step 2: Save movie data along with storage names
            const movieData = {
                ...inputData,
                poster_path,
                backdrop_path,
                trailer,
            };

            const endpoint = target === "category" ? "/categories" : "/movies";
            const response = await axios.post(endpoint, movieData);

            console.log("Add successful:", response.data);
            alert("Add successful!");
            setInputData({});
            setFiles({});
        } catch (error) {
            console.error("Error during add:", error);
            alert(`Error during add: ${error.message}`);
        } finally {
            setIsLoading(false);
        }
    };

    const renderFormFields = () => {
        if (target === "category") {
            return (
                <>
                    <label>
                        Name:
                        <input
                            type="text"
                            name="name"
                            value={inputData.name || ""}
                            onChange={handleInputChange}
                            required
                        />
                    </label>
                    <label>
                        Promoted:
                        <input
                            type="checkbox"
                            name="promoted"
                            checked={inputData.promoted || false}
                            onChange={handleInputChange}
                        />
                    </label>
                </>
            );
        } else if (target === "movie") {
            return (
                <>
                    <label>
                        Title:
                        <input
                            type="text"
                            name="title"
                            value={inputData.title || ""}
                            onChange={handleInputChange}
                            required
                        />
                    </label>
                    <label>
                        Genres (comma-separated):
                        <input
                            type="text"
                            name="genres"
                            value={inputData.genres || ""}
                            onChange={handleInputChange}
                        />
                    </label>
                    <label>
                        Runtime:
                        <input
                            type="text"
                            name="runtime"
                            value={inputData.runtime || ""}
                            onChange={handleInputChange}
                        />
                    </label>
                    <label>
                        Overview:
                        <textarea
                            name="overview"
                            value={inputData.overview || ""}
                            onChange={handleInputChange}
                            required
                        />
                    </label>
                    <label>
                        poster_path:
                        <input
                            type="file"
                            name="poster_path"
                            onChange={handleFileChange}
                        />
                    </label>
                    <label>
                        backdrop_path:
                        <input
                            type="file"
                            name="backdrop_path"
                            onChange={handleFileChange}
                        />
                    </label>
                    <label>
                        Trailer:
                        <input
                            type="file"
                            name="trailer"
                            onChange={handleFileChange}
                        />
                    </label>
                </>
            );
        }
    };

    return (
        <div className="operation-panel">
            <h2>Add {target.charAt(0).toUpperCase() + target.slice(1)}</h2>
            <form>
                {renderFormFields()}
                <button
                    type="button"
                    className="button operation-button"
                    onClick={handleAdd}
                    disabled={isLoading}
                    style={buttonStyle}
                >
                    {isLoading ? "Processing..." : "Add"}
                </button>
            </form>
        </div>
    );
};

const buttonStyle = {
    backgroundColor: "#e50914",
    color: "white",
    border: "none",
    padding: "15px 30px",
    fontSize: "1.2rem",
    borderRadius: "5px",
    cursor: "pointer",
};

export default AddOperationPanel;
