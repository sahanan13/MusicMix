package com.codepath.musicmix;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codepath.musicmix.models.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpotifyRequest {

    public static final String TAG = "SpotifyRequest";

    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Context context;
    private HashMap<String, Boolean> requestStatus = new HashMap<>();
    private ArrayList<Song> songs = new ArrayList<>();

    //            headers.put("Authorization", auth);

    public SpotifyRequest() {}

    public SpotifyRequest(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
        this.context = context;
    }

    private void setRequestStatus(String aRequest, boolean aStatus) {
        requestStatus.put(aRequest, aStatus);
    }

    private boolean isComplete() {
        for (Boolean aValue : requestStatus.values()) {
            if (aValue.equals(false)) {
                return false;
            }
        }
        return true;
    }

    public void getTracks(String[] keywordArray, int index, String offset, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/search?q=" + keywordArray[index] + "&type=track&market=US&limit=50&offset=" + offset;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    JSONObject jsonObject = response.optJSONObject("tracks");
                    JSONArray jsonArray = jsonObject.optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            songs.add(new Song(object.getString("id"), object.getString("name"), object.getString("uri")));
                            Log.d(TAG, songs.get(songs.size()-1).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                    /*if (index == keywordArray.length - 1) {
                        addAudioFeatures(0, 49, new VolleyCallBack() {
                            @Override
                            public void onSuccess() {
                                // do nothing
                            }
                        });
                    } else {
                        getTracks(keywordArray, index + 1, offset, new VolleyCallBack() {
                            @Override
                            public void onSuccess() {
                                //do nothing
                            }
                        });
                    }*/
                }, error -> {
                    Log.i(TAG, "error");

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return;
    }





}
