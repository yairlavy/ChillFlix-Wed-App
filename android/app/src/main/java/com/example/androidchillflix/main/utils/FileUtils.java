package com.example.androidchillflix.main.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class FileUtils {

    public static File getFileFromUri(Context context, Uri uri) {
        File file = null;
        try {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            String fileName = cursor.getString(nameIndex);
            cursor.close();

            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            file = new File(context.getCacheDir(), fileName);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            Log.e("error", Objects.requireNonNull(e.getMessage()));
        }
        return file;
    }

}
