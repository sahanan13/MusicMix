package com.codepath.musicmix.models;

public class Song {
    private String id;
    private String name;
    private String uri;

    public Song(String id, String name, String uri) {
        this.name = name;
        this.id = id;
        this.uri = uri;
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.id = uri;
    }

    public String toString() {
        return "Name: " + name + "| id: " + id;
    }
}
