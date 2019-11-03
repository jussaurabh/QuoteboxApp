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
import com.example.quotebox.interfaces.PoemPostsListener;
import com.example.quotebox.models.Posts;

import java.util.List;

public class ProfilePoemFragment extends Fragment {

    private LinearLayout poemSecPlaceholderLL;
    private Button goToWritePoemBtn;
    private RecyclerView poemSecRV;
    private PostsAdapter postsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frame_profile_poem_section, container, false);;

        poemSecPlaceholderLL = view.findViewById(R.id.poemSecPlaceholderLL);
        goToWritePoemBtn = view.findViewById(R.id.goToWritePoemBtn);
        poemSecRV = view.findViewById(R.id.poemSecRV);
        poemSecRV.setHasFixedSize(true);
        poemSecRV.setLayoutManager(new LinearLayoutManager(view.getContext()));

        goToWritePoemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), PostQuoteActivity.class).putExtra(Posts.POST_TYPE, Posts.POEM_TYPE_POST));
            }
        });

        ((HomeActivity) view.getContext()).passPoemPosts(new PoemPostsListener() {
            @Override
            public void setUserPoemPosts(List<Posts> p) {
                if (p.size() > 0) {
                    poemSecPlaceholderLL.setVisibility(View.GONE);
                    poemSecRV.setVisibility(View.VISIBLE);
                    postsAdapter = new PostsAdapter(view.getContext(), p);
                    poemSecRV.setAdapter(postsAdapter);
                }
            }
        });

        return view;
    }
}
