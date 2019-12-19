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

import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.models.Notifications;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.example.quotebox.ui.ProfilePoemFragment;
import com.example.quotebox.ui.ProfileQuoteFragment;
import com.example.quotebox.ui.ProfileStoryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    WriteBatch batch;
    String LOGGED_IN_UID;
    SharedPreferencesConfig preferencesConfig;
    GlobalClass globalClass;
    Bundle extras;

    Toolbar toolbar;
    TextView profileActUsernameTV, profileActFollowersCountTV, profileActFollowingsCountTV, profileActLikesCountTV, followRequestSentTV;
    ImageView profileActUserAvatarIV;
    LinearLayout profileActFollowerCountWrapperLL, profileActFollowingCountWrapperLL, profileActLikeCountWrapperLL, postTabsWrapperLL, userFollowBtnWrapperLL, userPrivateProfileWrapperLL;
    Button followUserBtn, followingUserBtn, profileActQuoteCountBtn, profileActPoemCountBtn, profileActStoryCountBtn;
    ProgressBar followBtnProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        getProfileUserDetails();

        toolbar.setTitle(globalClass.getAllUsersData().get(getIntent().getStringExtra(Users.USER_ID)).getUsername());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        });

        // user who is being followed
        final DocumentReference followingDoc = firestore.collection(CollectionNames.USERS).document(getIntent().getStringExtra(Users.USER_ID));
        // user who is gonna follow
        final DocumentReference followerDoc = firestore.collection(CollectionNames.USERS).document(LOGGED_IN_UID);

        // -- if user has already sent follow request
        if (globalClass.getAllUsersData().get(getIntent().getStringExtra(Users.USER_ID)).getFollowRequestReceived().contains(LOGGED_IN_UID)) {
            followUserBtn.setVisibility(View.GONE);
            followRequestSentTV.setVisibility(View.VISIBLE);
        }

        if (LOGGED_IN_UID.equals(getIntent().getStringExtra(Users.USER_ID))) {
            userFollowBtnWrapperLL.setVisibility(View.GONE);
        }

        // -- if user is already following this user
        if (globalClass.getAllUsersData().get(LOGGED_IN_UID).getFollowingUsers().contains(getIntent().getStringExtra(Users.USER_ID))) {
            followUserBtn.setVisibility(View.GONE);
            followingUserBtn.setVisibility(View.VISIBLE);
        }

        followUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followUserBtn.setVisibility(View.GONE);
                followBtnProgressBar.setVisibility(View.VISIBLE);

                // if the account is private
                if (globalClass.getAllUsersData().get(getIntent().getStringExtra(Users.USER_ID)).isPrivateProfile()) {
                    batch.update(followerDoc, Users.FOLLOW_REQUEST_SENT, FieldValue.arrayUnion(getIntent().getStringExtra(Users.USER_ID)));
                    batch.update(followingDoc, Users.FOLLOW_REQUEST_RECEIVED, FieldValue.arrayUnion(LOGGED_IN_UID));

                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            followBtnProgressBar.setVisibility(View.GONE);
                            followRequestSentTV.setVisibility(View.VISIBLE);

                            globalClass.getAllUsersData().get(LOGGED_IN_UID)
                                    .getFollowRequestSent().add(getIntent().getStringExtra(Users.USER_ID));

                            globalClass.getAllUsersData().get(getIntent().getStringExtra(Users.USER_ID))
                                    .getFollowRequestReceived().add(LOGGED_IN_UID);
                        }
                    });
                }
                else {
                    batch.update(followerDoc, Users.FOLLOWING_USERS, FieldValue.arrayUnion(getIntent().getStringExtra(Users.USER_ID)));
                    batch.update(followingDoc, Users.FOLLOWER_USERS, FieldValue.arrayUnion(LOGGED_IN_UID));

                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            followBtnProgressBar.setVisibility(View.GONE);
                            followingUserBtn.setVisibility(View.VISIBLE);

                            globalClass.getAllUsersData().get(LOGGED_IN_UID)
                                    .getFollowingUsers().add(getIntent().getStringExtra(Users.USER_ID));

                            globalClass.getAllUsersData().get(getIntent().getStringExtra(Users.USER_ID))
                                    .getFollowerUsers().add(LOGGED_IN_UID);
                        }
                    });

                    Notifications notifyData = new Notifications(
                            getIntent().getStringExtra(Users.USER_ID),
                            Timestamp.now(),
                            globalClass.getAllUsersData().get(LOGGED_IN_UID).getUsername() + " started following you",
                            Notifications.FOLLOWING_TYPE_NOTIFY,
                            LOGGED_IN_UID
                    );

                    firestore.collection(CollectionNames.NOTIFICATIONS).document(Timestamp.now().toString())
                            .set(notifyData);
                }
            }
        });


        followingUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followingUserBtn.setVisibility(View.GONE);
                followBtnProgressBar.setVisibility(View.VISIBLE);

                batch.update(followerDoc, Users.FOLLOWING_USERS, FieldValue.arrayRemove(getIntent().getStringExtra(Users.USER_ID)));
                batch.update(followingDoc, Users.FOLLOWER_USERS, FieldValue.arrayRemove(LOGGED_IN_UID));

                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        followBtnProgressBar.setVisibility(View.GONE);
                        followUserBtn.setVisibility(View.VISIBLE);

                        globalClass.getAllUsersData().get(LOGGED_IN_UID)
                                .getFollowingUsers().remove(getIntent().getStringExtra(Users.USER_ID));

                        globalClass.getAllUsersData().get(getIntent().getStringExtra(Users.USER_ID))
                                .getFollowerUsers().remove(LOGGED_IN_UID);
                    }
                });
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

        profileActFollowerCountWrapperLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, FollowListActivity.class));
            }
        });


        profileActFollowingCountWrapperLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extras.putString(Users.USER_ID, getIntent().getStringExtra(Users.USER_ID));
                extras.putString("follow", Users.FOLLOWING_USERS);
                startActivity(new Intent(ProfileActivity.this, FollowListActivity.class).putExtras(extras));
            }
        });


        profileActFollowerCountWrapperLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extras.putString(Users.USER_ID, getIntent().getStringExtra(Users.USER_ID));
                extras.putString("follow", Users.FOLLOWER_USERS);
                startActivity(new Intent(ProfileActivity.this, FollowListActivity.class).putExtras(extras));
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


    public void getProfileUserDetails() {
        firestore.collection(CollectionNames.USERS).document(getIntent().getStringExtra(Users.USER_ID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Users user = task.getResult().toObject(Users.class);
//                        DocumentSnapshot doc = task.getResult();

//                        Users user = new Users();
//                        user.setUserAvatar(doc.getString(Users.USERAVATAR));
//                        user.setUsername(doc.getString(Users.USERNAME));
//                        user.setNoOfStoryPosted(Integer.parseInt(doc.get(Users.NO_OF_STORY_POSTED).toString()));
//                        user.setNoOfPoemPosted(Integer.parseInt(doc.get(Users.NO_OF_POEM_POSTED).toString()));
//                        user.setNoOfQuotesPosted(Integer.parseInt(doc.get(Users.NO_OF_QUOTES_POSTED).toString()));
//                        user.setFavPosts((List<String>) doc.get(Users.FAV_POSTS));
//                        user.setFollowerUsers((List<String>) doc.get(Users.FOLLOWER_USERS));
//                        user.setFollowingUsers((List<String>) doc.get(Users.FOLLOWING_USERS));
//                        user.setPrivateProfile(doc.getBoolean(Users.IS_PRIVATE_PROFILE));

                        Log.d("PROFILE_ACT_LOG", "user profile: " + new Gson().toJson(user));

                        if (user.getUserAvatar() != null) {
                            Picasso.get().load(user.getUserAvatar())
                                    .transform(new ImageCircleTransform())
                                    .into(profileActUserAvatarIV);
                        }

                        profileActUsernameTV.setText(user.getUsername());
                        profileActFollowersCountTV.setText(Integer.toString(user.getFollowerUsers().size()));
                        profileActFollowingsCountTV.setText(Integer.toString(user.getFollowingUsers().size()));
                        profileActLikesCountTV.setText(Integer.toString(user.getFavPosts().size()));

                        String poemCount = String.format(Locale.getDefault(), "Poems (%d)", user.getNoOfPoemPosted());
                        String quoteCount = String.format(Locale.getDefault(), "Quotes (%d)", user.getNoOfQuotesPosted());
                        String storyCount = String.format(Locale.getDefault(), "Stories (%d)", user.getNoOfStoryPosted());

                        profileActPoemCountBtn.setText(poemCount);
                        profileActQuoteCountBtn.setText(quoteCount);
                        profileActStoryCountBtn.setText(storyCount);


                        if (user.isPrivateProfile()) {
                            if (globalClass.getAllUsersData().get(LOGGED_IN_UID).getFollowingUsers().contains(getIntent().getStringExtra(Users.USER_ID))) {
                                followUserBtn.setVisibility(View.GONE);
                                followingUserBtn.setVisibility(View.VISIBLE);
                                userPrivateProfileWrapperLL.setVisibility(View.GONE);
                                getSupportFragmentManager().beginTransaction().replace(R.id.profileActivityFL, new ProfileQuoteFragment()).commit();
                            }
                            else {
                                postTabsWrapperLL.setVisibility(View.GONE);
                            }
                        }
                        else {
                            userPrivateProfileWrapperLL.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.profileActivityFL, new ProfileQuoteFragment()).commit();
                        }


                    }
                });
    }


    public void init() {
        firestore = FirebaseFirestore.getInstance();
        LOGGED_IN_UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        batch = firestore.batch();
        preferencesConfig = new SharedPreferencesConfig(this);
        globalClass = (GlobalClass) getApplicationContext();
        globalClass.setSelectedUserPosts(getIntent().getStringExtra(Users.USER_ID));
        extras = new Bundle();

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
        postTabsWrapperLL = findViewById(R.id.postTabsWrapperLL);
        userFollowBtnWrapperLL = findViewById(R.id.userFollowBtnWrapperLL);
        userPrivateProfileWrapperLL = findViewById(R.id.userPrivateProfileWrapperLL);
        followRequestSentTV = findViewById(R.id.followRequestSentTV);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
