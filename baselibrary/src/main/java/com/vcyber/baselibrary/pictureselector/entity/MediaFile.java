package com.vcyber.baselibrary.pictureselector.entity;

public class MediaFile {
    public String fileName;
    public String filePath;
    public String fileCreatTime;

    public MediaFile() {}

    public MediaFile(String path, String name) {
        this.filePath = path;
        this.fileName = name;
    }
}
