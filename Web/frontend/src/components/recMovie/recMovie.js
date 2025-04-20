import "./recMovie.css"; 
import { Link } from "react-router-dom";
import axios from '../../utils/api'; 

function recMovie({_id, backdrop_path, title, genres, runtime}) {
    return (
        <div key={_id} className="recMovie">
        <Link to={`/movies/${_id}`}>
            <img
                src={backdrop_path.startsWith('/916x515') ? `https://dummyjson.com/image${backdrop_path}` 
                : `${axios.defaults.baseURL}/Assets/movieAssets/${backdrop_path}`}            alt={title}
            className="recMovie-poster"
            />
            <div className="recMovie-info">
            <p className="recInfo">Genres: {genres.join(", ")}</p>
            <p className="recInfo">Runtime: {runtime} minutes</p>
            </div>
        </Link>
        </div>
    );
}

export default recMovie;
