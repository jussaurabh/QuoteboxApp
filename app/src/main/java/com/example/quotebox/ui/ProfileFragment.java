package com.example.quotebox.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quotebox.FavoritePostActivity;
import com.example.quotebox.FollowListActivity;
import com.example.quotebox.R;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;


public class ProfileFragment extends Fragment {

    private GlobalClass globalClass;

    private ImageView userProfileAvatarIV;
    private TextView userProfileAuthornameTV, userFollowerCountTV, userFollowingsCountTV, userLikesCountTV, userAboutTV;
    private Button userQuoteCountBtn, userPoemCountBtn, userStoryCountBtn;
    private LinearLayout userAboutWrapperLL, followerCountWrapperLL, followingCountWrapperLL, postLikeCountWrapperLL;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_profile, container, false);

        getChildFragmentManager().beginTransaction().replace(R.id.profilePostFLContainer, new ProfileQuoteFragment()).commit();

        globalClass = (GlobalClass) getActivity().getApplicationContext();
        Users loggedInUserData = globalClass.getAllUsersData().get(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userProfileAvatarIV = view.findViewById(R.id.userProfileAvatarIV);
        userProfileAuthornameTV = view.findViewById(R.id.userProfileAuthornameTV);
        userFollowerCountTV = view.findViewById(R.id.userFollowersCountTV);
        userFollowingsCountTV = view.findViewById(R.id.userFollowingsCountTV);
        followerCountWrapperLL = view.findViewById(R.id.followerCountWrapperLL);
        followingCountWrapperLL = view.findViewById(R.id.followingCountWrapperLL);
        postLikeCountWrapperLL = view.findViewById(R.id.postLikeCountWrapperLL);
        userLikesCountTV = view.findViewById(R.id.userLikesCountTV);
        userAboutTV = view.findViewById(R.id.userAboutTV);
        userAboutWrapperLL = view.findViewById(R.id.userAboutWrapperLL);
        userQuoteCountBtn = view.findViewById(R.id.userQuoteCountBtn);
        userPoemCountBtn = view.findViewById(R.id.userPoemCountBtn);
        userStoryCountBtn = view.findViewById(R.id.userStoryCountBtn);

        userProfileAuthornameTV.setText(loggedInUserData.getUsername());

        userFollowerCountTV.setText(String.valueOf(loggedInUserData.getFollowerUsers().size()));
        userFollowingsCountTV.setText(String.valueOf(loggedInUserData.getFollowingUsers().size()));
        userLikesCountTV.setText(String.valueOf(loggedInUserData.getFavPosts().size()));

        String poemCount = String.format(Locale.getDefault(), "Poems (%d)", loggedInUserData.getNoOfPoemPosted());
        String quoteCount = String.format(Locale.getDefault(), "Quotes (%d)", loggedInUserData.getNoOfQuotesPosted());
        String storyCount = String.format(Locale.getDefault(), "Stories (%d)", loggedInUserData.getNoOfStoryPosted());

        userQuoteCountBtn.setText(quoteCount);
        userPoemCountBtn.setText(poemCount);
        userStoryCountBtn.setText(storyCount);

        if (loggedInUserData.getUserAvatar() != null) {
            Picasso.get().load(loggedInUserData.getUserAvatar())
                    .transform(new ImageCircleTransform())
                    .into(userProfileAvatarIV);
        }


        userQuoteCountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePostFragment(Posts.QUOTE_TYPE_POST);
            }
        });

        userPoemCountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePostFragment(Posts.POEM_TYPE_POST);
            }
        });

        userStoryCountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePostFragment(Posts.STORY_TYPE_POST);
            }
        });

        postLikeCountWrapperLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FavoritePostActivity.class));
            }
        });

        followerCountWrapperLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FollowListActivity.class));
            }
        });

        followingCountWrapperLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getContext(), FollowListActivity.class));
            }
        });

        return view;
    }


    public void togglePostFragment(String postType) {
        Fragment selectedFragment = null;

        switch (postType) {
            case Posts.QUOTE_TYPE_POST: {
                selectedFragment = new ProfileQuoteFragment();
                userQuoteCountBtn.setBackgroundResource(R.drawable.dark_rounded_solid_bg);
                userQuoteCountBtn.setTextColor(getResources().getColor(R.color.colorWhite, null));

                userStoryCountBtn.setBackgroundResource(R.color.transparent);
                userStoryCountBtn.setTextColor(getResources().getColor(R.color.colorPrimary, null));

                userPoemCountBtn.setBackgroundResource(R.color.transparent);
                userPoemCountBtn.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                break;
            }
            case Posts.POEM_TYPE_POST: {
                selectedFragment = new ProfilePoemFragment();
                userQuoteCountBtn.setBackgroundResource(R.color.transparent);
                userQuoteCountBtn.setTextColor(getResources().getColor(R.color.colorPrimary, null));

                userStoryCountBtn.setBackgroundResource(R.color.transparent);
                userStoryCountBtn.setTextColor(getResources().getColor(R.color.colorPrimary, null));

                userPoemCountBtn.setBackgroundResource(R.drawable.dark_rounded_solid_bg);
                userPoemCountBtn.setTextColor(getResources().getColor(R.color.colorWhite, null));
                break;
            }
            case Posts.STORY_TYPE_POST: {
                selectedFragment = new ProfileStoryFragment();
                userQuoteCountBtn.setBackgroundResource(R.color.transparent);
                userQuoteCountBtn.setTextColor(getResources().getColor(R.color.colorPrimary, null));

                userStoryCountBtn.setBackgroundResource(R.drawable.dark_rounded_solid_bg);
                userStoryCountBtn.setTextColor(getResources().getColor(R.color.colorWhite, null));

                userPoemCountBtn.setBackgroundResource(R.color.transparent);
                userPoemCountBtn.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                break;
            }
        }

        getChildFragmentManager().beginTransaction().replace(R.id.profilePostFLContainer, selectedFragment).commit();
    }

}
