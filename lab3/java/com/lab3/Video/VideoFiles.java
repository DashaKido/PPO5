package com.lab3.Video;

public class VideoFiles {
    private String id;
    private String path;
    private final String title;


    public VideoFiles(String id, String path,
                      String title) {
        this.id = id;
        this.path = path;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

}
