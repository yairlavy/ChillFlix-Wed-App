package com.example.androidchillflix.main.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.androidchillflix.R;
import com.example.androidchillflix.main.config.Config;

public class ImageGlideHelper {

    public enum ImageType {
        POSTER,
        BACKDROP,
        AVATAR
    }

    public static void loadImage(Context context, String imagePath, ImageType type, ImageView imageView) {
        String imageUrl = "";
        String baseUrl = Config.getBaseUrl();

        switch (type) {
            case POSTER:
                if (imagePath != null && imagePath.startsWith("/420x631/")) {
                    imageUrl = "https://dummyjson.com/image" + imagePath;
                } else {
                    imageUrl = baseUrl + "Assets/movieAssets/" + imagePath;
                }
                break;
            case BACKDROP:
                if (imagePath != null && imagePath.startsWith("/916x515")) {
                    imageUrl = "https://dummyjson.com/image" + imagePath;
                } else {
                    imageUrl = baseUrl + "Assets/movieAssets/" + imagePath;
                }
                break;
            case AVATAR:
                if (imagePath != null) {
                    imageUrl = baseUrl + "Assets/avatars/" + imagePath;
                }
                break;
        }

        int errorDrawable;
        switch (type) {
            case POSTER:
                errorDrawable = R.drawable.movie_poster_default;
                break;
            case BACKDROP:
                errorDrawable = R.drawable.background_image;
                break;
            case AVATAR:
                errorDrawable = R.drawable.avatar3;
                break;
            default:
                errorDrawable = R.drawable.movie_poster_default;
                break;
        }

        Glide.with(context)
                .load(imageUrl)
                .error(errorDrawable)
                .into(imageView);
    }
}
