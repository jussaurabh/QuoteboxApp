package com.example.quotebox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;

import com.example.quotebox.adapters.FollowRequestsAdapter;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class FollowRequestActivity extends AppCompatActivity {

    GlobalClass globalClass;

    Toolbar toolbar;
    SwipeRefreshLayout followRequestSwipeRefresh;
    RecyclerView followRequestRV;

    FollowRequestsAdapter followRequestsAdapter;
    List<Users> usersRequestsList;
    WriteBatch batch;
    FirebaseFirestore firestore;
    DocumentReference followerDoc, followingDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_request);

        init();

        for (String id : globalClass.getAllUsersData().get(FirebaseAuth.getInstance().getCurrentUser().getUid()).getFollowRequestReceived()) {
            usersRequestsList.add(globalClass.getAllUsersData().get(id));
        }

        followRequestSwipeRefresh.setRefreshing(false);

        followRequestsAdapter = new FollowRequestsAdapter(FollowRequestActivity.this, usersRequestsList);
        followRequestRV.setAdapter(followRequestsAdapter);
    }

    public void init() {
        globalClass = (GlobalClass) getApplicationContext();
        usersRequestsList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        batch = firestore.batch();

        followRequestRV = findViewById(R.id.followRequestRV);
        followRequestSwipeRefresh = findViewById(R.id.followRequestSwipeRefresh);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Follow Requests");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                FollowRequestActivity.super.onBackPressed();
            }
        });

        followRequestRV.setHasFixedSize(true);
        followRequestRV.setLayoutManager(new LinearLayoutManager(FollowRequestActivity.this));
    }

    public void deleteFollowRequest(final String id, final int position) {
        followingDoc = firestore.collection(CollectionNames.USERS).document(id);
        followerDoc = firestore.collection(CollectionNames.USERS).document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        batch.update(followerDoc, Users.FOLLOW_REQUEST_RECEIVED, FieldValue.arrayRemove(id));
        batch.update(followingDoc, Users.FOLLOW_REQUEST_SENT, FieldValue.arrayRemove(FirebaseAuth.getInstance().getCurrentUser().getUid()));

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                usersRequestsList.remove(position);

                globalClass.getAllUsersData().get(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .getFollowRequestReceived().remove(id);

                globalClass.getAllUsersData().get(id).getFollowRequestSent()
                        .remove(FirebaseAuth.getInstance().getCurrentUser().getUid());

                followRequestsAdapter.notifyItemRemoved(position);
            }
        });

    }

    public void confirmFollowRequest(final String id, final int position) {
        followingDoc = firestore.collection(CollectionNames.USERS).document(id);
        followerDoc = firestore.collection(CollectionNames.USERS).document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        batch.update(followerDoc, Users.FOLLOW_REQUEST_RECEIVED, FieldValue.arrayRemove(id));
        batch.update(followingDoc, Users.FOLLOW_REQUEST_SENT, FieldValue.arrayRemove(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        batch.update(followerDoc, Users.FOLLOWER_USERS, FieldValue.arrayUnion(id));
        batch.update(followingDoc, Users.FOLLOWING_USERS, FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getUid()));

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                globalClass.getAllUsersData().get(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .getFollowRequestReceived().remove(id);

                globalClass.getAllUsersData().get(id).getFollowRequestSent()
                        .remove(FirebaseAuth.getInstance().getCurrentUser().getUid());

                globalClass.getAllUsersData().get(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .getFollowerUsers().add(id);

                globalClass.getAllUsersData().get(id)
                        .getFollowingUsers().add(FirebaseAuth.getInstance().getCurrentUser().getUid());

                usersRequestsList.remove(position);

                followRequestsAdapter.notifyItemRemoved(position);
            }
        });
    }

}
