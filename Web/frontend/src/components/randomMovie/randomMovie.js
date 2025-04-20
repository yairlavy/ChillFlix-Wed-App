import "./randomMovie.css";
import { Link } from "react-router-dom";
import { useState } from "react";
import axios from '../../utils/api'; 

function RandomMovie({ _id, backdrop_path, title, genres, runtime, overview, trailer }) {
  const [isHovered, setIsHovered] = useState(false);

  const videoSrc = trailer && trailer !== ''
    ? `${axios.defaults.baseURL}/Assets/movieAssets/${trailer}`
    : "/video.mp4"; // Default video if no trailer

  return (
    <Link to={`/movies/${_id}`} className="movie-link">
      <div
        key={_id}
        className="random-tile"
        onMouseEnter={() => setIsHovered(true)}
        onMouseLeave={() => setIsHovered(false)}
      >
        <div className="random__media">
          {/* Display video when hovered */}
          {isHovered ? (
            <video
              src={videoSrc}
              autoPlay
              muted
              loop
              className="random__video"
            />
          ) : (
            <div
              className="random__media-image"
              style={{
                backgroundImage:
                  backdrop_path.startsWith("/916x515")
                    ? `url(https://dummyjson.com/image${backdrop_path})`
                    : `${axios.defaults.baseURL}/Assets/movieAssets/${backdrop_path}`,
              }}
            />
          )}
        </div>
        <div className="random__details">
          <div className="random__title">{title}</div>
          <div className="random__genres">
            {genres && genres.length > 0 ? genres.join(", ") : "No genres available"}
          </div>
          <div className="random__duration">{runtime} min</div>
          <div className="random__overview">{overview}</div>
        </div>
      </div>
    </Link>
  );
}

export default RandomMovie;
