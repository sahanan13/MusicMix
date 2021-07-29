package com.codepath.musicmix.models;

import java.util.ArrayList;

public class Playlist {
    private String id;
    private String name;
    private ArrayList<Song> songs;

    public Playlist(String id, String name) {
        this.id = id;
        this.name = name;
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
}


