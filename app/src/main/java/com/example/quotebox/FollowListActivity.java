package com.example.quotebox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.quotebox.adapters.FollowListAdapter;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FollowListActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    GlobalClass globalClass;
    HashMap<String, Users> allUsers;
    FollowListAdapter followListAdapter;
    String INTENT_USER_ID;
    Bundle extras;
    List<Users> followListUsers;

    SwipeRefreshLayout followListSwipeRefresh;
    TextView followListDefaultTV;
    RecyclerView followListRecyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        globalClass = (GlobalClass)getApplicationContext();
        allUsers = globalClass.getAllUsersData();
        INTENT_USER_ID = getIntent().getStringExtra(Users.USER_ID);
        extras = getIntent().getExtras();
        followListUsers = new ArrayList<>();


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(extras.getString("follow").equals(Users.FOLLOWER_USERS) ? "Followers" : "Followings");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                FollowListActivity.super.onBackPressed();
            }
        });

        followListDefaultTV = findViewById(R.id.followListDefaultTV);
        followListSwipeRefresh = findViewById(R.id.followListSwipeRefresh);
        followListRecyclerView = findViewById(R.id.followListRecyclerView);
        followListRecyclerView.setHasFixedSize(true);
        followListRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        for (String ids : extras.getString("follow").equals(Users.FOLLOWER_USERS) ? allUsers.get(INTENT_USER_ID).getFollowerUsers() : allUsers.get(INTENT_USER_ID).getFollowingUsers()) {
            Users user = new Users();
            user._setUserId(ids);
            user.setUserAvatar(allUsers.get(ids).getUserAvatar());
            user.setUsername(allUsers.get(ids).getUsername());
            user.setFollowerUsers(allUsers.get(ids).getFollowerUsers().size() > 0 ? allUsers.get(ids).getFollowerUsers() : new ArrayList<String>());
            user.setFollowingUsers(allUsers.get(ids).getFollowingUsers().size() > 0 ? allUsers.get(ids).getFollowingUsers() : new ArrayList<String>());

            followListUsers.add(user);
        }


        Log.d("FOLLOW_ACT", new Gson().toJson(followListUsers));


        if (followListUsers.size() > 0) {
            followListAdapter = new FollowListAdapter(FollowListActivity.this, followListUsers, firebaseUser.getUid());
            followListRecyclerView.setAdapter(followListAdapter);

            followListDefaultTV.setVisibility(View.GONE);
            followListRecyclerView.setVisibility(View.VISIBLE);
        }




    }

}
