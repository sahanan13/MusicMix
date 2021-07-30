package com.codepath.musicmix.connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codepath.musicmix.MusicMixAlgorithmConstants;
import com.codepath.musicmix.VolleyCallBack;
import com.codepath.musicmix.models.Options;
import com.codepath.musicmix.models.Playlist;
import com.codepath.musicmix.models.Song;

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
        if (optionsObject.getOption1().equals(OPTION_HAPPY)) {
            getHappyTracks(() -> {
                Log.d(TAG, "getHappyTracks Success!");
                option2Filter(optionsObject, userId);
            });
        } else if (optionsObject.getOption1().equals(OPTION_ANGRY)) {
            getAngryTracks(() -> {
                Log.d(TAG, "getAngryTracks Success!");
                option2Filter(optionsObject, userId);
            });
        } else if (optionsObject.getOption1().equals(OPTION_SAD)) {
            getSadTracks(() -> {
                Log.d(TAG, "getSadTracks Success!");
                option2Filter(optionsObject, userId);
            });
        } else {                //Nervous
            getNervousTracks(() -> {
                Log.d(TAG, "getNervousTracks Success!");
                option2Filter(optionsObject, userId);
            });
        }

        //createPlaylist(userId);
        //return songs;
        return;
    }

    private void option2Filter(Options optionsObject, String userId) {
        if (optionsObject.getOption2().equals(OPTION_RELAXING)) {
            filterRelaxingTracks(optionsObject, userId);
            Log.d(TAG, "Relax Success!");
            //option3Filter(optionsObject, userId);
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
    }

    private void option3Filter(Options optionsObject, String userId) {
        if (optionsObject.getOption3().equals(OPTION_INSTRUMENTALS)) {
            filterInstrumentalTracks(optionsObject, userId);
            Log.d(TAG, "Instrumental Success!");
            //    option4Filter(optionsObject, userId);
        } else if (optionsObject.getOption3().equals(OPTION_ELECTRONIC)) {
            filterElectronicTracks(optionsObject, userId);
            Log.d(TAG, "Electronic Success!");
        } else {                    //Vocal
            filterVocalTracks(optionsObject, userId);
            Log.d(TAG, "Vocal Success!");
        }
    }

    private void option4Filter(Options optionsObject, String userId) {
        if (optionsObject.getOption4().equals(OPTION_MOTIVATIONAL)) {
            filterMotivationalTracks(optionsObject, userId);
            Log.d(TAG, "Motivational Success!");
            //    option5Filter(optionsObject, userId);
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
    }

    private void option5Filter(Options optionsObject, String userId) {
        filterEnergyTracks(optionsObject, userId);
    }


    public ArrayList<Song> getHappyTracks(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/search?q=happy&type=track&market=US&limit=50&offset=0";
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

    public ArrayList<Song> getAngryTracks(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/search?q=angry&type=track&market=US&limit=50&offset=0";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    //Gson gson = new Gson();
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

    public ArrayList<Song> getSadTracks(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/search?q=sad&type=track&market=US&limit=50&offset=0";
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

    public ArrayList<Song> getNervousTracks(final VolleyCallBack callBack) {
        //String endpoint = "https://api.spotify.com/v1/search?q=happy&type=track&market=US&limit=20";
        String endpoint = "https://api.spotify.com/v1/search?q=nervous&type=track&market=US&limit=50&offset=0";
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

    //filtering through option 2
    private void filterRelaxingTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterRelaxingTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            int finalI = i;
            filterEachRelaxingSong(currentSong, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Relaxing track song success!");
                    if (finalI == songs.size()-1) {
                        songs = selectedSongs;
                        option3Filter(optionsObject, userId);
                    }
                }
            });
        }
        return;
    }

    private void filterEachRelaxingSong(Song currentSong, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/audio-features/" + currentSong.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    double danceability = response.optDouble("danceability");
                    if (danceability < RELAXING_DANCEABILITY) {
                        selectedSongs.add(currentSong);
                        Log.d(TAG, "Relaxing song: " + currentSong.getName());
                    } else {
                        Log.d(TAG, "Not relaxing song: " + currentSong.getName());
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

    private void filterPartyTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterPartyTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            int finalI = i;
            filterEachPartySong(currentSong, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Party track song success!");
                    if (finalI == songs.size()-1) {
                        songs = selectedSongs;
                        option3Filter(optionsObject, userId);
                    }
                }
            });
        }
        return;
    }

    private void filterEachPartySong(Song currentSong, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/audio-features/" + currentSong.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    double danceability = response.optDouble("danceability");
                    if (danceability >= PARTYING_DANCEABILITY) {
                        selectedSongs.add(currentSong);
                    } else {
                        Log.d(TAG, "Not included song: " + currentSong.getName());
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

    private void filterExercisingTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterExercisingTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            int finalI = i;
            filterEachExercisingSong(currentSong, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Exercising track song success!");
                    if (finalI == songs.size()-1) {
                        songs = selectedSongs;
                        option3Filter(optionsObject, userId);
                    }
                }
            });
        }
        return;
    }

    private void filterEachExercisingSong(Song currentSong, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/audio-features/" + currentSong.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    double danceability = response.optDouble("danceability");
                    if (danceability >= EXERCISING_DANCEABILITY_LOW && danceability < EXERCISING_DANCEABILITY_HIGH) {
                        selectedSongs.add(currentSong);
                    } else {
                        Log.d(TAG, "Not included song: " + currentSong.getName());
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

    private void filterWorkingTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterWorkingTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            int finalI = i;
            filterEachWorkingSong(currentSong, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Working track song success!");
                    if (finalI == songs.size()-1) {
                        songs = selectedSongs;
                        option3Filter(optionsObject, userId);
                    }
                }
            });
        }
        return;
    }

    private void filterEachWorkingSong(Song currentSong, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/audio-features/" + currentSong.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    double danceability = response.optDouble("danceability");
                    if (danceability >= WORKING_DANCEABILITY_LOW && danceability < WORKING_DANCEABILITY_HIGH) {
                        selectedSongs.add(currentSong);
                    } else {
                        Log.d(TAG, "Not included song: " + currentSong.getName());
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

    //filtering through option 3
    private void filterInstrumentalTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterInstrumentalTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            int finalI = i;
            filterEachInstrumentalSong(currentSong, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Instrumental track song success!");
                    if (finalI == songs.size()-1) {
                        songs = selectedSongs;
                        option4Filter(optionsObject, userId);
                    }
                }
            });
        }
        return;
    }

    private void filterEachInstrumentalSong(Song currentSong, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/audio-features/" + currentSong.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    double instrumentalness = response.optDouble("instrumentalness");
                    if (instrumentalness >= INSTRUMENTAL_INSTRUMENTALNESS) {
                        selectedSongs.add(currentSong);
                    } else {
                        Log.d(TAG, "Not included song: " + currentSong.getName());
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
    }

    private void filterElectronicTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterElectronicTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            int finalI = i;
            filterEachElectronicSong(currentSong, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Electronic track song success!");
                    if (finalI == songs.size()-1) {
                        songs = selectedSongs;
                        option4Filter(optionsObject, userId);
                    }
                }
            });
        }
        return;
    }

    private void filterEachElectronicSong(Song currentSong, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/audio-features/" + currentSong.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    double acousticness = response.optDouble("acousticness");
                    if (acousticness <= ELECTRONIC_ACOUSTICNESS) {
                        selectedSongs.add(currentSong);
                    } else {
                        Log.d(TAG, "Not included song: " + currentSong.getName());
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

    private void filterVocalTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterVocalTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            int finalI = i;
            filterEachVocalSong(currentSong, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Vocal track song success!");
                    if (finalI == songs.size()-1) {
                        songs = selectedSongs;
                        option4Filter(optionsObject, userId);
                    }
                }
            });
        }
        return;
    }

    private void filterEachVocalSong(Song currentSong, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/audio-features/" + currentSong.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    double instrumentalness = response.optDouble("instrumentalness");
                    if (instrumentalness <= VOCAL_INSTRUMENTALNESS) {
                        selectedSongs.add(currentSong);
                    } else {
                        Log.d(TAG, "Not included song: " + currentSong.getName());
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
    }

    //filtering through option 4
    private void filterMotivationalTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterMotivationalTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            int finalI = i;
            filterEachMotivationalSong(currentSong, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Motivational track song success!");
                    if (finalI == songs.size()-1) {
                        songs = selectedSongs;
                        option5Filter(optionsObject, userId);
                    }
                }
            });
        }
        return;
    }

    private void filterEachMotivationalSong(Song currentSong, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/audio-features/" + currentSong.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    double tempo = response.optDouble("tempo");
                    if (tempo >= MOTIVATIONAL_TEMPO) {
                        selectedSongs.add(currentSong);
                    } else {
                        Log.d(TAG, "Not included song: " + currentSong.getName());
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

    private void filterCalmingTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterCalmingTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            int finalI = i;
            filterEachCalmingSong(currentSong, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Calming track song success!");
                    if (finalI == songs.size()-1) {
                        songs = selectedSongs;
                        option5Filter(optionsObject, userId);
                    }
                }
            });
        }
        return;
    }

    private void filterEachCalmingSong(Song currentSong, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/audio-features/" + currentSong.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    double loudness = response.optDouble("loudness");
                    if (loudness >= CALMING_LOUDNESS) {
                        selectedSongs.add(currentSong);
                    } else {
                        Log.d(TAG, "Not included song: " + currentSong.getName());
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

    private void filterCheerfulTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterCheerfulTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            int finalI = i;
            filterEachCheerfulSong(currentSong, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Cheerful track song success!");
                    if (finalI == songs.size()-1) {
                        songs = selectedSongs;
                        option5Filter(optionsObject, userId);
                    }
                }
            });
        }
        return;
    }

    private void filterEachCheerfulSong(Song currentSong, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/audio-features/" + currentSong.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    double valence = response.optDouble("valence");
                    if (valence >= CHEERFUL_VALENCE) {
                        selectedSongs.add(currentSong);
                    } else {
                        Log.d(TAG, "Not included song: " + currentSong.getName());
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

    private void filterSorrowfulTracks(Options optionsObject, String userId) {
        Log.d(TAG, "in filterSorrowfulTracks");
        selectedSongs = new ArrayList<Song>();
        for (int i = 0; i < songs.size(); i++) {
            Song currentSong = songs.get(i);
            int finalI = i;
            filterEachSorrowfulSong(currentSong, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Sorrowful track song success!");
                    if (finalI == songs.size()-1) {
                        songs = selectedSongs;
                        option5Filter(optionsObject, userId);
                    }
                }
            });
        }
        return;
    }

    private void filterEachSorrowfulSong(Song currentSong, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/audio-features/" + currentSong.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    double valence = response.optDouble("valence");
                    if (valence <= SORROWFUL_VALENCE) {
                        selectedSongs.add(currentSong);
                    } else {
                        Log.d(TAG, "Not included song: " + currentSong.getName());
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
            int finalI = i;
            filterEachEnergySong(currentSong, energyLow, energyHigh, new VolleyCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Energy track song success!");
                    if (finalI == songs.size()-1) {
                        songs = selectedSongs;
                        for (int i = 0; i < songs.size();i++) {
                            Log.d(TAG, "createList: " + (i + ": " + (songs.get(i)).getName()));
                        }
                        createPlaylist(optionsObject, userId);
                    }
                }
            });
        }
        //songs = selectedSongs;
        return;
    }

    private void filterEachEnergySong(Song currentSong, double energyLow, double energyHigh, final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/audio-features/" + currentSong.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    double energy = response.optDouble("energy");
                    if (energy > energyLow && energy <= energyHigh) {
                        selectedSongs.add(currentSong);
                    } else {
                        Log.d(TAG, "Not included song: " + currentSong.getName());
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
                            playlist = new Playlist(response.getString("id"), name, optionsObject);
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
        playlist.addSongs(songs);
        JSONObject object = new JSONObject();

        String uriList = "";
        for(int i = 0; i < songs.size(); i++)
        {
            String songUri = (songs.get(i)).getUri();
            songUri.replace(":", "%3A");
            uriList += songUri + "%2C";
        }

        Log.d(TAG, "Uri list: " +uriList);
        //Url for adding items to playlists
        // "https://api.spotify.com/v1/playlists/3cEYpjA9oz9GiPac4AsH4n/tracks?uris=spotify%3Atrack%3A4iV5W9uYEdYUVa79Axb7Rh%2Cspotify%3Atrack%3A1301WleyT98MSxVHPZCA6M"
        String url = "https://api.spotify.com/v1/playlists/"+playlist.getId()+"/tracks?uris="+uriList;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "String Response : "+ response.toString());
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
