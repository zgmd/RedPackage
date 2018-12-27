package com.vcyber.baselibrary.pictureselector.entity;

import android.provider.MediaStore;

import java.util.List;

public class Folder {

    public String name;
    public String path;
    //文件夹显示的一个媒体img
    public MediaFile cover;
    public List<MediaFile> images;

    public Folder() {

    }

    public Folder(String name, String path, MediaFile cover, List<MediaFile> images) {
        this.name = name;
        this.path = path;
        this.cover = cover;
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        try {
            Folder other = (Folder) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }

}
