package com.example.quotebox.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.quotebox.interfaces.QuotePostsListener;
import com.example.quotebox.models.Posts;

import java.util.List;

public class ProfileQuoteFragment extends Fragment {

    private LinearLayout quoteSecPlaceholderLL;
    private RecyclerView quoteSecRV;
    private Button goToWriteQuoteBtn;
    private PostsAdapter postsAdapter;
    private GlobalClass globalClass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frame_profile_quote_section, container, false);

        globalClass = (GlobalClass) getActivity().getApplicationContext();

        quoteSecPlaceholderLL = view.findViewById(R.id.quoteSecPlaceholderLL);
        quoteSecRV = view.findViewById(R.id.quoteSecRV);
        quoteSecRV.setHasFixedSize(true);
        quoteSecRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        goToWriteQuoteBtn = view.findViewById(R.id.goToWriteQuoteBtn);

        goToWriteQuoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), PostQuoteActivity.class).putExtra(Posts.POST_TYPE, Posts.QUOTE_TYPE_POST));
            }
        });

//        ((HomeActivity) view.getContext()).passQuotePosts(new QuotePostsListener() {
        globalClass.passQuotePosts(new QuotePostsListener() {
            @Override
            public void setUserQuotePosts(List<Posts> p) {
                if (p.size() > 0) {
                    quoteSecPlaceholderLL.setVisibility(View.GONE);
                    quoteSecRV.setVisibility(View.VISIBLE);
                    postsAdapter = new PostsAdapter(view.getContext(), p);
                    quoteSecRV.setAdapter(postsAdapter);
                }
            }
        });

        return view;
    }


}
