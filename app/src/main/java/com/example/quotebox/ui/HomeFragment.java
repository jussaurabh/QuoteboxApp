package com.example.quotebox.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.R;
import com.example.quotebox.adapters.PostsAdapter;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private FirebaseFirestore firestore;
    private List<Posts> postsList;

    private PostsAdapter postsAdapter;
    private RecyclerView homeRecyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firestore = FirebaseFirestore.getInstance();

        postsList = new ArrayList<>();

        firestore.collection(new CollectionNames().getPostCollection())
                .orderBy(Posts.POST_TIMESTAMP, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            if (task.getResult().size() > 0) {
                                for(QueryDocumentSnapshot docs : task.getResult()) {
                                    Posts posts = new Posts();
                                    posts.setPostImage(docs.getString(Posts.POST_IMAGE));
                                    posts.setPost(docs.getString(Posts.POST));
                                    posts.setPostId(docs.getId());
                                    posts.setPostTitle(docs.getString(Posts.POST_TITLE));
                                    posts.setPostUser(docs.getString(Posts.POST_USER));
                                    posts.setPostComments(Integer.parseInt(docs.get(Posts.POST_COMMENTS).toString()));
                                    posts.setPostLikes(Integer.parseInt(docs.get(Posts.POST_LIKES).toString()));
                                    posts.setUserId(docs.getString(Posts.USER_ID));
                                    posts.setPostType(docs.getString(Posts.POST_TYPE));
                                    posts.setPostTimestamp(docs.getTimestamp(Posts.POST_TIMESTAMP));

                                    postsList.add(posts);
                                }

                                postsAdapter = new PostsAdapter(getActivity(), postsList);
                                homeRecyclerView.setAdapter(postsAdapter);
                            }

                        }
                    }
                });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_home, container, false);

        homeRecyclerView = view.findViewById(R.id.homeRecyclerView);
        homeRecyclerView.setHasFixedSize(true);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }



}
