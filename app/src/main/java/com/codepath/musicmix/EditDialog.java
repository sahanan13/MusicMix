package com.codepath.musicmix;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.codepath.musicmix.models.Playlist;

import org.jetbrains.annotations.NotNull;

public class EditDialog extends AppCompatDialogFragment {

    private EditText etPlaylistTitle;
    private EditText etPlaylistDescription;
    private EditDialogListener listener;
    private String oldPlaylistName, oldPlaylistDescription;
    private Playlist playlist;

    public EditDialog (String oldPlaylistName, String oldPlaylistDescription, Playlist playlist) {
        this.oldPlaylistName = oldPlaylistName;
        this.oldPlaylistDescription = oldPlaylistDescription;
        this.playlist = playlist;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_edit, null);

        builder.setView(view).setTitle("Edit Playlist").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing if cancel clicked
            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPlaylistName = etPlaylistTitle.getText().toString();
                String newPlaylistDescription = etPlaylistDescription.getText().toString();
                listener.applyTexts(newPlaylistName, newPlaylistDescription, playlist);
            }
        });

        etPlaylistTitle = view.findViewById(R.id.etPlaylistTitle);
        etPlaylistDescription = view.findViewById(R.id.etPlaylistDescription);
        etPlaylistTitle.setText(oldPlaylistName);
        etPlaylistDescription.setText(oldPlaylistDescription);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (EditDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement EditDialog");
        }
    }


    public interface EditDialogListener {
        void applyTexts(String newPlaylistName, String newPlaylistDescription, Playlist playlist);
    }
}
