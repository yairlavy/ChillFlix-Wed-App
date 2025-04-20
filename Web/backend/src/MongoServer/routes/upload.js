const express = require('express');
const router = express.Router();
const multer = require('multer');
const uploadController = require('../controllers/upload');
const verify = require('../controllers/verify');
const path = require('path');

// Debug: Log current directory
console.log('Current directory:', __dirname);

const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        let folderPath = path.resolve(__dirname, '../Assets/');

        if (req.originalUrl.includes('/avatars')) {
            folderPath = path.resolve(__dirname, '../Assets/avatars');
        } else if (req.originalUrl.includes('/movieAsset')) {
            folderPath = path.resolve(__dirname, '../Assets/movieAssets');
        }

        console.log("Folder path:", folderPath);


        cb(null, folderPath);
    },
    filename: (req, file, cb) => {
        cb(null, `${Date.now()}-${file.originalname}`);
    }
});

const upload = multer({ storage: storage });


// Define routes

// for user avatars
router.post('/avatars', verify, upload.single('file'), uploadController.uploadFile);

// for movie posters/backdrop/video
router.post('/movieAsset', verify, upload.fields([
    { name: 'poster_path', maxCount: 1 },
    { name: 'backdrop_path', maxCount: 1 },
    { name: 'trailer', maxCount: 1 }
]), uploadController.uploadFile);

module.exports = router;
