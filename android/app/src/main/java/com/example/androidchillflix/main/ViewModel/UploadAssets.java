package com.example.androidchillflix.main.ViewModel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.androidchillflix.main.api.RetrofitClient;
import com.example.androidchillflix.main.models.UploadResponse;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadAssets extends AndroidViewModel {

    private static final String TAG = "UploadAssetsViewModel";

    public UploadAssets(@NonNull Application application) {
        super(application);
    }

    public interface UploadAssetsCallback {
        void onUploadSuccess(UploadResponse response);
        void onUploadFailure(String errorMessage);
    }

    // Helper method to determine MIME type based on file extension
    private String getMimeType(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".mp4")) {
            return "video/mp4";
        } else {
            return "application/octet-stream";
        }
    }

    //Uploads the given asset files (poster, backdrop, trailer) and calls the callback.
    public void uploadAssets(File posterFile, File backdropFile, File trailerFile, final UploadAssetsCallback callback) {
        if (posterFile == null && backdropFile == null && trailerFile == null) {
            if (callback != null) {
                callback.onUploadFailure("Please select at least one asset file");
            }
            return;
        }

        Log.d(TAG, "Starting upload of movie assets...");
        if (posterFile != null) {
            Log.d(TAG, "Poster file: " + posterFile.getName() + ", MIME: " + getMimeType(posterFile) + ", size: " + posterFile.length());
        }
        if (backdropFile != null) {
            Log.d(TAG, "Backdrop file: " + backdropFile.getName() + ", MIME: " + getMimeType(backdropFile) + ", size: " + backdropFile.length());
        }
        if (trailerFile != null) {
            Log.d(TAG, "Trailer file: " + trailerFile.getName() + ", MIME: " + getMimeType(trailerFile) + ", size: " + trailerFile.length());
        }

        MultipartBody.Part posterPart = null;
        MultipartBody.Part backdropPart = null;
        MultipartBody.Part trailerPart = null;

        if (posterFile != null) {
            RequestBody posterRequest = RequestBody.create(MediaType.parse(getMimeType(posterFile)), posterFile);
            posterPart = MultipartBody.Part.createFormData("poster_path", posterFile.getName(), posterRequest);
        }
        if (backdropFile != null) {
            RequestBody backdropRequest = RequestBody.create(MediaType.parse(getMimeType(backdropFile)), backdropFile);
            backdropPart = MultipartBody.Part.createFormData("backdrop_path", backdropFile.getName(), backdropRequest);
        }
        if (trailerFile != null) {
            RequestBody trailerRequest = RequestBody.create(MediaType.parse(getMimeType(trailerFile)), trailerFile);
            trailerPart = MultipartBody.Part.createFormData("trailer", trailerFile.getName(), trailerRequest);
        }

        // Call the upload endpoint with the parts
        RetrofitClient.getUploadApiService(getApplication())
                .uploadMovieAssets(posterPart, backdropPart, trailerPart)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<UploadResponse> call, @NonNull Response<UploadResponse> response) {
                        Log.d(TAG, "Upload response code: " + response.code() + ", message: " + response.message());
                        if (!response.isSuccessful()) {
                            try {
                                if (response.errorBody() != null) {
                                    String error = response.errorBody().string();
                                    Log.e(TAG, "Error body: " + error);
                                    if (callback != null) {
                                        callback.onUploadFailure("Upload failed: " + error);
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error reading error body: " + e.getMessage());
                                if (callback != null) {
                                    callback.onUploadFailure("Upload failed: " + e.getMessage());
                                }
                            }
                            return;
                        }
                        if (response.body() != null) {
                            Log.d(TAG, "Upload successful, response: " + response.body());
                            if (callback != null) {
                                callback.onUploadSuccess(response.body());
                            }
                        } else {
                            Log.e(TAG, "Upload response body is null");
                            if (callback != null) {
                                callback.onUploadFailure("Upload response body is null");
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UploadResponse> call, @NonNull Throwable t) {
                        Log.e(TAG, "Upload error: " + t.getMessage());
                        if (callback != null) {
                            callback.onUploadFailure("Upload error: " + t.getMessage());
                        }
                    }
                });
    }
}
