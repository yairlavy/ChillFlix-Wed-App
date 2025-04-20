import React, { useState, useEffect } from "react";
import axios from '../../utils/api'; 
import { useNavigate } from "react-router-dom";
import "./settings.css";

// Import avatar images
import avatar1 from "../../Assets/avatars/avatar1.png";
import avatar2 from "../../Assets/avatars/avatar2.png";
import avatar3 from "../../Assets/avatars/avatar3.png";

const Settings = () => {
  const [userDetails, setUserDetails] = useState({
    name: "",
    password: "",
    profilePicture: "avatar1.png", // Default avatar with name and extension
  });
  const [isLoading, setIsLoading] = useState(false);
  const [userId, setUserId] = useState(null);
  const [file, setFile] = useState(null); // State to handle uploaded file
  const token = localStorage.getItem("token");
  const navigate = useNavigate();

  // Array of avatar names with the .png extension
  const avatarImages = [
    { name: "avatar1.png", src: avatar1 },
    { name: "avatar2.png", src: avatar2 },
    { name: "avatar3.png", src: avatar3 },
  ];

  useEffect(() => {
    if (!token) {
      window.location.href = "/";
    } else {
      const decodedToken = JSON.parse(atob(token.split(".")[1]));
      setUserId(decodedToken.id); // Updating userId using setUserId
    }
  }, [token]);

  useEffect(() => {
    if (!userId) return;

    // Fetch the current user details
    const fetchUserDetails = async () => {
      try {
        const response = await axios.get(`/users/${userId}`);
        setUserDetails(response.data);
      } catch (error) {
        console.error("Error fetching user details:", error);
      }
    };

    fetchUserDetails();
  }, [userId, token]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserDetails((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleImageChange = (avatarName) => {
    setUserDetails((prevState) => ({
      ...prevState,
      profilePicture: avatarName, // Store only the name with .png
    }));
  };

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    setFile(selectedFile);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);

    const updatedUserDetails = {};
    if (userDetails.name) updatedUserDetails.name = userDetails.name;
    if (userDetails.password) updatedUserDetails.password = userDetails.password;

    // Check if a custom avatar file was uploaded
    if (file) {
      const formData = new FormData();
      formData.append("file", file);

      try {
        const uploadResponse = await axios.post("/upload/avatars", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });

        updatedUserDetails.profilePicture = uploadResponse.data.storageName; // Use uploaded file's storage name
      } catch (error) {
        console.error("Error uploading custom avatar:", error);
        alert("Failed to upload custom avatar.");
        setIsLoading(false);
        return;
      }
    } else {
      // If no custom file uploaded, use selected avatar name with .png
      updatedUserDetails.profilePicture = userDetails.profilePicture || avatarImages[0].name; // Default to the first avatar if none selected
    }

    try {
      const response = await axios.patch(`/users/${userId}`, updatedUserDetails);
      alert("Profile updated successfully!");
      setUserDetails((prevState) => ({
        ...prevState,
        ...response.data,
      }));
    } catch (error) {
      console.error("Error updating profile:", error);
      alert("Failed to update profile.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="settings">
      <button className="button go-back-button" onClick={() => navigate("/browse")}>
        Go Back
      </button>

      <h2>Edit Profile</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Name:
          <input
            type="text"
            name="name"
            value={userDetails.name}
            onChange={handleInputChange}
            placeholder="Enter your new name (only if you want to change it)"
          />
        </label>

        <label>
          Password:
          <input
            type="password"
            name="password"
            onChange={handleInputChange}
            minLength="8"
            placeholder="Enter new password (only if you want to change it)"
          />
        </label>

        <label>
          Profile Picture:
          <div className="avatar-selection">
            {avatarImages.map((avatar, index) => (
              <div key={index} className="avatar-option">
                <img
                  src={avatar.src}
                  alt={`Avatar ${avatar.name}`}
                  className={`avatar-img ${userDetails.profilePicture === avatar.name ? "selected" : ""}`}
                  onClick={() => handleImageChange(avatar.name)} // Store the avatar name with .png
                />
              </div>
            ))}
          </div>
        </label>

        {/* File input for custom avatar */}
        <label>
          Upload Custom Avatar:
          <input
            type="file"
            accept="image/png, image/jpeg"
            onChange={handleFileChange}
            placeholder="Upload a custom avatar"
          />
        </label>

        <button type="submit" disabled={isLoading}>
          {isLoading ? "Saving..." : "Save Changes"}
        </button>
      </form>
    </div>
  );
};

export default Settings;
