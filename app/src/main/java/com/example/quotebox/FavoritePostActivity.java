package com.example.quotebox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.quotebox.adapters.PostsAdapter;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.models.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoritePostActivity extends AppCompatActivity {

    FirebaseFirestore firestore;

    PostsAdapter postsAdapter;

    Toolbar toolbar;
    ProgressBar favPostActProgressBar;
    RecyclerView userFavPostsRV;
    LinearLayout userFavPostDefaultLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_post);

        firestore = FirebaseFirestore.getInstance();


        favPostActProgressBar = findViewById(R.id.favPostActProgressBar);
        userFavPostsRV = findViewById(R.id.userFavPostsRV);
        userFavPostDefaultLL = findViewById(R.id.userFavPostDefaultLL);
        toolbar = findViewById(R.id.toolbar);


        toolbar.setTitle("Favorites");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(FavoritePostActivity.this, HomeActivity.class));
            }
        });


        userFavPostsRV.setHasFixedSize(true);
        userFavPostsRV.setLayoutManager(new LinearLayoutManager(this));


        firestore.collection(new CollectionNames().getPostCollection())
                .whereArrayContains(Posts.POST_LIKES, FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                userFavPostsRV.setVisibility(View.VISIBLE);
                                favPostActProgressBar.setVisibility(View.GONE);

                                List<Posts> postsList = new ArrayList<>();
                                for (QueryDocumentSnapshot docs : task.getResult()) {
                                    Posts post = new Posts();
                                    post.setPostId(docs.getId());
                                    post.setPost(docs.getString(Posts.POST));
                                    post.setPostImage(docs.getString(Posts.POST_IMAGE));
                                    post.setPostType(docs.getString(Posts.POST_TYPE));
                                    post.setPostTitle(docs.getString(Posts.POST_TITLE));
                                    post.setUserId(docs.getString(Posts.USER_ID));
                                    post.setPostUser(docs.getString(Posts.POST_USER));
                                    post.setPostLikes((List<String>) docs.get(Posts.POST_LIKES));
                                    post.setPostComments(Integer.parseInt(docs.get(Posts.POST_COMMENTS).toString()));
                                    post.setPostTimestamp(docs.getTimestamp(Posts.POST_TIMESTAMP));

                                    postsList.add(post);
                                }

                                postsAdapter = new PostsAdapter(FavoritePostActivity.this, postsList);
                                userFavPostsRV.setAdapter(postsAdapter);
                            }
                            else {
                                userFavPostDefaultLL.setVisibility(View.VISIBLE);
                                favPostActProgressBar.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }
}
