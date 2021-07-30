package com.codepath.musicmix.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel(analyze=Playlist.class)
@ParseClassName("Playlist")
public class Playlist extends ParseObject {
    //private String id;
    //private String name;
    private ArrayList<Song> songs;
    private Options options;
    //public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_PLAYLIST_NAME = "playlistName";
    public static final String KEY_NUM_SONGS = "numSongs";
    public static final String KEY_PLAYLIST_ID = "playlistId";

    //public Playlist() {} //empty constructor

    /*public Playlist(String id, String name, Options optionsObject) {
        //this.id = id;
        //this.name = name;
        this.options = optionsObject;

        put(KEY_PLAYLIST_ID, id);
        put(KEY_PLAYLIST_NAME, name);
    }*/

    public String getId() {
        return getString(KEY_PLAYLIST_ID);
    }

    public void setId(String id) {
        //this.id = id;
        put(KEY_PLAYLIST_ID, id);
    }

    public String getName() {
        return getString(KEY_PLAYLIST_NAME);
    }

    public void setName(String name) {
        put(KEY_PLAYLIST_NAME, name);
    }


    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getNumSongs() {
        return getString(KEY_NUM_SONGS);
    }

    public void setNumSongs(String numSongs) {
        put(KEY_NUM_SONGS, numSongs);
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


