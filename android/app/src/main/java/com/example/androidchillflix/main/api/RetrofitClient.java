package com.example.androidchillflix.main.api;

import android.content.Context;
import com.example.androidchillflix.main.config.Config;
import com.example.androidchillflix.main.utils.TokenManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getInstance(Context context) {
        if (retrofit == null) {
            TokenManager tokenManager = new TokenManager(context);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest.newBuilder();

                        // Attach the token if available
                        String token = tokenManager.getToken();
                        if (token != null) {
                            builder.addHeader("Authorization", "Bearer " + token);
                        }

                        Request modifiedRequest = builder.build();
                        return chain.proceed(modifiedRequest);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.getBaseUrl()) // Base API URL
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create()) // Convert JSON responses
                    .build();
        }
        return retrofit;
    }

    public static userApi getUserApiService(Context context) {
        return getInstance(context).create(userApi.class);
    }

    public static moviesApi getMoviesApiService(Context context) {
        return getInstance(context).create(moviesApi.class);
    }

    public static categoriesApi getCategoriesApiService(Context context) {
        return getInstance(context).create(categoriesApi.class);
    }
    public static UploadApi getUploadApiService(Context context) {
        return getInstance(context).create(UploadApi.class);
    }
}
