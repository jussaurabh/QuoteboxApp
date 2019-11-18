package com.example.quotebox.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.R;
import com.example.quotebox.adapters.PostsAdapter;
import com.example.quotebox.controllers.PostController;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.interfaces.PostListeners;
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

    private PostController postController;
    private PostsAdapter postsAdapter;
    private RecyclerView homeRecyclerView;
    ProgressBar homeFragProgressBar;
    LinearLayout homeFragDefaultLL;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        postController = new PostController();

        postController.getAllPosts().addOnGetAllPostCompleteListener(new PostListeners.OnGetAllPostCompleteListener() {
            @Override
            public void onGetAllPost(List<Posts> postsList) {
                if (postsList.size() > 0) {
                    homeRecyclerView.setVisibility(View.VISIBLE);
                    homeFragProgressBar.setVisibility(View.GONE);
                    postsAdapter = new PostsAdapter(getActivity(), postsList);
                    homeRecyclerView.setAdapter(postsAdapter);
                }
                else {
                    homeFragProgressBar.setVisibility(View.GONE);
                    homeFragDefaultLL.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_home, container, false);

        homeFragDefaultLL = view.findViewById(R.id.homeFragDefaultLL);
        homeFragProgressBar = view.findViewById(R.id.homeFragProgressBar);

        homeRecyclerView = view.findViewById(R.id.homeRecyclerView);
        homeRecyclerView.setHasFixedSize(true);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }



}
