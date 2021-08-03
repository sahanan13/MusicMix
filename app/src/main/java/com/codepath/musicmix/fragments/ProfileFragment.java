package com.codepath.musicmix.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.musicmix.Adapters.PlaylistsAdapter;
import com.codepath.musicmix.R;
import com.codepath.musicmix.models.Playlist;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private RecyclerView rvPlaylists;
    protected PlaylistsAdapter adapter;
    protected List<Playlist> allPlaylists;
    TextView tvUsername;
    ImageView ivProfileImage;
    private ParseUser user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = ParseUser.getCurrentUser();
        tvUsername = view.findViewById(R.id.tvUsername);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        rvPlaylists = view.findViewById(R.id.rvProfilePlaylists);
        // initialize the array that will hold posts and create a PostsAdapter
        allPlaylists = new ArrayList<>();
        adapter = new PlaylistsAdapter(getContext(), allPlaylists);

        // set the adapter on the recycler view
        rvPlaylists.setAdapter(adapter);
        // set the layout manager on the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPlaylists.setLayoutManager(linearLayoutManager);
        // query playlists from MusicMix
        queryPlaylists();

        tvUsername.setText(user.getUsername());
    }

    protected void queryPlaylists() {
        // specify what type of data to query - Playlist.class
        ParseQuery<Playlist> query = ParseQuery.getQuery(Playlist.class);
        // include data referred by user key
        query.whereEqualTo(Playlist.KEY_USER, ParseUser.getCurrentUser());
        query.include(Playlist.KEY_USER);
        // limit query to latest 100 items
        query.setLimit(100);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Playlist>() {
            @Override
            public void done(List<Playlist> playlists, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes - print every post description to logcat
                for (Playlist playlist : playlists) {
                    Log.i(TAG, "Playlist: " + playlist.getName() + ", username: " + playlist.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                allPlaylists.addAll(playlists);
                adapter.notifyDataSetChanged();
            }
        });
    }
}