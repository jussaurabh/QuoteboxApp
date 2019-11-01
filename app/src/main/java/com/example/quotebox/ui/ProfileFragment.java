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
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.R;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private FirebaseFirestore firestore;
    private Users userPostDetails;
    private SharedPreferencesConfig preferencesConfig;
    private GlobalClass globalClass;

    private static final FirebaseUser LOGGED_IN_USER = FirebaseAuth.getInstance().getCurrentUser();

    private ImageView userProfileAvatarIV;
    private TextView userProfileAuthornameTV, userFollowerCountTV, userFollowingsCountTV, userLikesCountTV, userAboutTV;
    private Button followUserBtn, followingUserBtn, userQuoteCountBtn, userPoemCountBtn, userStoryCountBtn;
    private ProgressBar followUserProgressBar;
    private LinearLayout userAboutWrapperLL;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getFragmentManager().beginTransaction().replace(R.id.profilePostFLContainer, new ProfileQuoteFragment()).commit();

        firestore = FirebaseFirestore.getInstance();
        preferencesConfig = new SharedPreferencesConfig(view.getContext());
        globalClass = (GlobalClass) getActivity().getApplicationContext();
        Users loggedInUserData = globalClass.getLoggedInUserData();

        userProfileAvatarIV = view.findViewById(R.id.userProfileAvatarIV);
        userProfileAuthornameTV = view.findViewById(R.id.userProfileAuthornameTV);
        userFollowerCountTV = view.findViewById(R.id.userFollowersCountTV);
        userFollowingsCountTV = view.findViewById(R.id.userFollowingsCountTV);
        userLikesCountTV = view.findViewById(R.id.userLikesCountTV);
        userAboutTV = view.findViewById(R.id.userAboutTV);
        userAboutWrapperLL = view.findViewById(R.id.userAboutWrapperLL);
        followingUserBtn = view.findViewById(R.id.followingUserBtn);
        followUserBtn = view.findViewById(R.id.followUserBtn);
        userQuoteCountBtn = view.findViewById(R.id.userQuoteCountBtn);
        userPoemCountBtn = view.findViewById(R.id.userPoemCountBtn);
        userStoryCountBtn = view.findViewById(R.id.userStoryCountBtn);
        followUserProgressBar = view.findViewById(R.id.followUserProgressBar);

        userProfileAuthornameTV.setText(loggedInUserData.getUsername());
        userQuoteCountBtn.setText(loggedInUserData.getNoOfQuotesPosted() + " Quote");
        userPoemCountBtn.setText(loggedInUserData.getNoOfPoemPosted() + " Poem");
        userStoryCountBtn.setText(loggedInUserData.getNoOfStoryPosted() + " Story");

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frame_profile, container, false);
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

        getFragmentManager().beginTransaction().replace(R.id.profilePostFLContainer, selectedFragment).commit();
    }

}
