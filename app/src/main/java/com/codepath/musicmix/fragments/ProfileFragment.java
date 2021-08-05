package com.codepath.musicmix.fragments;

import android.content.SharedPreferences;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.widget.PullRefreshLayout;
import com.bumptech.glide.Glide;
import com.codepath.musicmix.Adapters.PlaylistsAdapter;
import com.codepath.musicmix.MainActivity;
import com.codepath.musicmix.R;
import com.codepath.musicmix.VolleyCallBack;
import com.codepath.musicmix.models.Like;
import com.codepath.musicmix.models.Playlist;
import com.codepath.musicmix.models.Song;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private RecyclerView rvPlaylists;
    protected PlaylistsAdapter adapter;
    protected List<Playlist> allPlaylists;
    protected ArrayList<Like> likes;
    TextView tvUsername;
    ImageView ivProfileImage;
    private ParseUser user;
    private String userId;
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private String profileImageUrl;
    private PullRefreshLayout pullRefreshLayout;

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

        sharedPreferences = getContext().getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(getContext());
        //getting user id
        userId = sharedPreferences.getString("userid", "none");

        user = ParseUser.getCurrentUser();
        tvUsername = view.findViewById(R.id.tvUsername);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        rvPlaylists = view.findViewById(R.id.rvProfilePlaylists);

        getProfileImage(new VolleyCallBack() {
            @Override
            public void onSuccess() {
                // initialize the array that will hold posts and create a PostsAdapter
                allPlaylists = new ArrayList<>();
                likes = new ArrayList<>();

                queryLikes();
                adapter = new PlaylistsAdapter(getContext(), allPlaylists, likes);

                // set the adapter on the recycler view
                rvPlaylists.setAdapter(adapter);
                // set the layout manager on the recycler view
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                rvPlaylists.setLayoutManager(linearLayoutManager);
                // query playlists from MusicMix
                queryPlaylists();
                Glide.with(getActivity()).load(profileImageUrl).circleCrop().into(ivProfileImage);
                tvUsername.setText(user.getUsername());

                // Lookup the swipe container view
                pullRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.pullRefreshLayout);

                pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        pullRefreshLayout.setRefreshing(true);
                        pullRefreshLayout.setVisibility(View.VISIBLE);
                        Log.d(TAG, "onRefresh!");
                        queryPlaylists();
                    }
                });

                // refresh complete
                Log.d(TAG, "refresh complete!");
                pullRefreshLayout.setRefreshing(false);

            }
        });
    }

    protected void queryPlaylists() {
        // specify what type of data to query - Playlist.class
        ParseQuery<Playlist> query = ParseQuery.getQuery(Playlist.class);
        // include data referred by user key
        query.whereEqualTo(Playlist.KEY_USER, ParseUser.getCurrentUser());
        query.include(Playlist.KEY_USER);
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
                // end refreshing
                pullRefreshLayout.setRefreshing(false);
                Log.d(TAG, "done refreshing in queryPlaylist");
            }
        });
    }

    private void getProfileImage(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    JSONArray jsonArray = response.optJSONArray("images");
                    profileImageUrl = (jsonArray.optJSONObject(0)).optString("url");
                    callBack.onSuccess();
                }, error -> {
                    Log.i(TAG, "error");

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return;
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