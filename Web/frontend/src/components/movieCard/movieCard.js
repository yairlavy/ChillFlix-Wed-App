import "./movieCard.css"; 
import { Link } from "react-router-dom";
import axios from '../../utils/api'; 


function movieCard({_id, backdrop_path, title}) {
    return (
        <div className="movie">
            <Link to={`/movies/${_id}`}>
            <img
                src={backdrop_path.startsWith('/916x515') ? `https://dummyjson.com/image${backdrop_path}` 
                    : `${axios.defaults.baseURL}/Assets/movieAssets/${backdrop_path}`}
                alt={title}
                className="movie-poster"
            />
            <div className="movie-info">
            <p className="movie-title">{title}</p>
            </div>
            </Link>
        </div>
    );
}

export default movieCard;
