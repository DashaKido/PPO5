package com.lab3;

public class MusicFiles {
    private String path;
    private String title;
    private String artist;
    private String duration;

    public MusicFiles(String path, String title, String artist, String duration) {
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }

    public MusicFiles() {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
