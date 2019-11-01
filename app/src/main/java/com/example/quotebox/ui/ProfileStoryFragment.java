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
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.PostQuoteActivity;
import com.example.quotebox.R;
import com.example.quotebox.models.Posts;

public class ProfileStoryFragment extends Fragment {

    private LinearLayout storySecPlaceholderLL;
    private RecyclerView storySecRV;
    private Button goToWriteStoryBtn;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storySecPlaceholderLL = view.findViewById(R.id.storySecPlaceholderLL);
        storySecRV = view.findViewById(R.id.storySecRV);
        goToWriteStoryBtn = view.findViewById(R.id.goToWriteStoryBtn);

        goToWriteStoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), PostQuoteActivity.class).putExtra(Posts.POST_TYPE, Posts.STORY_TYPE_POST));
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frame_profile_story_section, container, false);
    }
}
