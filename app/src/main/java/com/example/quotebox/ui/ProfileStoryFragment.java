package com.example.quotebox.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.HomeActivity;
import com.example.quotebox.PostQuoteActivity;
import com.example.quotebox.R;
import com.example.quotebox.adapters.PostsAdapter;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.interfaces.StoryPostsListener;
import com.example.quotebox.models.Posts;

import java.util.List;

public class ProfileStoryFragment extends Fragment {

    private LinearLayout storySecPlaceholderLL;
    private RecyclerView storySecRV;
    private Button goToWriteStoryBtn;
    private PostsAdapter postsAdapter;
    private GlobalClass globalClass;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frame_profile_story_section, container, false);

        globalClass = (GlobalClass) getActivity().getApplicationContext();

        storySecPlaceholderLL = view.findViewById(R.id.storySecPlaceholderLL);
        storySecRV = view.findViewById(R.id.storySecRV);
        storySecRV.setHasFixedSize(true);
        storySecRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        goToWriteStoryBtn = view.findViewById(R.id.goToWriteStoryBtn);

        goToWriteStoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), PostQuoteActivity.class).putExtra(Posts.POST_TYPE, Posts.STORY_TYPE_POST));
            }
        });

        globalClass.passStoryPosts(new StoryPostsListener() {
            @Override
            public void setUserStoryPosts(List<Posts> p) {
                if (p.size() > 0) {
                    storySecPlaceholderLL.setVisibility(View.GONE);
                    storySecRV.setVisibility(View.VISIBLE);
                    postsAdapter = new PostsAdapter(view.getContext(), p);
                    storySecRV.setAdapter(postsAdapter);
                }
            }
        });

        return view;
    }
}
