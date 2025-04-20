package com.example.androidchillflix.main.models;

import java.util.Map;

public class UploadResponse {

    private String storageName;
    private Map<String, String> storageNames;
    public String getStorageName() {
        return storageName;
    }
    public Map<String, String> getStorageNames() {
        return storageNames;
    }
}
