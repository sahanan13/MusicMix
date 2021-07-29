package com.codepath.musicmix.models;

import java.util.ArrayList;

public class Playlist {
    private String id;
    private String name;
    private ArrayList<Song> songs;
    private Options options;

    public Playlist(String id, String name, Options optionsObject) {
        this.id = id;
        this.name = name;
        this.options = optionsObject;
    }

    public void addSongs(ArrayList<Song> newSongs) {
        songs = newSongs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }
}


