const uploadFile = async (req, res) => {
    try {
        if (req.originalUrl.includes('/avatars')) {
            if (!req.file) {
                return res.status(400).json({ message: 'No file uploaded for avatar' });
            }

            const allowedTypes = ['image/png', 'image/jpeg', 'image/jpg'];
            if (!allowedTypes.includes(req.file.mimetype)) {
                return res.status(400).json({
                    message: 'Invalid file type for avatar. Only PNG, JPG files are allowed.'
                });
            }

            const fileName = req.file.filename;

            return res.status(200).json({
                message: 'Avatar uploaded successfully',
                storageName: fileName
            });
        } else if (req.originalUrl.includes('/movieAsset')) {
            if (!req.files) {
                return res.status(400).json({ message: 'No files uploaded' });
            }

            console.log('Uploaded files:', req.files);

            const allowedTypes = ['image/png', 'image/jpeg', 'image/jpg', 'video/mp4'];
            const fileNames = {};

            if (req.files.poster_path) {
                if (!allowedTypes.includes(req.files.poster_path[0].mimetype)) {
                    return res.status(400).json({
                        message: 'Invalid file type for poster_path. Only PNG, JPG, and MP4 files are allowed.'
                    });
                }
                fileNames.poster_path = req.files.poster_path[0].filename;
            }
            if (req.files.backdrop_path) {
                if (!allowedTypes.includes(req.files.backdrop_path[0].mimetype)) {
                    return res.status(400).json({
                        message: 'Invalid file type for backdrop_path. Only PNG, JPG, and MP4 files are allowed.'
                    });
                }
                fileNames.backdrop_path = req.files.backdrop_path[0].filename;
            }
            if (req.files.trailer) {
                if (!allowedTypes.includes(req.files.trailer[0].mimetype)) {
                    return res.status(400).json({
                        message: 'Invalid file type for trailer. Only PNG, JPG, and MP4 files are allowed.'
                    });
                }
                fileNames.trailer = req.files.trailer[0].filename;
            }

            if (Object.keys(fileNames).length === 0) {
                return res.status(400).json({ message: 'No valid files uploaded' });
            }

            return res.status(200).json({
                message: 'Movie assets uploaded successfully',
                storageNames: fileNames
            });
        }
    } catch (error) {
        console.error('Error in uploadFile:', error);
        return res.status(500).json({ message: 'Error uploading file', error: error.message });
    }
};

module.exports = { uploadFile };
