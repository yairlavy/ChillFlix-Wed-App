import "./browseMovieCard.css"; 
import { Link } from "react-router-dom";
import axios from '../../utils/api'; 

function browseMovieCard({_id, backdrop_path, title, genres}) {
    return (
        <div className="tile">
            <Link to={`/movies/${_id}`} className="movie-link">
            <div>
                <img
                src={backdrop_path.startsWith('/916x515') ? `https://dummyjson.com/image${backdrop_path}` 
                : `${axios.defaults.baseURL}/Assets/movieAssets/${backdrop_path}`}
                alt={title}
                className="movie-card"
                />
            </div>
            <div className="tile__details">
                <div className="tile__title">{title}</div>
                <div className="tile__geners">
                {genres && genres.length > 0 ? genres.join(", ") : 'No genres available'}
                </div>
            </div>
            </Link>
        </div>
    );
}

export default browseMovieCard;
