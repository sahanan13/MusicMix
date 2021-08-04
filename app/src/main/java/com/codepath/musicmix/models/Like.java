package com.codepath.musicmix.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel(analyze=Like.class)
@ParseClassName("Like")
public class Like extends ParseObject {
    public static final String KEY_USER_ID = "userLiked";
    public static final String KEY_PLAYLIST_ID = "playlistId";

    public String getUserId() {
        return getString(KEY_USER_ID);
    }

    public void setUserId(String id) {
        put(KEY_USER_ID, id);
    }

    public String getPlaylistId() {
        return getString(KEY_PLAYLIST_ID);
    }

    public void setPlaylistId(String id) {
        put(KEY_PLAYLIST_ID, id);
    }
}
