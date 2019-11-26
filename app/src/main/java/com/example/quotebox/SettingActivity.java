package com.example.quotebox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.quotebox.ui.SettingFragment;

public class SettingActivity extends AppCompatActivity {

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_settings);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(SettingActivity.this, HomeActivity.class));
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.setting_frag_container, new SettingFragment()).commit();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
    }
}
