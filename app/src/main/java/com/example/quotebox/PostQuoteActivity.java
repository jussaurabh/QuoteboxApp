package com.example.quotebox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.models.Posts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class PostQuoteActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    CollectionNames collNames;

    private static final String LOGGED_IN_USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        collNames = new CollectionNames();

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
        Log.d(
                "POST_QUOTE_LOG",
                "Image Uri: " + imgUri.toString() + "\n" +
                        "Post Title : " + postTitleEditText.getText().toString().trim() + "\n" +
                        "Post : " + postEditText.getText().toString().trim()
        );


        String posttitle = postTitleEditText.getText().toString().trim();
        String post = postEditText.getText().toString().trim();
        String posttype = getIntent().getStringExtra(Posts.POST_TYPE);

//        Posts posts = new Posts(
//                posttitle,
//                post,
//                img
//        );
    }
}
