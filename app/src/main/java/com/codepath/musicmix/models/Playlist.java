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
    private ArrayList<Song> songs;
    private Options options;
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_PLAYLIST_NAME = "playlistName";
    public static final String KEY_NUM_SONGS = "numSongs";
    public static final String KEY_PLAYLIST_ID = "playlistId";
    public static final String KEY_IMAGE_URL = "imageUrl";
    public static final String KEY_OPTION1 = "option1";
    public static final String KEY_OPTION2 = "option2";
    public static final String KEY_OPTION3 = "option3";
    public static final String KEY_OPTION4 = "option4";
    public static final String KEY_OPTION5 = "option5";

    public String getId() {
        return getString(KEY_PLAYLIST_ID);
    }

    public void setId(String id) {
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

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getImageUrl() {
        return getString(KEY_IMAGE_URL);
    }

    public void setImageUrl(String imageUrl) {
        put(KEY_IMAGE_URL, imageUrl);
    }

    public String getKeyOption1() {
        return getString(KEY_OPTION1);
    }

    public void setKeyOption1(String newOption1) {
        put(KEY_OPTION1, newOption1);
    }

    public String getKeyOption2() {
        return getString(KEY_OPTION2);
    }

    public void setKeyOption2(String newOption2) {
        put(KEY_OPTION2, newOption2);
    }

    public String getKeyOption3() {
        return getString(KEY_OPTION3);
    }

    public void setKeyOption3(String newOption3) {
        put(KEY_OPTION3, newOption3);
    }

    public String getKeyOption4() { return getString(KEY_OPTION4); }

    public void setKeyOption4(String newOption4) {
        put(KEY_OPTION4, newOption4);
    }

    public String getKeyOption5() {
        return getString(KEY_OPTION5);
    }

    public void setKeyOption5(String newOption5) {
        put(KEY_OPTION5, newOption5);
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
        put(KEY_OPTION1, options.getOption1());
        put(KEY_OPTION2, options.getOption2());
        put(KEY_OPTION3, options.getOption3());
        put(KEY_OPTION4, options.getOption4());
        put(KEY_OPTION5, options.getOption5());
    }

}


