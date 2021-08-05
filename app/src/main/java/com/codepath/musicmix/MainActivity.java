package com.codepath.musicmix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.musicmix.fragments.HomeFragment;
import com.codepath.musicmix.fragments.ProfileFragment;
import com.codepath.musicmix.fragments.QuestionnaireFragment;
import com.codepath.musicmix.models.Song;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    final FragmentManager fragmentManager = getSupportFragmentManager();

    public static HomeFragment homeFragment;
    public static QuestionnaireFragment questionnaireFragment;
    public static ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        homeFragment = new HomeFragment();
        questionnaireFragment = new QuestionnaireFragment();
        profileFragment = new ProfileFragment();
        final int[] enterAnim = new int[1];
        final int[] exitAnim = new int[1];
        final int[] popEnterAnim = new int[1];
        final int[] popExitAnim = new int[1];

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment fragment;
                int currentFragmentId = bottomNavigationView.getSelectedItemId();
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Toast.makeText(MainActivity.this, "Home!", Toast.LENGTH_SHORT).show();
                        fragment = homeFragment;
                        // slide to left
                        enterAnim[0] = R.anim.from_left;
                        exitAnim[0] = R.anim.to_right;
                        popEnterAnim[0] = R.anim.from_right;
                        popExitAnim[0] = R.anim.to_left;
                        break;
                    case R.id.action_questionnaire:
                        Toast.makeText(MainActivity.this, "Questionnaire!", Toast.LENGTH_SHORT).show();
                        fragment = questionnaireFragment;
                        if (currentFragmentId == R.id.action_profile) {
                            // slide to left
                            enterAnim[0] = R.anim.from_left;
                            exitAnim[0] = R.anim.to_right;
                            popEnterAnim[0] = R.anim.from_right;
                            popExitAnim[0] = R.anim.to_left;
                        } else if (currentFragmentId == R.id.action_home) {
                            // slide to right
                            enterAnim[0] = R.anim.from_right;
                            exitAnim[0] = R.anim.to_left;
                            popEnterAnim[0] = R.anim.from_left;
                            popExitAnim[0] = R.anim.to_right;
                        }
                        break;
                    case R.id.action_profile:
                        // slide to right
                        enterAnim[0] = R.anim.from_right;
                        exitAnim[0] = R.anim.to_left;
                        popEnterAnim[0] = R.anim.from_left;
                        popExitAnim[0] = R.anim.to_right;
                    default:
                        Toast.makeText(MainActivity.this, "Profile!", Toast.LENGTH_SHORT).show();
                        fragment = profileFragment;
                        break;
                }
                fragmentManager.beginTransaction().setCustomAnimations(enterAnim[0], exitAnim[0], popEnterAnim[0], popExitAnim[0])
                        .replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnLogout) {
            // forget who's logged in
            ParseUser.logOut();
            ParseUser currentUser = ParseUser.getCurrentUser();

            // navigate backwards to Login screen
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // same as above
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void goHome() {
        fragmentManager.beginTransaction().replace(R.id.flContainer, new HomeFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

}

