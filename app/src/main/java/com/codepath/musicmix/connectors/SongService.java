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

    private ArrayList<Song> songs = new ArrayList<>();
    ArrayList<Song> selectedSongs = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    public static final String TAG = "SongService";
    private Context context;
    private Playlist playlist;

    public SongService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
        this.context = context;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void getPlaylistTracks(String userId, Options optionsObject) {
        String keyword1, keyword2, keyword3, keyword4, keyword5, keyword6, keyword7, keyword8, keyword9, keyword10;
        String userChoice;
        if (optionsObject.getOption1().equals(OPTION_HAPPY)) {
            userChoice = "Happy";
            keyword1 = "happy";
            keyword2 = "sunshine";
            keyword3 = "good";
            keyword4 = "fun";
            keyword5 = "danc";
            keyword6 = "sun";
            keyword7 = "dream";
            keyword8 = "beautiful";
            keyword9 = "live";
            keyword10 = "excited";
        } else if (optionsObject.getOption1().equals(OPTION_ANGRY)) {
            userChoice = "Angry";
            keyword1 = "break";
            keyword2 = "hate";
            keyword3 = "hell";
            keyword4 = "angry";
            keyword5 = "never";
            keyword6 = "kill";
            keyword7 = "anger";
            keyword8 = "misery";
            keyword9 = "revenge";
            keyword10 = "mad";
        } else if (optionsObject.getOption1().equals(OPTION_SAD)) {
            userChoice = "Sad";
            keyword1 = "sad";
            keyword2 = "hurt";
            keyword3 = "loved";
            keyword4 = "wreck";
            keyword5 = "goodbye";
            keyword6 = "heart";
            keyword7 = "cry";
            keyword8 = "tear";
            keyword9 = "burn";
            keyword10 = "broken";
        } else {                //Nervous
            userChoice = "Nervous";
            keyword1 = "nervous";
            keyword2 = "save";
            keyword3 = "fake";
            keyword4 = "breath";
            keyword5 = "lonely";
            keyword6 = "lost";
            keyword7 = "calm%20down";
            keyword8 = "stress";
            keyword9 = "numb";
            keyword10 = "mind";
        }

        // getting tracks for keywords
        // used stacked onSuccess methods because not possible to query for multiple words at the same time in Spotify Web API
        getTracks(keyword1, new VolleyCallBack() {
            @Override
            public void onSuccess() {
                getTracks(keyword2, new VolleyCallBack() {
                    @Override
                    public void onSuccess() {
                        getTracks(keyword3, new VolleyCallBack() {
                            @Override
                            public void onSuccess() {
                                getTracks(keyword4, new VolleyCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        getTracks(keyword5, new VolleyCallBack() {
                                            @Override
                                            public void onSuccess() {
                                                getTracks(keyword6, new VolleyCallBack() {
                                                    @Override
                                                    public void onSuccess() {
                                                        getTracks(keyword7, new VolleyCallBack() {
                                                            @Override
                                                            public void onSuccess() {
                                                                getTracks(keyword8, new VolleyCallBack() {
                                                                    @Override
                                                                    public void onSuccess() {
                                                                        getTracks(keyword9, new VolleyCallBack() {
                                                                            @Override
                                                                            public void onSuccess() {
                                                                                getTracks(keyword10, new VolleyCallBack() {
                                                                                    @Override
                                                                                    public void onSuccess() {
                                                                                        Log.d(TAG, userChoice + " all getTracks Success!");
                                                                                        addAllAudioFeatures(optionsObject, userId);
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
    private void addAllAudioFeatures(Options optionsObject, String userId) {
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
                                                                                         option2Filter(optionsObject, userId);
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

    private void option2Filter(Options optionsObject, String userId) {
        if (optionsObject.getOption2().equals(OPTION_RELAXING)) {
            filterRelaxingTracks(optionsObject, userId);
            Log.d(TAG, "Relax Success!");
        } else if (optionsObject.getOption2().equals(OPTION_PARTY)) {
            filterPartyTracks(optionsObject, userId);
            Log.d(TAG, "Party success!");
        } else if (optionsObject.getOption2().equals(OPTION_EXERCISING)) {
            filterExercisingTracks(optionsObject, userId);
            Log.d(TAG, "Exercising Success!");
        } else {                    //Working
            filterWorkingTracks(optionsObject, userId);
            Log.d(TAG, "Working Success!");
        }
        option3Filter(optionsObject, userId);
    }

    private void option3Filter(Options optionsObject, String userId) {
        if (optionsObject.getOption3().equals(OPTION_INSTRUMENTALS)) {
            filterInstrumentalTracks(optionsObject, userId);
            Log.d(TAG, "Instrumental Success!");
        } else if (optionsObject.getOption3().equals(OPTION_ELECTRONIC)) {
            filterElectronicTracks(optionsObject, userId);
            Log.d(TAG, "Electronic Success!");
        } else {                    //Vocal
            filterVocalTracks(optionsObject, userId);
            Log.d(TAG, "Vocal Success!");
        }
        option4Filter(optionsObject, userId);
    }

    private void option4Filter(Options optionsObject, String userId) {
        if (optionsObject.getOption4().equals(OPTION_MOTIVATIONAL)) {
            filterMotivationalTracks(optionsObject, userId);
            Log.d(TAG, "Motivational Success!");
        } else if (optionsObject.getOption4().equals(OPTION_CALMING)) {
            filterCalmingTracks(optionsObject, userId);
            Log.d(TAG, "Calming Success!");
        } else if (optionsObject.getOption4().equals(OPTION_CHEERFUL)) {
            filterCheerfulTracks(optionsObject, userId);
            Log.d(TAG, "Cheerful Success!");
        } else {                    //Sorrowful
            filterSorrowfulTracks(optionsObject, userId);
            Log.d(TAG, "Sorrowful Success!");
        }
        option5Filter(optionsObject, userId);
    }

    private void option5Filter(Options optionsObject, String userId) {
        filterEnergyTracks(optionsObject, userId);
        createPlaylist(optionsObject, userId);
    }


    public ArrayList<Song> getTracks(String keyword, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/search?q=" + keyword + "&type=track&market=US&limit=50&offset=0";
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
                            double danceability = object.optDouble("danceability");
                            double instrumentalness = object.optDouble("instrumentalness");
                            double acousticness = object.optDouble("acousticness");
                            double tempo = object.optDouble("tempo");
                            double loudness = object.optDouble("loudness");
                            double valence = object.optDouble("valence");
                            double energy = object.optDouble("energy");
                            Log.d(TAG, "Song array size audioFeatures: " + songs.size());
                            Song currentSong = songs.get(n+indexStart);
                            currentSong.setDanceability(danceability);
                            currentSong.setInstrumentalness(instrumentalness);
                            currentSong.setAcousticness(acousticness);
                            currentSong.setTempo(tempo);
                            currentSong.setLoudness(loudness);
                            currentSong.setValence(valence);
                            currentSong.setEnergy(energy);
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
    private void filterRelaxingTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterRelaxingTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            double danceability = currentSong.getDanceability();
            if (danceability < RELAXING_DANCEABILITY) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not relaxing song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterPartyTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterPartyTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            double danceability = currentSong.getDanceability();
            if (danceability >= PARTYING_DANCEABILITY) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterExercisingTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterExercisingTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            double danceability = currentSong.getDanceability();
            if (danceability >= EXERCISING_DANCEABILITY_LOW && danceability < EXERCISING_DANCEABILITY_HIGH) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterWorkingTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterWorkingTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            double danceability = currentSong.getDanceability();
            if (danceability >= WORKING_DANCEABILITY_LOW && danceability < WORKING_DANCEABILITY_HIGH) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }


    //filtering through option 3
    private void filterInstrumentalTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterInstrumentalTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            double instrumentalness = currentSong.getInstrumentalness();
            if (instrumentalness >= INSTRUMENTAL_INSTRUMENTALNESS) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterElectronicTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterElectronicTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            double acousticness = currentSong.getAcousticness();
            if (acousticness <= ELECTRONIC_ACOUSTICNESS) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterVocalTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterVocalTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            double instrumentalness = currentSong.getInstrumentalness();
            if (instrumentalness <= VOCAL_INSTRUMENTALNESS) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    //filtering through option 4
    private void filterMotivationalTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterMotivationalTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            double tempo = currentSong.getTempo();
            if (tempo >= MOTIVATIONAL_TEMPO) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterCalmingTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterCalmingTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            double loudness = currentSong.getLoudness();
            if (loudness >= CALMING_LOUDNESS) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterCheerfulTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterCheerfulTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            double valence = currentSong.getValence();
            if (valence >= CHEERFUL_VALENCE) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }

    private void filterSorrowfulTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterSorrowfulTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            double valence = currentSong.getValence();
            if (valence <= SORROWFUL_VALENCE) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }


    //filtering through option 5
    private void filterEnergyTracks(Options optionsObject, String userId) {
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
            double energy = currentSong.getEnergy();
            if (energy > energyLow && energy <= energyHigh) {
                selectedSongs.add(currentSong);
            } else {
                Log.d(TAG, "Not included song: " + currentSong.getName());
            }
        }
        songs = selectedSongs;
        return;
    }


    // method to create empty playlist
    public void createPlaylist(Options optionsObject, String userId) {
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
