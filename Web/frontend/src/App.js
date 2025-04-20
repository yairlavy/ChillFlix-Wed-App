import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import HomePage  from './pages/home/home';
import LoginPage  from './pages/login/login';
import SignUpPage from './pages/signup/signup';
import Browse from "./pages/browse/browse"; 
import Movie from "./pages/movie/movie";
import Category from "./pages/category/category";
import AdminDashboard from "./pages/Admin/AdminDashboard";
import MoviePlayer from "./pages/MoviePlayer/MoviePlayer";
import Search from "./pages/search/search";
import Settings from "./pages/settings/settings";

function App() {
  return (
    <Router>
      <Routes>
      <Route path="/" element={< HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignUpPage />} />
        <Route path="/browse" element={<Browse />} />
        <Route path="movies/:movieId" element={<Movie />} /> 
        <Route path="categories/:categoryId" element={<Category />} /> 
        <Route path="/Admin" element={<AdminDashboard />} />
        <Route path="/movies/:movieId/play" element={<MoviePlayer/>} />
        <Route path="/search" element={<Search />} /> 
        <Route path="/settings" element={<Settings />} /> 
        <Route path="*" element={<h1>Not Found</h1>} />
      </Routes>
    </Router>
  );
}

export default App;
