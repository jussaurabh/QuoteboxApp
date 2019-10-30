package com.example.quotebox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.google.firebase.auth.FirebaseAuth;


public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    TextView splashScreenTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();

        splashScreenTag = findViewById(R.id.splashScreenTag);

        Animation fadeInAnime = AnimationUtils.loadAnimation(this, R.anim.fade_in_transition);

        splashScreenTag.startAnimation(fadeInAnime);

        String msg = (firebaseAuth.getCurrentUser() != null) ? "true" : "false";
        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_LONG).show();


        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();

                    if (firebaseAuth.getCurrentUser() != null) {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    }
                    else {
                        startActivity(new Intent(SplashActivity.this, LandingActivity.class));
                    }
                }
            }
        };

        timer.start();
    }
}
