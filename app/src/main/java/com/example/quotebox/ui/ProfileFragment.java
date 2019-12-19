package com.example.quotebox.ui;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
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
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;


public class ProfileFragment extends Fragment {

    static final int PICK_IMAGE_REQUEST = 1;

    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private GlobalClass globalClass;
    Bundle extras;
    Uri imgUri;
    Users loggedInUserData;

    private ImageView userProfileAvatarIV;
    private TextView userProfileAuthornameTV, userFollowerCountTV, userFollowingsCountTV, userLikesCountTV, userAboutTV;
    private Button userQuoteCountBtn, userPoemCountBtn, userStoryCountBtn;
    private LinearLayout userAboutWrapperLL, followerCountWrapperLL, followingCountWrapperLL, postLikeCountWrapperLL;
    private ProgressBar profileImageLoadingProgressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_profile, container, false);


        loggedInUserData = globalClass.getAllUsersData().get(FirebaseAuth.getInstance().getCurrentUser().getUid());

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
        profileImageLoadingProgressBar = view.findViewById(R.id.profileImageLoadingProgressBar);

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

        if (loggedInUserData.getUserAboutMe() != null) {
            if (loggedInUserData.getUserAboutMe().length() > 0) {
                userAboutWrapperLL.setVisibility(View.VISIBLE);
                userAboutTV.setText(loggedInUserData.getUserAboutMe());
            }
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
                extras.putString(Users.USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
                extras.putString("follow", Users.FOLLOWER_USERS);
                startActivity(new Intent(getContext(), FollowListActivity.class).putExtras(extras));
            }
        });

        followingCountWrapperLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extras.putString(Users.USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
                extras.putString("follow", Users.FOLLOWING_USERS);
                startActivity(new Intent(getContext(), FollowListActivity.class).putExtras(extras));
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST &&
                data != null &&
                data.getData() != null) {
            imgUri = data.getData();

            profileImageLoadingProgressBar.setVisibility(View.VISIBLE);

            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imgUri));

            fileRef.putFile(imgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) throw task.getException();
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        final String newUserAvatar = task.getResult().toString();
                        firestore.collection(CollectionNames.USERS).document(loggedInUserData._getUserId())
                                .update(Users.USERAVATAR, newUserAvatar)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            profileImageLoadingProgressBar.setVisibility(View.GONE);
                                            Picasso.get().load(imgUri).transform(new ImageCircleTransform())
                                                    .into(userProfileAvatarIV);
                                            globalClass.getAllUsersData().get(loggedInUserData._getUserId())
                                                    .setUserAvatar(newUserAvatar);
                                            loggedInUserData.setUserAvatar(newUserAvatar);
                                        }
                                    }
                                });
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeAvatarMenu: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View v = LayoutInflater.from(getContext()).inflate(R.layout.change_avatar_dialog_layout, null);

                Button selectImgBtn, removeImgBtn;
                selectImgBtn = v.findViewById(R.id.selectProfileImgBtn);
                removeImgBtn = v.findViewById(R.id.removeProfileImgBtn);

                builder.setView(v);
                final AlertDialog dialog = builder.create();

                removeImgBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        storageReference.child(loggedInUserData.getUserAvatar()).delete()
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
                                            firestore.collection(CollectionNames.USERS).document(loggedInUserData._getUserId())
                                                    .update(Users.USERAVATAR, null)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            dialog.dismiss();
                                                            Picasso.get().load(R.mipmap.ic_avatar_placeholder_round)
                                                                    .transform(new ImageCircleTransform())
                                                                    .into(userProfileAvatarIV);
                                                            globalClass.getAllUsersData().get(loggedInUserData._getUserId())
                                                                    .setUserAvatar(null);
                                                            loggedInUserData.setUserAvatar(null);
                                                        }
                                                    });
//                                        } else {
//                                            Toast.makeText(
//                                                    getContext(),
//                                                    "ERRRO on deletin profile image",
//                                                    Toast.LENGTH_LONG
//                                            ).show();
//                                            dialog.dismiss();
//                                        }
//                                    }
//                                });


                    }
                });

                selectImgBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        openFileChooser();
                    }
                });
                builder.show();
                break;
            }
        }

        return false;
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getChildFragmentManager().beginTransaction().replace(R.id.profilePostFLContainer, new ProfileQuoteFragment()).commit();

        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("userProfileImages");
        globalClass = (GlobalClass) getActivity().getApplicationContext();
        extras = new Bundle();
    }
}
