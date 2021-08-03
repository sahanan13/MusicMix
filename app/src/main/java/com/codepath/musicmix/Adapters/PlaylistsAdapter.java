package com.codepath.musicmix.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.text.Layout;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.musicmix.R;
import com.codepath.musicmix.models.Playlist;
import com.parse.ParseFile;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistsAdapter.ViewHolder> {
    private Context context;
    private List<Playlist> playlists;

    public PlaylistsAdapter(Context context, List<Playlist> playlists) {
        this.context = context;
        this.playlists = playlists;
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
        boolean isLiked = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlaylistTitle = itemView.findViewById(R.id.tvPlaylistTitle);
            tvNumSongs = itemView.findViewById(R.id.tvNumSongs);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivPlaylistimg = itemView.findViewById(R.id.ivPlaylistimg);
            btnLike = itemView.findViewById(R.id.btnLike);
        }

        public void bind(Playlist playlist) {
            // Bind the post data to the view elements
            tvPlaylistTitle.setText(playlist.getName());
            tvNumSongs.setText(playlist.getNumSongs() + " songs");
            tvUsername.setText("Made by: " + playlist.getUser().getUsername());
            ParseFile image = playlist.getImage();
            Glide.with(context).load(image.getUrl()).into(ivPlaylistimg);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, PostDetailActivity.class);
                    i.putExtra("Post", Parcels.wrap(post));
                    context.startActivity(i);
                }
            });*/

            itemView.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(context.getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        Toast.makeText(context.getApplicationContext(), "Double tap", Toast.LENGTH_SHORT).show();
                        if (isLiked) {
                            btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_ufi_heart));
                            isLiked = false;
                        } else {
                            btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_ufi_heart_active));
                            isLiked = true;
                        }
                        return super.onDoubleTap(e);
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
                    if (isLiked) {
                        btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_ufi_heart));
                        isLiked = false;
                    } else {
                        btnLike.setBackground(context.getResources().getDrawable(R.drawable.ic_ufi_heart_active));
                        isLiked = true;
                    }
                }
            });

        }
    }

}