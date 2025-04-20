const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Movie = new Schema({
    short_id: { type: Number, unique: true, min: 1 },
    title: { type: String, required: true, unique: true },
    releaseYear: { type: String },
    overview: { type: String },
    age_Rating: {
        age: { type: String }, // Example: "18+", "PG-13", etc.
        description: [{ type: String }] // Example: "Death", etc.
    },
    actors: { type: Array },
    creators: { type: Array },
    genres: { type: Array },
    poster_path: { type: String, default: (doc) => `/420x631/008080/FFFFFF?text=${doc.title}` },
    backdrop_path: { type: String, default: (doc) => `/916x515/FF5733/FFFFFF?text=${doc.title}` },
    trailer: { type: String },
    isSeries: { type: Boolean, default: false },
    runtime: { type: String }
}, 
{ timestamps: true });

module.exports = mongoose.model('Movie', Movie);