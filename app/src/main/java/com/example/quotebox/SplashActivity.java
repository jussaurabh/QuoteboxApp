package com.example.quotebox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotebox.helpers.SharedPreferencesConfig;


public class SplashActivity extends AppCompatActivity {


    private SharedPreferencesConfig preferencesConfig;

    TextView splashScreenTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());

        splashScreenTag = findViewById(R.id.splashScreenTag);

        Animation fadeInAnime = AnimationUtils.loadAnimation(this, R.anim.fade_in_transition);

        splashScreenTag.startAnimation(fadeInAnime);

        String msg = "login status " + preferencesConfig.getLoginStatus();
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

                    if (preferencesConfig.getLoginStatus()) {
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
