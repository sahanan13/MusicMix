package com.codepath.musicmix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.codepath.musicmix.Adapters.PlaylistsAdapter;
import com.codepath.musicmix.Adapters.SongsAdapter;
import com.codepath.musicmix.models.Like;
import com.codepath.musicmix.models.Playlist;
import com.codepath.musicmix.models.Song;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistDetailActivity extends AppCompatActivity {

    private ImageView ivPlaylistImage;
    private TextView tvPlaylistName, tvDescription, tvCreatedBy, tvCreatedAt, tvNumSongsDetails;
    private RecyclerView rvSongs;
    protected SongsAdapter adapter;
    protected List<Song> allSongs;
    private static final String TAG = "PlaylistDetailActivity";
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Button btnLikeDetail, btnEdit;
    private List<Like> likes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_detail);
        ivPlaylistImage = findViewById(R.id.ivPlaylistImage);
        tvPlaylistName = findViewById(R.id.tvPlaylistName);
        tvDescription = findViewById(R.id.tvDescription);
        tvCreatedBy = findViewById(R.id.tvCreatedBy);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvNumSongsDetails = findViewById(R.id.tvNumSongsDetails);
        rvSongs = findViewById(R.id.rvSongs);
        btnLikeDetail = findViewById(R.id.btnLikeDetail);
        btnEdit = findViewById(R.id.btnEdit);

        sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);

        Playlist playlist = (Playlist) Parcels.unwrap(getIntent().getParcelableExtra("Playlist"));
        likes = (List<Like>) Parcels.unwrap(getIntent().getParcelableExtra("Likes"));

        tvPlaylistName.setText(playlist.getName());
        tvDescription.setText("Description: " + playlist.getDescription());
        tvCreatedBy.setText("Created by: " + playlist.getUser().getUsername());
        tvCreatedAt.setText("Created at: " + playlist.getCreatedDate());
        tvNumSongsDetails.setText(playlist.getNumSongs() + " songs");
        Glide.with(this).load(playlist.getImageUrl()).into(ivPlaylistImage);

        allSongs = new ArrayList<>();

        // query songs from Spotify
        querySongs(playlist, new VolleyCallBack() {
            @Override
            public void onSuccess() {
                adapter = new SongsAdapter(PlaylistDetailActivity.this, allSongs);

                // set the adapter on the recycler view
                rvSongs.setAdapter(adapter);
                // set the layout manager on the recycler view
                rvSongs.setLayoutManager(new LinearLayoutManager(PlaylistDetailActivity.this));

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PlaylistDetailActivity.this);
                rvSongs.setLayoutManager(linearLayoutManager);

                btnLikeDetail.setTag(R.drawable.ic_ufi_heart);
                btnLikeDetail.setBackground(PlaylistDetailActivity.this.getResources().getDrawable(R.drawable.ic_ufi_heart));
                btnLikeDetail.setBackgroundTintList(PlaylistDetailActivity.this.getResources().getColorStateList(R.color.black));

                for (int i = 0; i < likes.size(); i++) {
                    if ((playlist.getObjectId()).equals(likes.get(i).getPlaylistId())) {
                        btnLikeDetail.setTag(R.drawable.ic_ufi_heart_active);
                        btnLikeDetail.setBackground(PlaylistDetailActivity.this.getResources().getDrawable(R.drawable.ic_ufi_heart_active));
                        btnLikeDetail.setBackgroundTintList(PlaylistDetailActivity.this.getResources().getColorStateList(R.color.red));
                    }
                }

                btnLikeDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((int)btnLikeDetail.getTag() == R.drawable.ic_ufi_heart) {
                            Like newLike = new Like();
                            newLike.setPlaylistId(playlist.getObjectId());
                            newLike.setUserId(ParseUser.getCurrentUser().getObjectId());
                            newLike.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.e("PlaylistsAdapter", "Error while saving", e);
                                        Toast.makeText(PlaylistDetailActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Log.d("PlaylistsAdapter", "Saved successfully!");
                                        btnLikeDetail.setTag(R.drawable.ic_ufi_heart_active);
                                        btnLikeDetail.setBackground(PlaylistDetailActivity.this.getResources().getDrawable(R.drawable.ic_ufi_heart_active));
                                        btnLikeDetail.setBackgroundTintList(PlaylistDetailActivity.this.getResources().getColorStateList(R.color.red));
                                        likes.add(newLike);
                                    }
                                    Log.i("PlaylistsAdapter", "Like save was successful!!");
                                }
                            });
                        } else {
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
                            String likeObjectId = "";
                            int likeIndex = -1;

                            for (int i = 0; i < likes.size(); i++) {
                                if ((playlist.getObjectId()).equals(likes.get(i).getPlaylistId())) {
                                    likeObjectId = likes.get(i).getObjectId();
                                    likeIndex = i;
                                    break;
                                }
                            }

                            // Retrieve the object by id
                            int finalLikeIndex = likeIndex;
                            query.getInBackground(likeObjectId, (object, e3) -> {
                                if (e3 == null) {
                                    //Object was fetched
                                    //Deletes the fetched ParseObject from the database
                                    object.deleteInBackground(e2 -> {
                                        if(e2==null){
                                            Log.d(TAG, "Delete Successful");
                                            btnLikeDetail.setTag(R.drawable.ic_ufi_heart);
                                            btnLikeDetail.setBackground(PlaylistDetailActivity.this.getResources().getDrawable(R.drawable.ic_ufi_heart));
                                            btnLikeDetail.setBackgroundTintList(PlaylistDetailActivity.this.getResources().getColorStateList(R.color.black));
                                            likes.remove(finalLikeIndex);
                                        }else{
                                            //Something went wrong while deleting the Object
                                            Log.e(TAG, e2.getMessage(), e2);
                                        }
                                    });
                                }else{
                                    //Something went wrong
                                    Log.e(TAG, e3.getMessage(), e3);
                                }
                            });
                        }
                    }
                });
            }
        });

    }

    private void querySongs(Playlist playlist, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/playlists/" + playlist.getId() + "/tracks?market=US&limit=100";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            JSONObject jsonTrack = object.optJSONObject("track");
                            JSONObject jsonAlbum = jsonTrack.optJSONObject("album");
                            JSONArray jsonImages = jsonAlbum.optJSONArray("images");
                            String songImageUrl = (jsonImages.optJSONObject(0)).optString("url");
                            JSONArray jsonArtists = jsonTrack.getJSONArray("artists");
                            String artistName = (jsonArtists.optJSONObject(0)).optString("name");
                            String songName = jsonTrack.optString("name");
                            Song newSong = new Song();
                            newSong.setName(songName);
                            newSong.setArtist(artistName);
                            newSong.setSongImageUrl(songImageUrl);
                            allSongs.add(newSong);
                            Log.d(TAG, "Song: " + newSong.getName() + " added!");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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


}