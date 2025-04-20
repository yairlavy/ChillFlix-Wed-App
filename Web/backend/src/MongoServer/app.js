const express = require('express');
var app = express();

const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));

const cors = require('cors');
app.use(cors());

const customEnv = require('custom-env');
customEnv.env(process.env.NODE_ENV, './config');
console.log("CONNECTION_STRING: ", process.env.CONNECTION_STRING);
console.log("PORT: ", process.env.PORT);

const net = require('net');

const mongoose = require('mongoose');
mongoose.connect(process.env.CONNECTION_STRING, {
    //useNewUrlParser: true,
    //useUnifiedTopology: true
}).then(() => {
    console.log('Successfully connected to the database.');
}).catch((error) => {
    console.error('Error connecting to the database', error);
});

app.use(express.static('public'));

app.use(express.json());

const tokenRoutes = require('./routes/tokens');
app.use('/api/tokens', tokenRoutes);

const userRoutes = require('./routes/user');
app.use('/api/users', userRoutes);

const category = require('./routes/category');
app.use('/api/categories', category);

const movie = require('./routes/movie');
app.use('/api/movies', movie);

const upload = require('./routes/upload')
app.use('/api/upload', upload);

const path = require('path');
app.use('/api/Assets', express.static(path.join(__dirname, 'Assets')));


const port = process.env.PORT;

app.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
});