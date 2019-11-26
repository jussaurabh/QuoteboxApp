package com.example.quotebox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.models.Users;

import java.util.HashMap;

public class FollowListActivity extends AppCompatActivity {

    GlobalClass globalClass;
    HashMap<String, Users> allUsers;

    SwipeRefreshLayout followListSwipeRefresh;
    TextView followListDefaultTV;
    RecyclerView followListRecyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);

        globalClass = (GlobalClass)getApplicationContext();
        allUsers = globalClass.getAllUsersData();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Follower");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                startActivity(new Intent(FollowListActivity.this, HomeActivity.class));
                FollowListActivity.super.onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
