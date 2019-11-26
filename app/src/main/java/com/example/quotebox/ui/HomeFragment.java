package com.example.quotebox.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quotebox.R;
import com.example.quotebox.adapters.PostsAdapter;
import com.example.quotebox.controllers.PostController;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.interfaces.PostListeners;
import com.example.quotebox.models.Posts;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {

    private boolean isHomeFragmentLoaded = false;

    private PostController postController;
    private PostsAdapter postsAdapter;
    GlobalClass globalClass;
    RecyclerView homeRecyclerView;
    SwipeRefreshLayout homeSwipeRefresh;
//    ProgressBar homeFragProgressBar;
    LinearLayout homeFragDefaultLL, homeFragmentLL;


    @Override
    public void onPause() {
        super.onPause();

        Log.d("HOME_FRAG", "on Home fragment onPause");

        this.isHomeFragmentLoaded = true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("HOME_FRAG", "on Home fragment onCreated");

        globalClass = (GlobalClass) getActivity().getApplicationContext();
        postController = new PostController(globalClass);

        displayAllPosts();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isHomeFragmentLoaded) {
            Log.d("HOME_FRAG", "on Home fragment RESUME");
            postController = new PostController(globalClass);

            displayAllPosts();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_home, container, false);

        homeFragDefaultLL = view.findViewById(R.id.homeFragDefaultLL);
        homeSwipeRefresh = view.findViewById(R.id.homeSwipeRefresh);
//        homeFragProgressBar = view.findViewById(R.id.homeFragProgressBar);
        homeFragmentLL = view.findViewById(R.id.homeFragmentLL);

        homeRecyclerView = view.findViewById(R.id.homeRecyclerView);
        homeRecyclerView.setHasFixedSize(true);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        homeSwipeRefresh.setRefreshing(true);

        homeSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayAllPosts();
                homeSwipeRefresh.setRefreshing(false);
            }
        });

        return view;
    }


    public void displayAllPosts() {
        postController.getAllPosts().addOnGetAllPostCompleteListener(new PostListeners.OnGetAllPostCompleteListener() {
            @Override
            public void onGetAllPost(List<Posts> postsList) {
                if (postsList.size() > 0) {
                    homeRecyclerView.setVisibility(View.VISIBLE);
//                        homeFragProgressBar.setVisibility(View.GONE);
                    postsAdapter = new PostsAdapter(getActivity(), postsList);
                    homeRecyclerView.setAdapter(postsAdapter);

                    homeSwipeRefresh.setRefreshing(false);

                    HashMap<String, Posts> allposts = new HashMap<>();

                    for (Posts p : postsList) {
                        allposts.put(p._getPostId(), p);
                    }

                    globalClass.setAllPosts(allposts);
                }
                else {
//                        homeFragProgressBar.setVisibility(View.GONE);
                    homeSwipeRefresh.setRefreshing(false);
                    homeFragDefaultLL.setVisibility(View.VISIBLE);

                    HashMap<String, Posts> allposts = new HashMap<>();

                    for (Posts p : postsList) {
                        allposts.put(p._getPostId(), p);
                    }

                    globalClass.setAllPosts(allposts);
                }
            }
        });
    }

}
