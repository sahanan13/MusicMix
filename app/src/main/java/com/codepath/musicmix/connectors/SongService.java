package com.codepath.musicmix.connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codepath.musicmix.MainActivity;
import com.codepath.musicmix.MusicMixAlgorithmConstants;
import com.codepath.musicmix.VolleyCallBack;
import com.codepath.musicmix.fragments.QuestionnaireFragment;
import com.codepath.musicmix.models.Options;
import com.codepath.musicmix.models.Playlist;
import com.codepath.musicmix.models.Song;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SongService implements MusicMixAlgorithmConstants {

    public static final String TAG = "SongService";

    private ArrayList<Song> songs = new ArrayList<>();
    private ArrayList<Song> selectedSongs = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Context context;
    private Playlist playlist;
    private String userId;
    private Options optionsObject;

    public SongService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
        this.context = context;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }


    public void getPlaylistTracks(String userId, Options optionsObject, String offset) {
        this.userId = userId;
        this.optionsObject = optionsObject;
        String[] keywordArray = optionsObject.getOptions1Keywords();

        int index = 0;
        // used stacked onSuccess methods because not possible to query for multiple words at the same time in Spotify Web API
        getTracks(keywordArray, index, offset, new VolleyCallBack() {
            @Override
            public void onSuccess() {
                getTracks(keywordArray, 2, offset, new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        getTracks(keywordArray, 3, offset, new VolleyCallBack() {
                            @Override
                            public void onSuccess() {
                                getTracks(keywordArray, 4, offset, new VolleyCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        getTracks(keywordArray, 5, offset, new VolleyCallBack() {
                                            @Override
                                            public void onSuccess() {
                                                getTracks(keywordArray, 6, offset, new VolleyCallBack() {
                                                    @Override
                                                    public void onSuccess() {
                                                        getTracks(keywordArray, 7, offset, new VolleyCallBack() {
                                                            @Override
                                                            public void onSuccess() {
                                                                getTracks(keywordArray, 8, offset, new VolleyCallBack() {
                                                                    @Override
                                                                    public void onSuccess() {
                                                                        getTracks(keywordArray, 9, offset, new VolleyCallBack() {
                                                                            @Override
                                                                            public void onSuccess() {
                                                                                getTracks(keywordArray, 10, offset, new VolleyCallBack() {
                                                                                    @Override
                                                                                    public void onSuccess() {
                                                                                        addAllAudioFeatures();
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });

            }
        });
        return;
    }

    //getting audio features for all tracks
    private void addAllAudioFeatures() {
        addAudioFeatures(0, 49, new VolleyCallBack() {
            @Override
            public void onSuccess() {
                 addAudioFeatures(50, 99, new VolleyCallBack() {
                     @Override
                     public void onSuccess() {
                         addAudioFeatures(100, 149, new VolleyCallBack() {
                             @Override
                             public void onSuccess() {
                                 addAudioFeatures(150, 199, new VolleyCallBack() {
                                     @Override
                                     public void onSuccess() {
                                         addAudioFeatures(200, 249, new VolleyCallBack() {
                                             @Override
                                             public void onSuccess() {
                                                 addAudioFeatures(250, 299, new VolleyCallBack() {
                                                     @Override
                                                     public void onSuccess() {
                                                         addAudioFeatures(300, 349, new VolleyCallBack() {
                                                             @Override
                                                             public void onSuccess() {
                                                                 addAudioFeatures(350, 399, new VolleyCallBack() {
                                                                     @Override
                                                                     public void onSuccess() {
                                                                         addAudioFeatures(400, 449, new VolleyCallBack() {
                                                                             @Override
                                                                             public void onSuccess() {
                                                                                 addAudioFeatures(450, 499, new VolleyCallBack() {
                                                                                     @Override
                                                                                     public void onSuccess() {
                                                                                         filterTracks();
                                                                                     }
                                                                                 });
                                                                             }
                                                                         });
                                                                     }
                                                                 });
                                                             }
                                                         });
                                                     }
                                                 });
                                             }
                                         });
                                     }
                                 });
                             }
                         });
                     }
                 });
            }
        });

    }

    private void filterTracks() {
        option2Filter();
        option3Filter();
        option4Filter();
        option5Filter();
        createPlaylist();
    }

    private void option2Filter() {
        String option2 = optionsObject.getOption2();
        switch(option2) {
            case OPTION_RELAXING:
                filterRelaxingTracks();
                Log.d(TAG, "Relax Success!");
                break;
            case OPTION_PARTY:
                filterPartyTracks();
                Log.d(TAG, "Party success!");
                break;
            case OPTION_EXERCISING:
                filterExercisingTracks();
                Log.d(TAG, "Exercising Success!");
                break;
            default:    // Working
                filterWorkingTracks();
                Log.d(TAG, "Working Success!");
        }
    }

    private void option3Filter() {
        String option3 = optionsObject.getOption3();
        switch(option3) {
            case OPTION_INSTRUMENTALS:
                filterInstrumentalTracks();
                Log.d(TAG, "Instrumental Success!");
                break;
            case OPTION_ELECTRONIC:
                filterElectronicTracks();
                Log.d(TAG, "Electronic Success!");
                break;
            default:    // Vocal
                filterVocalTracks();
                Log.d(TAG, "Vocal Success!");
        }
    }

    private void option4Filter() {
        String option4 = optionsObject.getOption4();
        switch(option4) {
            case OPTION_MOTIVATIONAL:
                filterMotivationalTracks();
                Log.d(TAG, "Motivational Success!");
                break;
            case OPTION_CALMING:
                filterCalmingTracks();
                Log.d(TAG, "Calming Success!");
                break;
            case OPTION_CHEERFUL:
                filterCheerfulTracks();
                Log.d(TAG, "Cheerful Success!");
                break;
            default:    // Sorrowful
                filterSorrowfulTracks();
                Log.d(TAG, "Sorrowful Success!");
        }
    }

    private void option5Filter() {
        filterEnergyTracks();
    }


    public ArrayList<Song> getTracks(String[] keywordArray, int index, String offset, final VolleyCallBack callBack) {
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
        return songs;
    }

    private void addAudioFeatures(int indexStart, int indexEnd, final VolleyCallBack callBack) {
        String idList = "";
        for(int i = indexStart; i <= indexEnd; i++)
        {
            String songId = (songs.get(i)).getId();
            idList += songId;
            if (i != indexEnd) {
                idList +=  "%2C";
            }
        }

        Log.d(TAG, "Song id list: " +idList);
        String endpoint = "https://api.spotify.com/v1/audio-features?ids=" + idList;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    JSONArray jsonArray = response.optJSONArray("audio_features");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            Log.d(TAG, "Song array size audioFeatures: " + songs.size());
                            Song currentSong = songs.get(n+indexStart);
                            currentSong.setDanceability(object.optDouble("danceability"));
                            currentSong.setInstrumentalness(object.optDouble("instrumentalness"));
                            currentSong.setAcousticness(object.optDouble("acousticness"));
                            currentSong.setTempo(object.optDouble("tempo"));
                            currentSong.setLoudness(object.optDouble("loudness"));
                            currentSong.setValence(object.optDouble("valence"));
                            currentSong.setEnergy(object.optDouble("energy"));
                            Log.d(TAG, "currentSong" + currentSong);
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

    //filtering through option 2
    private void filterRelaxingTracks() {
        Log.d(TAG, "in filterRelaxingTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            if (currentSong.isRelaxing()) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not relaxing song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterPartyTracks() {
        Log.d(TAG, "in filterPartyTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            if (currentSong.isParty()) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterExercisingTracks() {
        Log.d(TAG, "in filterExercisingTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            if (currentSong.isExercising()) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterWorkingTracks() {
        Log.d(TAG, "in filterWorkingTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            if (currentSong.isWorking()) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }


    //filtering through option 3
    private void filterInstrumentalTracks() {
        Log.d(TAG, "in filterInstrumentalTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            if (currentSong.isInstrumental()) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterElectronicTracks() {
        Log.d(TAG, "in filterElectronicTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            if (currentSong.isElectronic()) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterVocalTracks() {
        Log.d(TAG, "in filterVocalTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            if (currentSong.isVocal()) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    //filtering through option 4
    private void filterMotivationalTracks() {
        Log.d(TAG, "in filterMotivationalTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            if (currentSong.isMotivational()) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterCalmingTracks() {
        Log.d(TAG, "in filterCalmingTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            if (currentSong.isCalming()) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterCheerfulTracks() {
        Log.d(TAG, "in filterCheerfulTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            if (currentSong.isCheerful()) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterSorrowfulTracks() {
        Log.d(TAG, "in filterSorrowfulTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            if (currentSong.isSorrowful()) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }


    //filtering through option 5
    private void filterEnergyTracks() {
        double energyLow;
        double energyHigh;
        if (optionsObject.isScale_1()) {
            energyLow = ENERGY_0;
            energyHigh = ENERGY_1;
        } else if (optionsObject.isScale_2()) {
            energyLow = ENERGY_1;
            energyHigh = ENERGY_2;
        } else if (optionsObject.isScale_3()) {
            energyLow = ENERGY_2;
            energyHigh = ENERGY_3;
        } else if (optionsObject.isScale_4()) {
            energyLow = ENERGY_3;
            energyHigh = ENERGY_4;
        } else {   //option5 is "5"
            energyLow = ENERGY_4;
            energyHigh = ENERGY_5;
        }

        Log.d(TAG, "in filterEnergyTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            if (currentSong.isInEnergyLevels(energyLow, energyHigh)) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }


    // method to create empty playlist
    public void createPlaylist() {
        JSONObject object = new JSONObject();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String name = dtf.format(now);
        Log.d(TAG, "Playlist name: " + name);
        try {
            //input your API parameters
            object.put("name", name);
            object.put("description","None");
            object.put("public", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Enter the correct url for your api service site
        String url = "https://api.spotify.com/v1/users/"+ userId + "/playlists";
        Log.d(TAG, "URL: " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "String Response : "+ response.toString());
                        try {
                            playlist = new Playlist();
                            playlist.setId(response.getString("id"));
                            playlist.setName(name);
                            playlist.setDescription("None");
                            playlist.setCreatedDate(name);
                            playlist.setOptions(optionsObject);
                            playlist.setUser(optionsObject.getCurrentUser());
                            addSongsToPlaylist();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public void addSongsToPlaylist() {
        playlist.setSongs(songs);
        JSONObject object = new JSONObject();

        String uriList = "";
        for(int i = 0; i < songs.size(); i++)
        {
            String songUri = (songs.get(i)).getUri();
            songUri.replace(":", "%3A");
            uriList += songUri + "%2C";
        }

        Log.d(TAG, "Uri list: " +uriList);
        String url = "https://api.spotify.com/v1/playlists/"+playlist.getId()+"/tracks?uris="+uriList;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "String Response : "+ response.toString());
                        playlist.setNumSongs(String.valueOf(songs.size()));
                        playlist.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(TAG, "Error while saving", e);
                                    Toast.makeText(context, "Error while saving!", Toast.LENGTH_SHORT).show();
                                }
                                Log.i(TAG, "Playlist save was successful!!");
                                MainActivity.questionnaireFragment.goHomeFragment();
                            }
                        });
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
