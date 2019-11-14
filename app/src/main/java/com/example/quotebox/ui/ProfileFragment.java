package com.example.quotebox.ui;

import android.content.Context;
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

import com.example.quotebox.R;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Locale;


public class ProfileFragment extends Fragment {

    private FirebaseFirestore firestore;
    private Users userPostDetails;
    private GlobalClass globalClass;

    private ImageView userProfileAvatarIV;
    private TextView userProfileAuthornameTV, userFollowerCountTV, userFollowingsCountTV, userLikesCountTV, userAboutTV;
    private Button userQuoteCountBtn, userPoemCountBtn, userStoryCountBtn;
    private LinearLayout userAboutWrapperLL, userFollowBtnWrapperLL;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_profile, container, false);

        getChildFragmentManager().beginTransaction().replace(R.id.profilePostFLContainer, new ProfileQuoteFragment()).commit();

        firestore = FirebaseFirestore.getInstance();
        globalClass = (GlobalClass) getActivity().getApplicationContext();
        Users loggedInUserData = globalClass.getLoggedInUserData();

        userProfileAvatarIV = view.findViewById(R.id.userProfileAvatarIV);
        userProfileAuthornameTV = view.findViewById(R.id.userProfileAuthornameTV);
        userFollowerCountTV = view.findViewById(R.id.userFollowersCountTV);
        userFollowingsCountTV = view.findViewById(R.id.userFollowingsCountTV);
        userLikesCountTV = view.findViewById(R.id.userLikesCountTV);
        userAboutTV = view.findViewById(R.id.userAboutTV);
        userAboutWrapperLL = view.findViewById(R.id.userAboutWrapperLL);
        userFollowBtnWrapperLL = view.findViewById(R.id.userFollowBtnWrapperLL);
        userQuoteCountBtn = view.findViewById(R.id.userQuoteCountBtn);
        userPoemCountBtn = view.findViewById(R.id.userPoemCountBtn);
        userStoryCountBtn = view.findViewById(R.id.userStoryCountBtn);

        userProfileAuthornameTV.setText(loggedInUserData.getUsername());

        userFollowerCountTV.setText(String.valueOf(loggedInUserData.getFollowersCount()));
        userFollowingsCountTV.setText(String.valueOf(loggedInUserData.getFollowingsCount()));
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
