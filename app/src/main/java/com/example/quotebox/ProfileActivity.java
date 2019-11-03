package com.example.quotebox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.models.Users;
import com.example.quotebox.ui.ProfileFragment;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    SharedPreferencesConfig preferencesConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportFragmentManager().beginTransaction().replace(R.id.profileActivityFL, new ProfileFragment()).commit();

        preferencesConfig = new SharedPreferencesConfig(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(preferencesConfig.getAllUserCreds().get(getIntent().getStringExtra(Users.USER_ID)).getUsername());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        });
    }
}
