package com.example.quotebox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class PostQuoteActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    CollectionNames collNames;
    SharedPreferencesConfig preferencesConfig;
    GlobalClass globalClass;
    Users loggedInUserData;

//    private static final String LOGGED_IN_USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final int PICK_IMAGE_REQUEST = 1;
    
    Toolbar toolbar;
    EditText postTitleEditText, postEditText;
    TextView postAddImageBtnTag, postTitleErrMsgTV, postErrMsgTV;
    ImageView postImageView;
    ImageButton removePostImageIB;
    LinearLayout postAddBtnLL;
    RelativeLayout postImageWrapperRL;
    Button postSubmitBtn;
    Uri imgUri;
    Dialog postDialog;
    ProgressBar postProgressBar;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getData() != null) {
            imgUri = data.getData();

            postAddBtnLL.setVisibility(View.GONE);
            postImageWrapperRL.setVisibility(View.VISIBLE);
//            postImageView.setMaxHeight(100);

            Picasso.get().load(imgUri).into(postImageView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_quote);
        
        final String POSTTYPE = getIntent().getStringExtra(Posts.POST_TYPE);

        firestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("uploads/" + POSTTYPE);
        collNames = new CollectionNames();
        preferencesConfig = new SharedPreferencesConfig(this);
        globalClass = (GlobalClass) getApplicationContext();
        loggedInUserData = globalClass.getLoggedInUserData();

        postDialog = new Dialog(this);

        postProgressBar = findViewById(R.id.postProgressBar);
        postAddImageBtnTag = findViewById(R.id.postAddImageBtnTag);
        postAddBtnLL = findViewById(R.id.postAddBtnLL);
        postImageView = findViewById(R.id.postImageView);
        postImageWrapperRL = findViewById(R.id.postImageWrapperRL);
        removePostImageIB = findViewById(R.id.removePostImageIB);
        postTitleEditText = findViewById(R.id.postTitleEditText);
        postEditText = findViewById(R.id.postEditText);
        postSubmitBtn = findViewById(R.id.postSubmitBtn);
        postTitleErrMsgTV = findViewById(R.id.postTitleErrMsgTV);
        postErrMsgTV = findViewById(R.id.postErrMsgTV);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Write your " + POSTTYPE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        
        // -- setting default text for Quote Post, Story Post and Poem Post
        postAddImageBtnTag.setText("Add " + POSTTYPE + " Image");
        postTitleEditText.setHint(POSTTYPE + " Title");
        postEditText.setHint("Write your " + POSTTYPE);
        postSubmitBtn.setText("Post your " + POSTTYPE);
        

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostQuoteActivity.this, HomeActivity.class));
            }
        });


        postAddBtnLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        removePostImageIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postImageWrapperRL.setVisibility(View.GONE);
                postAddBtnLL.setVisibility(View.VISIBLE);
                imgUri = null;
                Picasso.get().load(imgUri).into(postImageView);
            }
        });

        postSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postProgressBar.setVisibility(View.VISIBLE);
                postSubmitBtn.setVisibility(View.GONE);
                insertPostToDatabase();
            }
        });
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    public void insertPostToDatabase() {

        // returns all users credentials
        HashMap<String, Users> data = preferencesConfig.getAllUserCreds();
        final String LOGGED_IN_USER_ID = firebaseUser.getUid();

        final Posts posts = new Posts(
                postTitleEditText.getText().toString().trim(),
                postEditText.getText().toString().trim(),
                null,
                getIntent().getStringExtra(Posts.POST_TYPE),
                LOGGED_IN_USER_ID,
                data.get(LOGGED_IN_USER_ID).getUsername(),
                0,
                0
        );

        if (imgUri != null) {
            final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imgUri));

            fileRef.putFile(imgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) throw task.getException();
                        return fileRef.getDownloadUrl();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            posts.setPostImage(downloadUri.toString());

                            firestore.collection(collNames.getPostCollection())
                                    .add(posts.getPostsCreds())
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()) {
                                                postProgressBar.setVisibility(View.GONE);
                                                postSubmitBtn.setVisibility(View.VISIBLE);

                                                switch (getIntent().getStringExtra(Posts.POST_TYPE)) {
                                                    case Posts.QUOTE_TYPE_POST: {
                                                        firestore.collection(collNames.getUserCollection()).document(LOGGED_IN_USER_ID)
                                                                .update(Users.NO_OF_QUOTES_POSTED, globalClass.incrementPostCount(getIntent().getStringExtra(Posts.POST_TYPE)));
                                                        break;
                                                    }
                                                    case Posts.POEM_TYPE_POST: {
                                                        firestore.collection(collNames.getUserCollection()).document(LOGGED_IN_USER_ID)
                                                                .update(Users.NO_OF_POEM_POSTED, globalClass.incrementPostCount(getIntent().getStringExtra(Posts.POST_TYPE)));
                                                        break;
                                                    }
                                                    case Posts.STORY_TYPE_POST: {
                                                        firestore.collection(collNames.getUserCollection()).document(LOGGED_IN_USER_ID)
                                                                .update(Users.NO_OF_STORY_POSTED, globalClass.incrementPostCount(getIntent().getStringExtra(Posts.POST_TYPE)));
                                                        break;
                                                    }
                                                }

                                                finish();
                                                startActivity(new Intent(PostQuoteActivity.this, HomeActivity.class));
                                            }
                                            else {
                                                Log.d("POST_LOG ", task.getException().toString());
                                            }
                                        }
                                    });
                        }
                    }
                });
        }
        else {
            firestore.collection(collNames.getPostCollection())
                    .add(posts.getPostsCreds())
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                postProgressBar.setVisibility(View.GONE);
                                postSubmitBtn.setVisibility(View.VISIBLE);

                                switch (getIntent().getStringExtra(Posts.POST_TYPE)) {
                                    case Posts.QUOTE_TYPE_POST: {
                                        firestore.collection(collNames.getUserCollection()).document(LOGGED_IN_USER_ID)
                                                .update(Users.NO_OF_QUOTES_POSTED, globalClass.incrementPostCount(getIntent().getStringExtra(Posts.POST_TYPE)));
                                        break;
                                    }
                                    case Posts.POEM_TYPE_POST: {
                                        firestore.collection(collNames.getUserCollection()).document(LOGGED_IN_USER_ID)
                                                .update(Users.NO_OF_POEM_POSTED, globalClass.incrementPostCount(getIntent().getStringExtra(Posts.POST_TYPE)));
                                        break;
                                    }
                                    case Posts.STORY_TYPE_POST: {
                                        firestore.collection(collNames.getUserCollection()).document(LOGGED_IN_USER_ID)
                                                .update(Users.NO_OF_STORY_POSTED, globalClass.incrementPostCount(getIntent().getStringExtra(Posts.POST_TYPE)));
                                        break;
                                    }
                                }

                                finish();
                                startActivity(new Intent(PostQuoteActivity.this, HomeActivity.class));
                            }
                            else {
                                Log.d("POST_LOG ", task.getException().toString());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("POST_QUTOE_ACTIVITY ", e.getMessage());
                        }
                    });
        }
    }


}
