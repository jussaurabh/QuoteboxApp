package com.example.quotebox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.quotebox.adapters.PostsAdapter;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionPostsActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    GlobalClass globalClass;
    List<String> posts;
    PostsAdapter postsAdapter;
    List<Posts> collPosts = new ArrayList<>();
    HashMap<String, Posts> allPosts = new HashMap<>();

    Toolbar toolbar;
    RecyclerView collectionListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_posts);

        firestore = FirebaseFirestore.getInstance();
        globalClass = (GlobalClass)getApplicationContext();
        allPosts = globalClass.getAllPosts();

        posts = globalClass.getAllUsersData().get(FirebaseAuth.getInstance().getCurrentUser().getUid()).getUserPostCollections().get(getIntent().getStringExtra(Users.COLLECTION_NAME));

        Bundle extras = getIntent().getExtras();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(extras.getString(Users.COLLECTION_NAME));
        toolbar.setSubtitle("Posts (" + extras.getString(Users.POSTS_IN_COLLECTION) + ")");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(CollectionPostsActivity.this, HomeActivity.class));
            }
        });

        collectionListRecyclerView = findViewById(R.id.collectionListRecyclerView);
        collectionListRecyclerView.setHasFixedSize(true);
        collectionListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d("CP", posts.toString());

        for (String p : posts) {
            collPosts.add(allPosts.get(p));
        }

        postsAdapter = new PostsAdapter(CollectionPostsActivity.this, collPosts);
        collectionListRecyclerView.setAdapter(postsAdapter);

    }
}
