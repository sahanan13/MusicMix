package com.codepath.musicmix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;

    private RequestQueue queue;

    private static final String CLIENT_ID = "d05768ed70704c4997b6097f446682c8";
    private static final String REDIRECT_URI = "com.codepath.musicmix://callback";
    private static final int REQUEST_CODE = 1337;
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private";

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateSpotify();

                msharedPreferences = LoginActivity.this.getSharedPreferences("SPOTIFY", 0);
                queue = Volley.newRequestQueue(LoginActivity.this);
            }
        });
        /*authenticateSpotify();

        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);*/
    }

    private void authenticateSpotify() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{SCOPES});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    // Receiving the token if request was successful
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("STARTING", "GOT AUTH TOKEN");
                    editor.apply();
                    //waitForUserInfo();
                    Log.i("LoginActivity", "login success");
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    Log.e("LoginActivity", "Login failed");
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

}