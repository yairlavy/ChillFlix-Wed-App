package com.example.androidchillflix.main.api;

import com.example.androidchillflix.main.models.UploadResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApi {

    @Multipart
    @POST("upload/avatars")
    Call<UploadResponse> uploadAvatar(@Part MultipartBody.Part file);

    @Multipart
    @POST("upload/movieAsset")
    Call<UploadResponse> uploadMovieAssets(
            @Part MultipartBody.Part poster_path,
            @Part MultipartBody.Part backdrop_path,
            @Part MultipartBody.Part trailer
    );
}
