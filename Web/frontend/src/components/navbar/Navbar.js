import React, { useState, useEffect, useRef } from "react";
import "./Navbar.css";
import { Link, useNavigate } from "react-router-dom";
import { FaSearch } from "react-icons/fa";
import axios from '../../utils/api'; 
import defaultAvatar from "../../Assets/avatars/avatar1.png";

function Navbar() {
  const [isScrolled, setIsScrolled] = useState(false);
  const [isSearchOpen, setIsSearchOpen] = useState(false);
  const [searchText, setSearchText] = useState("");
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);
  const [isCategoriesOpen, setIsCategoriesOpen] = useState(false);
  const [categories, setCategories] = useState([]);
  const [profileImg, setProfileImg] = useState(defaultAvatar);
  const [isAdmin, setIsAdmin] = useState(false);
  const settingsRef = useRef(null);
  const profileRef = useRef(null);
  const categoriesRef = useRef(null);
  const categoriesListRef = useRef(null);
  const searchRef = useRef(null);
  const token = localStorage.getItem("token");
  const navigate = useNavigate();

  useEffect(() => {
    if (!token) {
      window.location.href = "/";
    }
  }, [token]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axios.get("/categories/");
        console.log(response.data);
        setCategories(response.data);
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };
    fetchCategories();
  }, [token]);

  useEffect(() => {
    const fetchProfileImg = async () => {
      try {
        const decodedToken = JSON.parse(atob(token.split(".")[1]));
        const userId = decodedToken.id;
        setIsAdmin(decodedToken.isAdmin);

        const response = await axios.get(`/users/${userId}`);

        // Use the server-hosted image path
        const avatarName = response.data.profilePicture;
        const avatarUrl = `${axios.defaults.baseURL}/Assets/avatars/${avatarName}`;
        setProfileImg(avatarUrl);
      } catch (error) {
        console.error("Error fetching profile picture:", error);
        setProfileImg(defaultAvatar); 
      }
    };
    fetchProfileImg();
  }, [token]);

  useEffect(() => {
    const handleScroll = () => setIsScrolled(window.scrollY > 0);

    const handleClickOutside = (event) => {
      if (
        settingsRef.current &&
        !settingsRef.current.contains(event.target) &&
        !profileRef.current.contains(event.target)
      ) {
        setIsSettingsOpen(false);
      }

      if (
        searchRef.current &&
        !searchRef.current.contains(event.target)
      ) {
        setIsSearchOpen(false);
      }

      if (
        categoriesRef.current &&
        !categoriesRef.current.contains(event.target) &&
        categoriesListRef.current &&
        !categoriesListRef.current.contains(event.target)
      ) {
        setIsCategoriesOpen(false);
      }
    };

    window.addEventListener("scroll", handleScroll);
    document.addEventListener("click", handleClickOutside);

    return () => {
      window.removeEventListener("scroll", handleScroll);
      document.removeEventListener("click", handleClickOutside);
    };
  }, []);

  const handleProfileClick = (event) => {
    event.stopPropagation();
    setIsSettingsOpen((prev) => !prev);
  };

  const handleSearchClick = (event) => {
    event.stopPropagation();
    setIsSearchOpen((prev) => !prev);
  };

  const handleSearchChange = (event) => {
    setSearchText(event.target.value);
  };

  const handleSearchSubmit = (event) => {
    if (event.key === "Enter" && searchText.trim() !== "") {
      event.preventDefault();
      navigate(`/search?query=${encodeURIComponent(searchText)}`);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/");
  };

  return (
    <div className={isScrolled ? "navbar scrolled" : "navbar"}>
      <div className="container">
        <div className="left">
          <Link to="/browse" className="link">
            <img src="/chilflix-transperent.png" alt="Netflix logo" />
          </Link>
          <div className="links">
            <Link to="/browse" className="link">
              <span>Home</span>
            </Link>
            <div
              className="categories"
              onClick={() => setIsCategoriesOpen((prev) => !prev)}
              ref={categoriesRef}
            >
              <span>Categories</span>
              {isCategoriesOpen && (
                <div className="categoriesList" ref={categoriesListRef}>
                  {categories.length > 0 ? (
                    categories.map((category) => (
                      <Link
                        key={category._id}
                        to={`/categories/${category._id}`}
                        className="category"
                      >
                        {category.name}
                      </Link>
                    ))
                  ) : (
                    <p>No categories available</p>
                  )}
                </div>
              )}
            </div>
            {isAdmin && (
              <Link to="/Admin" className="link">
                <span>Admin</span>
              </Link>
            )}
          </div>
        </div>
        <div className="right">
          <div className="search" onClick={handleSearchClick} ref={searchRef}>
            <FaSearch />
            {isSearchOpen && (
              <form onSubmit={(e) => e.preventDefault()}>
                <label>
                  <input
                    type="text"
                    name="search"
                    value={searchText}
                    onChange={handleSearchChange}
                    onKeyDown={handleSearchSubmit}
                    placeholder="Search..."
                    autoFocus
                  />
                </label>
              </form>
            )}
          </div>
          <div className="profile" onClick={handleProfileClick} ref={profileRef}>
            <img src={profileImg} alt="Profile" />
            {isSettingsOpen && (
              <div className="options" ref={settingsRef}>
                <span onClick={() => navigate("/settings")}>Settings</span>
                <span onClick={handleLogout}>Logout</span>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default Navbar;
