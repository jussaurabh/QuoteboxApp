package com.example.quotebox.ui;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.PostQuoteActivity;
import com.example.quotebox.R;
import com.example.quotebox.interfaces.QuotePostsListener;
import com.example.quotebox.models.Posts;

import java.util.List;

public class ProfileQuoteFragment extends Fragment implements QuotePostsListener {

    private LinearLayout quoteSecPlaceholderLL;
    private RecyclerView quoteSecRV;
    private Button goToWriteQuoteBtn;
    private List<Posts> quotePostList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frame_profile_quote_section, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        quoteSecPlaceholderLL = view.findViewById(R.id.quoteSecPlaceholderLL);
        quoteSecRV = view.findViewById(R.id.quoteSecRV);
        goToWriteQuoteBtn = view.findViewById(R.id.goToWriteQuoteBtn);

        goToWriteQuoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), PostQuoteActivity.class).putExtra(Posts.POST_TYPE, Posts.QUOTE_TYPE_POST));
            }
        });
    }

    @Override
    public void setUserQuotePosts(List<Posts> p, String s) {
        quotePostList = p;
        Log.d("QUOTE_FRAG", "quote posts: " + quotePostList.toString());
        goToWriteQuoteBtn.setText(s);
    }
}
