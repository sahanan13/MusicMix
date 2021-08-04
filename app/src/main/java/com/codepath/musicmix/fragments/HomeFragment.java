package com.codepath.musicmix.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.musicmix.Adapters.PlaylistsAdapter;
import com.codepath.musicmix.R;
import com.codepath.musicmix.models.Like;
import com.codepath.musicmix.models.Playlist;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private RecyclerView rvPlaylists;
    protected PlaylistsAdapter adapter;
    protected List<Playlist> allPlaylists;
    protected ArrayList<Like> likes;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPlaylists = (RecyclerView) view.findViewById(R.id.rvPlaylists);
        allPlaylists = new ArrayList<>();
        likes = new ArrayList<>();

        queryLikes();
        adapter = new PlaylistsAdapter(getContext(), allPlaylists, likes);

        // set the adapter on the recycler view
        rvPlaylists.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPlaylists.setLayoutManager(new LinearLayoutManager(getContext()));
        // query posts from MusicMix
        queryPlaylists();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPlaylists.setLayoutManager(linearLayoutManager);

    }

    protected void queryPlaylists() {
        // specify what type of data to query - Playlist.class
        ParseQuery<Playlist> query = ParseQuery.getQuery(Playlist.class);
        // include data referred by user key
        query.include(Playlist.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(20);
        // order playlists by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for playlists
        query.findInBackground(new FindCallback<Playlist>() {
            @Override
            public void done(List<Playlist> playlists, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting playlists", e);
                    return;
                }

                // save received playlists to list and notify adapter of new data
                adapter.clear();
                allPlaylists.addAll(playlists);
                adapter.notifyDataSetChanged();
            }
        });
    }

    protected void queryLikes() {
        // specify what type of data to query - Playlist.class
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.whereEqualTo(Like.KEY_USER_ID, ParseUser.getCurrentUser().getObjectId());
        // start an asynchronous call for playlists
        query.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> likesList, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting likes", e);
                    return;
                }
                Log.d(TAG, "Likes list: " + likesList.toString());
                // save received playlists to list and notify adapter of new data
                adapter.clear();
                likes.addAll(likesList);
                adapter.notifyDataSetChanged();
            }
        });
    }
}