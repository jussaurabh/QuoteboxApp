package com.example.quotebox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quotebox.helpers.SharedPreferencesConfig;


public class LandingActivity extends AppCompatActivity {

    private SharedPreferencesConfig preferencesConfig;

    Button goToSignupActivityBtn, goToLoginActivityBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());

        goToSignupActivityBtn = findViewById(R.id.goToSignupActivityBtn);
        goToLoginActivityBtn = findViewById(R.id.goToLoginActivityBtn);


        goToLoginActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandingActivity.this, LoginActivity.class));
            }
        });

        goToSignupActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LandingActivity.this, SignupActivity.class));
            }
        });
    }

}
