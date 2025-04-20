package com.example.androidchillflix.main.models;

import androidx.annotation.NonNull;

// Category.java
public class Category {
    private String _id;
    private boolean promoted;
    private String name;

    public String getId() { return _id; }
    public void setId(String _id) { this._id = _id; }

    public boolean isPromoted() { return promoted; }
    public void setPromoted(boolean promoted) { this.promoted = promoted; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @NonNull
    @Override
    public String toString() {
        return "Category{" +
                "_id='" + _id + '\'' +
                ", promoted=" + promoted +
                ", name='" + name + '\'' +
                '}';
    }
}

