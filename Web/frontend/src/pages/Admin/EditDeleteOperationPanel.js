import React, { useState, useEffect, useCallback } from "react";
import axios from '../../utils/api'; 
import "./AdminDashboard.css";

const EditDeleteOperationPanel = ({ target, action }) => {
    const [options, setOptions] = useState([]); 
    const [selectedOption, setSelectedOption] = useState(""); 
    const [itemDetails, setItemDetails] = useState({}); 
    const [isLoading, setIsLoading] = useState(false);
    const [files, setFiles] = useState({});
    const token = localStorage.getItem("token");

    // Fetch options for the dropdown
    const fetchOptions = useCallback(async () => {
        try {
          const endpoint = target === "category" ? "/categories" : "/movies/all";
          const response = await axios.get(endpoint);
          setOptions(response.data);
        } catch (error) {
          console.error("Error fetching options:", error);
          setOptions([]);
        }
      }, [target]);
    
      useEffect(() => {
        fetchOptions();
      }, [fetchOptions, token]);

    // Fetch item details for editing
    useEffect(() => {
        const fetchItemDetails = async () => {
          if (selectedOption && action !== "delete") {
            try {
              let endpoint;
              if (target === "category") {
                endpoint = `/categories/${selectedOption}`;
              } else {
                endpoint = `/movies/${selectedOption}`;
              }
      
              const response = await axios.get(endpoint);
      
              if (target === "category") {
                setItemDetails(response.data.category);
              } else {
                setItemDetails(response.data);
              }
            } catch (error) {
              console.error("Error fetching item details:", error);
            }
          }
        };

        fetchItemDetails();
    }, [selectedOption, target, token, action]);

    const handleInputChange = (e) => {
        const { name, value, type, checked } = e.target;

        if (name === "genres") {
            const genresArray = value.split(",");
            setItemDetails((prevData) => ({
                ...prevData,
                [name]: genresArray,
            }));
        } else if (type === "checkbox") {
            setItemDetails((prevData) => ({
                ...prevData,
                [name]: checked,
            }));
        } else {
            setItemDetails((prevData) => ({
                ...prevData,
                [name]: value,
            }));
        }
    };

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        setFiles((prevFiles) => ({
            ...prevFiles,
            [e.target.name]: file,
        }));

        setItemDetails((prevDetails) => ({
            ...prevDetails,
            [e.target.name]: file.name, 
        }));
    };

    const handleAction = async () => {
        try {
            setIsLoading(true);

            let updatedItemDetails = { ...itemDetails };

            if (action === "delete") {
                const url =
                    target === "category"
                        ? `/categories/${selectedOption}`
                        : `/movies/${selectedOption}`;
                await axios.delete(`${url}`);

                alert(`${action} successful!`);

                // Refresh options after deletion
                fetchOptions();

            } else {
                const formData = new FormData();

                if (files.poster_path) formData.append("poster_path", files.poster_path);
                if (files.backdrop_path) formData.append("backdrop_path", files.backdrop_path);
                if (files.trailer) formData.append("trailer", files.trailer);

                let uploadResponse;
                if (formData.has("poster_path") || formData.has("backdrop_path") || formData.has("trailer")) {
                    try {
                        uploadResponse = await axios.post("/upload/movieAsset", formData, {
                            headers: {
                                "Content-Type": "multipart/form-data",
                            },
                        });

                        console.log("Upload Response Data:", uploadResponse.data);

                        if (uploadResponse && uploadResponse.data.storageNames) {
                            const { poster_path, backdrop_path, trailer } = uploadResponse.data.storageNames;

                            // Update item details with the filenames returned by the server
                            updatedItemDetails = {
                                ...updatedItemDetails,
                                poster_path,
                                backdrop_path,
                                trailer,
                            };
                        }
                    } catch (error) {
                        console.error("Error during file upload:", error);
                    }
                }
                                
                const url =
                    target === "category"
                        ? `/categories/${selectedOption}`
                        : `/movies/${selectedOption}`;

                if (target === "category" && action === "edit") {
                    await axios.patch(`${url}`, updatedItemDetails);
                } else if (target === "movie" && action === "edit") {
                    await axios.put(`${url}`, updatedItemDetails);
                }

                alert(`${action} successful!`);

                // Refresh options after update
                fetchOptions();

                console.log("update data:", updatedItemDetails);
            }

        } catch (error) {
            console.error(`Error during ${action}:`, error);
            alert(`Error during ${action}: ${error.message}`);
        } finally {
            setSelectedOption("");
            setItemDetails({});
            setFiles({});
            setIsLoading(false);
        }
    };

    const renderFormFields = () => {
        if (action !== "delete") {
            if (target === "category") {
                return (
                    <>
                        <label>
                            Name:
                            <input
                                type="text"
                                name="name"
                                value={itemDetails.name || ""}
                                onChange={handleInputChange}
                                required
                            />
                        </label>
                        <label>
                            Promoted:
                            <input
                                type="checkbox"
                                name="promoted"
                                checked={itemDetails.promoted || false}
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
                                value={itemDetails.title || ""}
                                onChange={handleInputChange}
                                required
                            />
                        </label>
                        <label>
                            Genres (comma-separated):
                            <input
                                type="text"
                                name="genres"
                                value={itemDetails.genres || ""}
                                onChange={handleInputChange}
                            />
                        </label>
                        <label>
                            Overview:
                            <textarea
                                name="overview"
                                value={itemDetails.overview || ""}
                                onChange={handleInputChange}
                                required
                            />
                        </label>
                        <label>
                            Runtime:
                            <input
                                type="text"
                                name="runtime"
                                value={itemDetails.runtime || ""}
                                onChange={handleInputChange}
                            />
                        </label>
                        <label>
                            Upload poster_path Image:
                            <input
                                type="file"
                                name="poster_path"
                                onChange={handleFileChange}
                            />
                            {itemDetails.poster_path && <p>Current poster_path: {itemDetails.poster_path}</p>}
                        </label>
                        <label>
                            Upload backdrop_path Image:
                            <input
                                type="file"
                                name="backdrop_path"
                                onChange={handleFileChange}
                            />
                            {itemDetails.backdrop_path && <p>Current backdrop_path: {itemDetails.backdrop_path}</p>}
                        </label>
                        <label>
                            Trailer (File upload):
                            <input
                                type="file"
                                name="trailer"
                                onChange={handleFileChange}
                            />
                            {itemDetails.trailer && <p>Current Trailer: {itemDetails.trailer}</p>}
                        </label>
                    </>
                );
            }
        }
        return null;
    };

    return (
        <div className="operation-panel">
            <h2>{`${action.charAt(0).toUpperCase() + action.slice(1)} ${target}`}</h2>
            <select
                className="dropdown"
                value={selectedOption}
                onChange={(e) => setSelectedOption(e.target.value)}
            >
                <option value="" disabled>
                    Select {target}
                </option>
                {Array.isArray(options) && options.map((option) => (
                    <option
                        key={option._id}
                        value={option._id}
                    >
                        {target === "category" ? option.name : option.title}
                    </option>
                ))}
            </select>

            {selectedOption && action !== "delete" && (
                <form>
                    {renderFormFields()}
                    <button
                        type="button"
                        className="button operation-button"
                        onClick={handleAction}
                        disabled={isLoading || !selectedOption}
                    >
                        {isLoading ? "Processing..." : action}
                    </button>
                </form>
            )}

            {selectedOption && action === "delete" && (
                <button
                    type="button"
                    className="button operation-button"
                    onClick={handleAction}
                    disabled={isLoading || !selectedOption}
                >
                    {isLoading ? "Processing..." : "Delete"}
                </button>
            )}
        </div>
    );
};

export default EditDeleteOperationPanel;
