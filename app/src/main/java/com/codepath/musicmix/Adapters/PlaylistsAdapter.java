package com.codepath.musicmix.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.codepath.musicmix.MainActivity;
import com.codepath.musicmix.PlaylistDetailActivity;
import com.codepath.musicmix.R;
import com.codepath.musicmix.VolleyCallBack;
import com.codepath.musicmix.models.Like;
import com.codepath.musicmix.models.Playlist;
import com.codepath.musicmix.models.Song;
import com.parse.GetCallback;
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
import org.w3c.dom.Text;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistsAdapter.ViewHolder> {
    private Context context;
    private List<Playlist> playlists;
    private List<Like> likes;
    private final String TAG = "PlaylistsAdapter";
    private String playlistUrl;
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;

    public PlaylistsAdapter(Context context, List<Playlist> playlists, List<Like> likes) {
        this.context = context;
        this.playlists = playlists;
        this.likes = likes;
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.bind(playlist);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        playlists.clear();
        notifyDataSetChanged();
    }

    // Add a list of posts
    public void addAll(List<Playlist> list) {
        playlists.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPlaylistTitle;
        private TextView tvUsername;
        private TextView tvNumSongs;
        private ImageView ivPlaylistimg;
        private Button btnLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlaylistTitle = itemView.findViewById(R.id.tvPlaylistTitle);
            tvNumSongs = itemView.findViewById(R.id.tvNumSongs);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivPlaylistimg = itemView.findViewById(R.id.ivPlaylistimg);
            btnLike = itemView.findViewById(R.id.btnLike);
        }

        public void bind(Playlist playlist) {
            // Bind the playlist data to the view elements
            tvPlaylistTitle.setText(playlist.getName());
            tvNumSongs.setText(playlist.getNumSongs() + " songs");
            tvUsername.setText("Made by: " + playlist.getUser().getUsername());

            getPlaylistImage(playlist, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    //change playlist image
                    updatePlaylistImage(playlist);
                    Glide.with(context).load(playlist.getImageUrl()).into(ivPlaylistimg);
                    btnLike.setTag(R.drawable.ic_ufi_heart);
                    btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_ufi_heart));
                    btnLike.setBackgroundTintList(context.getResources().getColorStateList(R.color.white));

                    for (int i = 0; i < likes.size(); i++) {
                        if ((playlist.getObjectId()).equals(likes.get(i).getPlaylistId())) {
                            btnLike.setTag(R.drawable.ic_ufi_heart_active);
                            btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_ufi_heart_active));
                            btnLike.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
                        }
                    }

                    itemView.setOnTouchListener(new View.OnTouchListener() {
                        GestureDetector gestureDetector = new GestureDetector(context.getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                            @Override
                            public boolean onDoubleTap(MotionEvent e) {
                                if ((int)btnLike.getTag() == R.drawable.ic_ufi_heart) {
                                    Like newLike = new Like();
                                    newLike.setPlaylistId(playlist.getObjectId());
                                    newLike.setUserId(ParseUser.getCurrentUser().getObjectId());
                                    newLike.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e != null) {
                                                Log.e("PlaylistsAdapter", "Error while saving", e);
                                                Toast.makeText(context, "Error while saving!", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Log.d("PlaylistsAdapter", "Saved successfully!");
                                                btnLike.setTag(R.drawable.ic_ufi_heart_active);
                                                btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_ufi_heart_active));
                                                btnLike.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
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
                                                    btnLike.setTag(R.drawable.ic_ufi_heart);
                                                    btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_ufi_heart));
                                                    btnLike.setBackgroundTintList(context.getResources().getColorStateList(R.color.white));
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
                                return super.onDoubleTap(e);
                            }

                            @Override
                            public boolean onSingleTapConfirmed(MotionEvent e) {
                                Log.d(TAG, "playlist clicked!");
                                Intent i = new Intent(context, PlaylistDetailActivity.class);
                                i.putExtra("Playlist", Parcels.wrap(playlist));
                                i.putExtra("Likes", Parcels.wrap(likes));
                                context.startActivity(i);
                                return super.onSingleTapConfirmed(e);
                            }
                        });

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            gestureDetector.onTouchEvent(event);
                            return true;
                        }
                    });

                    btnLike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ((int)btnLike.getTag() == R.drawable.ic_ufi_heart) {
                                Like newLike = new Like();
                                newLike.setPlaylistId(playlist.getObjectId());
                                newLike.setUserId(ParseUser.getCurrentUser().getObjectId());
                                newLike.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            Log.e("PlaylistsAdapter", "Error while saving", e);
                                            Toast.makeText(context, "Error while saving!", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Log.d("PlaylistsAdapter", "Saved successfully!");
                                            btnLike.setTag(R.drawable.ic_ufi_heart_active);
                                            btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_ufi_heart_active));
                                            btnLike.setBackgroundTintList(context.getResources().getColorStateList(R.color.red));
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
                                                btnLike.setTag(R.drawable.ic_ufi_heart);
                                                btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_ufi_heart));
                                                btnLike.setBackgroundTintList(context.getResources().getColorStateList(R.color.white));
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

        public void getPlaylistImage(Playlist playlist, final VolleyCallBack callBack) {
            String endpoint = "https://api.spotify.com/v1/playlists/" + playlist.getId();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, endpoint, null, response -> {
                        JSONArray jsonArray = response.optJSONArray("images");
                        playlistUrl = (jsonArray.optJSONObject(0)).optString("url");
                        playlist.setImageUrl(playlistUrl);
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

        public void updatePlaylistImage(Playlist playlist) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Playlist");

            // Retrieve the object by id
            query.getInBackground(playlist.getObjectId(), (object, e) -> {
                if (e == null) {
                    //Object was successfully retrieved
                    // Update the fields we want to
                    object.put("imageUrl", playlist.getImageUrl());

                    //All other fields will remain the same
                    object.saveInBackground();

                } else {
                    // something went wrong
                    Log.e(TAG, e.getMessage(), e);
                }
            });

        }


    }

}
