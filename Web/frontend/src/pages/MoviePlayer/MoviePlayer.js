import React, { useRef, useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import axios from '../../utils/api'; 
import "./MoviePlayer.css";

const MoviePlayer = () => {
  const location = useLocation();
  const { trailer, title, subtitlesUrl, movieId } = location.state || {}; // Get the passed movie data


  const videoRef = useRef(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [currentTime, setCurrentTime] = useState(0);
  const [duration, setDuration] = useState(0);
  const [playbackRate, setPlaybackRate] = useState(1);
  const [isLocked, setIsLocked] = useState(false);
  const [isMinimized, setIsMinimized] = useState(false);
  const [isMobile, setIsMobile] = useState(window.innerWidth <= 768); // Detect mobile screen

  // Use the provided video or fall back to the default video.mp4
  const videoSource = trailer && trailer !== '' 
  ? `${axios.defaults.baseURL}/Assets/movieAssets/${trailer}` 
  : "/video.mp4";

  // Handle screen resize
  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth <= 768); // Adjust for mobile
    };

    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);
  
  // Handle video progress and time updates
  useEffect(() => {
    if (videoRef.current) {
      const video = videoRef.current;
      const updateTime = () => {
        setCurrentTime(video.currentTime);
        setDuration(video.duration);
      };

      video.addEventListener("timeupdate", updateTime);

      return () => video.removeEventListener("timeupdate", updateTime);
    }
  }, []);

  const handlePlayPause = () => {
    if (!videoRef.current) return;
    if (isPlaying) {
      videoRef.current.pause();
    } else {
      videoRef.current.play();
    }
    setIsPlaying(!isPlaying);
  };

  const handleBackward = () => {
    if (videoRef.current) {
      videoRef.current.currentTime = Math.max(videoRef.current.currentTime - 10, 0);
      setCurrentTime(videoRef.current.currentTime);
    }
  };

  const handleForward = () => {
    if (videoRef.current) {
      videoRef.current.currentTime = Math.min(videoRef.current.currentTime + 10, duration);
      setCurrentTime(videoRef.current.currentTime);
    }
  };

  const handlePlaybackRate = (e) => {
    const rate = parseFloat(e.target.value);
    if (videoRef.current) {
      videoRef.current.playbackRate = rate;
    }
    setPlaybackRate(rate);
  };

  const handleFullscreen = () => {
    if (document.fullscreenElement) {
      document.exitFullscreen();
    } else if (videoRef.current) {
      videoRef.current.requestFullscreen();
    }
  };

  const handleLock = () => {
    setIsLocked(!isLocked);
  };

  const handleMinimize = () => {
    setIsMinimized(!isMinimized);
  };

  const formatTime = (time) => {
    const minutes = Math.floor(time / 60);
    const seconds = Math.floor(time % 60);
    return `${minutes}:${seconds < 10 ? `0${seconds}` : seconds}`;
  };

  // Navigate back to the movie page 
  const handleExit = () => {
    window.history.back(null, '', `/movies/${movieId}`);
  };

  return (
    <div
      className={`movie-player-container ${
        isMinimized ? "minimized" : isMobile ? "mobile" : "default"
      }`}
    >
      <video
        ref={videoRef}
        className="movie-player-video"
        src={videoSource}
        controls={false}
        onClick={handlePlayPause}
      >
        {subtitlesUrl && (
          <track
            src={subtitlesUrl}
            kind="subtitles"
            srcLang="en"
            label="English"
            default
          />
        )}
      </video>

      {/* Top Control Bar */}
      <div className="movie-player-top-controls">
        <button className="top-control-button" onClick={handleLock}>
          {isLocked ? "🔒 Lock" : "🔓 Unlock"}
        </button>
        <div className="title-display">{title}</div>
        <button className="top-control-button" onClick={handleExit}>
          Exit
        </button>
      </div>

      {/* Bottom Control Bar */}
      <div className={`movie-player-controls ${isLocked ? "locked" : ""}`}>
        <button className="control-button" onClick={handlePlayPause} disabled={isLocked}>
          {isPlaying ? "⏸️" : "▶️"}
        </button>
        <button className="control-button" onClick={handleBackward} disabled={isLocked}>
          -10
        </button>
        <button className="control-button" onClick={handleForward} disabled={isLocked}>
          +10
        </button>
        <input
          type="range"
          className="progress-bar"
          value={(currentTime / duration) * 100 || 0}
          onChange={(e) => {
            const time = (e.target.value / 100) * duration;
            if (videoRef.current) videoRef.current.currentTime = time;
            setCurrentTime(time);
          }}
          disabled={isLocked}
        />
        <span className="time-display">
          {formatTime(currentTime)} / {formatTime(duration)}
        </span>
        <select
          className="playback-rate"
          value={playbackRate}
          onChange={handlePlaybackRate}
          disabled={isLocked}
        >
          <option value="0.25">0.25x</option>
          <option value="0.5">0.5x</option>
          <option value="0.75">0.75x</option>
          <option value="1">1x</option>
          <option value="1.25">1.25x</option>
          <option value="1.5">1.5x</option>
          <option value="1.75">1.75x</option>
          <option value="2">2x</option>
        </select>
        <button
          className="control-button"
          onClick={() => console.log("Subtitles clicked!")}
        >
          🄾
        </button>
        <button className="control-button" onClick={handleMinimize}>
          {isMinimized ? "⬆️ Maximize" : "⬇️ Minimize"}
        </button>
        <button className="control-button" onClick={handleFullscreen}>
          ⛶
        </button>
      </div>
    </div>
  );
};

export default MoviePlayer;
