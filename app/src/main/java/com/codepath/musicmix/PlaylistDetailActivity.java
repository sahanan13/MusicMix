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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.codepath.musicmix.Adapters.PlaylistsAdapter;
import com.codepath.musicmix.Adapters.SongsAdapter;
import com.codepath.musicmix.connectors.SongService;
import com.codepath.musicmix.models.Like;
import com.codepath.musicmix.models.Options;
import com.codepath.musicmix.models.Playlist;
import com.codepath.musicmix.models.Song;
import com.google.android.material.chip.Chip;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistDetailActivity extends AppCompatActivity implements EditDialog.EditDialogListener {

    private ImageView ivPlaylistImage;
    private TextView tvPlaylistName, tvDescription, tvCreatedBy, tvCreatedAt, tvNumSongsDetails;
    private RecyclerView rvSongs;
    protected SongsAdapter adapter;
    protected List<Song> allSongs;
    private static final String TAG = "PlaylistDetailActivity";
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Button btnLikeDetail, btnEdit, btnRefresh;
    private List<Like> likes;
    private Chip option1Chip, option2Chip, option3Chip, option4Chip, option5Chip;
    private SongService songService;
    String option1, option2, option3, option4, option5;
    private String userId;

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
        btnRefresh = findViewById(R.id.btnRefresh);
        option1Chip = findViewById(R.id.option1Chip);
        option2Chip = findViewById(R.id.option2Chip);
        option3Chip = findViewById(R.id.option3Chip);
        option4Chip = findViewById(R.id.option4Chip);
        option5Chip = findViewById(R.id.option5Chip);

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

        option1 = playlist.getKeyOption1();
        option2 = playlist.getKeyOption2();
        option3 = playlist.getKeyOption3();
        option4 = playlist.getKeyOption4();
        option5 = playlist.getKeyOption5();

        option1Chip.setText(option1);
        option2Chip.setText(option2);
        option3Chip.setText(option3);
        option4Chip.setText(option4);
        option5Chip.setText(option5);

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
                btnLikeDetail.setBackgroundTintList(PlaylistDetailActivity.this.getResources().getColorStateList(R.color.white));

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
                                            btnLikeDetail.setBackgroundTintList(PlaylistDetailActivity.this.getResources().getColorStateList(R.color.white));
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

                // to allow user to edit playlist information
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog(tvPlaylistName.getText().toString(), playlist.getDescription(), playlist);
                    }
                });

                //to allow user to generate new playlist with same options but different songs
                btnRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(PlaylistDetailActivity.this, "Generating new playlist...", Toast.LENGTH_SHORT).show();
                        songService = new SongService(getApplicationContext());

                        userId = sharedPreferences.getString("userid", "none");

                        //creating object for user's answers
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        Options optionsObject = new Options(option1, option2, option3, option4, option5, currentUser);
                        songService.getPlaylistTracks(userId, optionsObject, "50");
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

    public void openDialog(String oldPlaylistName, String oldPlaylistDescription, Playlist playlist) {
        EditDialog editDialog = new EditDialog(oldPlaylistName, oldPlaylistDescription, playlist);
        editDialog.show(getSupportFragmentManager(), "Edit dialog");
    }


    @Override
    public void applyTexts(String newPlaylistName, String newPlaylistDescription, Playlist playlist) {
        //Update Parse
        updateParsePlaylistObject(newPlaylistName, newPlaylistDescription, playlist);

        //Update Spotify
        updatePlaylist(newPlaylistName, newPlaylistDescription, playlist);

        //Update Playlist detail view
        tvPlaylistName.setText(newPlaylistName);
        tvDescription.setText("Description: " + newPlaylistDescription);

    }

    public void updateParsePlaylistObject(String newPlaylistName, String newPlaylistDescription, Playlist playlist) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Playlist");

        // Retrieve the object by id
        query.getInBackground(playlist.getObjectId(), (object, e) -> {
            if (e == null) {
                //Object was successfully retrieved
                // Update the fields we want to
                object.put("description", newPlaylistDescription);
                object.put("playlistName", newPlaylistName);

                //All other fields will remain the same
                object.saveInBackground();

            } else {
                // something went wrong
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // method to update playlist
    public void updatePlaylist(String newPlaylistName, String newPlaylistDescription, Playlist playlist) {
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("name", newPlaylistName);
            object.put("description",newPlaylistDescription);
            object.put("public", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Enter the correct url for your api service site
        String endpoint = "https://api.spotify.com/v1/playlists/" + playlist.getId();
        Log.d(TAG, "Update playlist endpoint: " + endpoint);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, endpoint, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "String Response : "+ response.toString());
                        playlist.setName(newPlaylistName);
                        playlist.setDescription(newPlaylistDescription);
                        Log.d(TAG, "Playlist updated!");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error getting response");
            }
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
    }
}