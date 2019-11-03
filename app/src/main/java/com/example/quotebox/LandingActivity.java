package com.example.quotebox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class LandingActivity extends AppCompatActivity {

    Button goToSignupActivityBtn, goToLoginActivityBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);


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
