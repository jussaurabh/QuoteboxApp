package com.example.quotebox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quotebox.adapters.PostsAdapter;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.example.quotebox.ui.ProfileFragment;
import com.example.quotebox.ui.ProfilePoemFragment;
import com.example.quotebox.ui.ProfileQuoteFragment;
import com.example.quotebox.ui.ProfileStoryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    SharedPreferencesConfig preferencesConfig;
    GlobalClass globalClass;

    Toolbar toolbar;
    TextView profileActUsernameTV, profileActFollowersCountTV, profileActFollowingsCountTV, profileActLikesCountTV;
    ImageView profileActUserAvatarIV;
    LinearLayout profileActFollowerCountWrapperLL, profileActFollowingCountWrapperLL, profileActLikeCountWrapperLL;
    Button followUserBtn, followingUserBtn, profileActQuoteCountBtn, profileActPoemCountBtn, profileActStoryCountBtn;
    ProgressBar followBtnProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firestore = FirebaseFirestore.getInstance();
        preferencesConfig = new SharedPreferencesConfig(this);
        globalClass = (GlobalClass) getApplicationContext();

        profileActUsernameTV = findViewById(R.id.profileActUsernameTV);
        profileActFollowersCountTV = findViewById(R.id.profileActUserFollowersCountTV);
        profileActFollowingsCountTV = findViewById(R.id.profileActUserFollowingsCountTV);
        profileActLikesCountTV = findViewById(R.id.profileActLikesCountTV);
        profileActUserAvatarIV = findViewById(R.id.profileActUserAvatarIV);
        profileActFollowerCountWrapperLL = findViewById(R.id.profileActFollowerCountWrapperLL);
        profileActFollowingCountWrapperLL = findViewById(R.id.profileActFollowingCountWrapperLL);
        profileActLikeCountWrapperLL = findViewById(R.id.profileActPostLikeCountWrapperLL);
        followUserBtn = findViewById(R.id.followUserBtn);
        followingUserBtn = findViewById(R.id.followingUserBtn);
        profileActQuoteCountBtn = findViewById(R.id.profileActQuoteCountBtn);
        profileActPoemCountBtn = findViewById(R.id.profileActPoemCountBtn);
        profileActStoryCountBtn = findViewById(R.id.profileActStoryCountBtn);
        followBtnProgressBar = findViewById(R.id.followBtnProgressBar);

        getSupportFragmentManager().beginTransaction().replace(R.id.profileActivityFL, new ProfileQuoteFragment()).commit();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(globalClass.getAllUsersData().get(getIntent().getStringExtra(Users.USER_ID)).getUsername());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        });


        firestore.collection(new CollectionNames().getUserCollection()).document(getIntent().getStringExtra(Users.USER_ID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();

                        Users user = new Users();
                        user.setUserAvatar(doc.getString(Users.USERAVATAR));
                        user.setUsername(doc.getString(Users.USERNAME));
                        user.setNoOfStoryPosted(Integer.parseInt(doc.get(Users.NO_OF_STORY_POSTED).toString()));
                        user.setNoOfPoemPosted(Integer.parseInt(doc.get(Users.NO_OF_POEM_POSTED).toString()));
                        user.setNoOfQuotesPosted(Integer.parseInt(doc.get(Users.NO_OF_QUOTES_POSTED).toString()));
                        user.setFavPosts((List<String>) doc.get(Users.FAV_POSTS));
                        user.setFollowersCount(Integer.parseInt(doc.get(Users.FOLLOWERS_COUNT).toString()));
                        user.setFollowingsCount(Integer.parseInt(doc.get(Users.FOLLOWINGS_COUNT).toString()));

                        Log.d("PROFILE_ACT_LOG", "user profile: " + new Gson().toJson(user));

                        if (user.getUserAvatar() != null) {
                            Picasso.get().load(user.getUserAvatar())
                                    .transform(new ImageCircleTransform())
                                    .into(profileActUserAvatarIV);
                        }

                        profileActUsernameTV.setText(user.getUsername());
                        profileActFollowersCountTV.setText(Integer.toString(user.getFollowersCount()));
                        profileActFollowingsCountTV.setText(Integer.toString(user.getFollowingsCount()));
                        profileActLikesCountTV.setText(Integer.toString(user.getFavPosts().size()));

                        String poemCount = String.format(Locale.getDefault(), "Poems (%d)", user.getNoOfPoemPosted());
                        String quoteCount = String.format(Locale.getDefault(), "Quotes (%d)", user.getNoOfQuotesPosted());
                        String storyCount = String.format(Locale.getDefault(), "Stories (%d)", user.getNoOfStoryPosted());

                        profileActPoemCountBtn.setText(poemCount);
                        profileActQuoteCountBtn.setText(quoteCount);
                        profileActStoryCountBtn.setText(storyCount);
                    }
                });

        profileActQuoteCountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePostFragment(Posts.QUOTE_TYPE_POST);
            }
        });

        profileActPoemCountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePostFragment(Posts.POEM_TYPE_POST);
            }
        });

        profileActStoryCountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePostFragment(Posts.STORY_TYPE_POST);
            }
        });

    }

    public void togglePostFragment(String postType) {
        Fragment selectedFragment = null;

        switch (postType) {
            case Posts.QUOTE_TYPE_POST: {
                selectedFragment = new ProfileQuoteFragment();
                profileActQuoteCountBtn.setBackgroundResource(R.drawable.dark_rounded_solid_bg);
                profileActQuoteCountBtn.setTextColor(getResources().getColor(R.color.colorWhite, null));

                profileActStoryCountBtn.setBackgroundResource(R.color.transparent);
                profileActStoryCountBtn.setTextColor(getResources().getColor(R.color.colorPrimary, null));

                profileActPoemCountBtn.setBackgroundResource(R.color.transparent);
                profileActPoemCountBtn.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                break;
            }
            case Posts.POEM_TYPE_POST: {
                selectedFragment = new ProfilePoemFragment();
                profileActQuoteCountBtn.setBackgroundResource(R.color.transparent);
                profileActQuoteCountBtn.setTextColor(getResources().getColor(R.color.colorPrimary, null));

                profileActStoryCountBtn.setBackgroundResource(R.color.transparent);
                profileActStoryCountBtn.setTextColor(getResources().getColor(R.color.colorPrimary, null));

                profileActPoemCountBtn.setBackgroundResource(R.drawable.dark_rounded_solid_bg);
                profileActPoemCountBtn.setTextColor(getResources().getColor(R.color.colorWhite, null));
                break;
            }
            case Posts.STORY_TYPE_POST: {
                selectedFragment = new ProfileStoryFragment();
                profileActQuoteCountBtn.setBackgroundResource(R.color.transparent);
                profileActQuoteCountBtn.setTextColor(getResources().getColor(R.color.colorPrimary, null));

                profileActStoryCountBtn.setBackgroundResource(R.drawable.dark_rounded_solid_bg);
                profileActStoryCountBtn.setTextColor(getResources().getColor(R.color.colorWhite, null));

                profileActPoemCountBtn.setBackgroundResource(R.color.transparent);
                profileActPoemCountBtn.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                break;
            }
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.profileActivityFL, selectedFragment).commit();
    }
}
