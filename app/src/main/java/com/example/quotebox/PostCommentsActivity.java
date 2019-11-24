package com.example.quotebox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotebox.adapters.CommentAdapter;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.models.Comments;
import com.example.quotebox.models.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PostCommentsActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    GlobalClass globalClass;
    CollectionNames collnames;
    CommentAdapter commentAdapter;
    WriteBatch batch;

    String cmntId = null;
    int adaptPosition;

    Toolbar toolbar;
    LinearLayout cmntDefaultPlaceholderLL;
    ImageView cmntPostUserAvatarImageView;
    TextView cmntPostUserTV,cmntPostDateTV, cmntPostTitleTV, cmntPostTV, cmntPostAuthorTV, commentListDefaultTV;
    RecyclerView commentRecyclerView;
    EditText commentEditText;
    ImageButton commentSubmitBtn;
    ProgressBar commentListProgressBar, commentSubmitProgressBar;
    SwipeRefreshLayout commentSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);

        firestore = FirebaseFirestore.getInstance();
        batch = firestore.batch();
        globalClass = (GlobalClass) getApplicationContext();
        collnames = new CollectionNames();

        cmntDefaultPlaceholderLL = findViewById(R.id.cmntDefaultPlaceholderLL);
        cmntPostUserAvatarImageView = findViewById(R.id.cmntPostUserAvatarImageView);
        cmntPostUserTV = findViewById(R.id.cmntPostUserTV);
        cmntPostDateTV = findViewById(R.id.cmntPostDateTV);
        cmntPostTitleTV = findViewById(R.id.cmntPostTitleTV);
        cmntPostTV = findViewById(R.id.cmntPostTV);
        cmntPostAuthorTV = findViewById(R.id.cmntPostAuthorTV);
        commentRecyclerView = findViewById(R.id.commentRecyclerView);
        commentEditText = findViewById(R.id.commentEditText);
        commentSubmitBtn = findViewById(R.id.commentSubmitBtn);
        commentListDefaultTV = findViewById(R.id.commentListDefaultTV);
        commentListProgressBar = findViewById(R.id.commentListProgressBar);
        commentSubmitProgressBar = findViewById(R.id.commentSubmitProgressBar);
        commentSwipeRefresh = findViewById(R.id.commentSwipeRefresh);

        commentRecyclerView.setHasFixedSize(true);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Comments");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(PostCommentsActivity.this, HomeActivity.class));
            }
        });


        getSelectPost();
        getPostComments();

        commentSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPostComments();
                commentSwipeRefresh.setRefreshing(false);
            }
        });


    }


    public void getSelectPost() {
        final String postid = getIntent().getStringExtra(Posts.POST_ID);

        firestore.collection(CollectionNames.POSTS)
                .document(postid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Posts post = task.getResult().toObject(Posts.class);

                        DateFormat postDateFormat = new SimpleDateFormat("MMM d, ''yy");
                        Date date = new Date(post.getPostTimestamp().getSeconds() * 1000);
                        String dateStr = postDateFormat.format(date);

                        cmntPostTitleTV.setText(post.getPostTitle());
                        cmntPostAuthorTV.setText("- " + post.getPostUser());
                        cmntPostUserTV.setText(post.getPostUser());
                        cmntPostDateTV.setText(dateStr);
                        cmntPostTV.setText(post.getPost());

                        if (globalClass.getAllUsersData().get(post.getUserId()).getUserAvatar() != null) {
                            Picasso.get().load(globalClass.getAllUsersData().get(post.getUserId()).getUserAvatar())
                                    .transform(new ImageCircleTransform())
                                    .into(cmntPostUserAvatarImageView);
                        }
                    }
                });
    }


    public void getPostComments() {
        firestore.collection(CollectionNames.COMMENTS)
                .whereEqualTo(Comments.POST_ID, getIntent().getStringExtra(Posts.POST_ID))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        List<Comments> comments = new ArrayList<>();
                        HashMap<String, Comments> cmntsHashMap = new HashMap<>();

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Comments cmnt = new Comments();
                            cmnt.setCommentId(doc.getId());
                            cmnt.setComment(doc.getString(Comments.COMMENT));
                            cmnt.setCommentTimestamp(doc.getTimestamp(Comments.COMMENT_TIMESTAMP));
                            cmnt.setPostId(doc.getString(Comments.POST_ID));
                            cmnt.setUserId(doc.getString(Comments.USER_ID));
                            cmnt.setUserAvatar(doc.getString(Comments.USER_AVATAR));
                            cmnt.setUsername(doc.getString(Comments.USERNAME));

                            comments.add(cmnt);
                            cmntsHashMap.put(doc.getId(), cmnt);
                        }

                        if (comments.size() > 0) {
                            cmntDefaultPlaceholderLL.setVisibility(View.GONE);
                            commentRecyclerView.setVisibility(View.VISIBLE);

                            globalClass.setAllComments(cmntsHashMap);

                            Log.d("POST_COMMENT_LOG", "all comments : " + new Gson().toJson(comments));

                            commentAdapter = new CommentAdapter(PostCommentsActivity.this, comments);
                            commentRecyclerView.setAdapter(commentAdapter);
                        }
                        else {
                            commentListDefaultTV.setVisibility(View.VISIBLE);
                            commentListProgressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }


    public void submitComment(View v) {
        String comment = commentEditText.getText().toString();
        String username = globalClass.getLoggedInUserData().getUsername();
        String userAvatar = globalClass.getLoggedInUserData().getUserAvatar();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String postid = getIntent().getStringExtra(Posts.POST_ID);

        if (comment.isEmpty()) {
            Toast.makeText(
                    PostCommentsActivity.this,
                    "Please enter any comment",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }


        commentSubmitBtn.setVisibility(View.GONE);
        commentSubmitProgressBar.setVisibility(View.VISIBLE);

        final Comments cmnt = new Comments(
                userid,
                userAvatar,
                username,
                comment,
                Timestamp.now(),
                postid
        );

        firestore.collection(CollectionNames.COMMENTS).add(cmnt)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        commentEditText.setText("");

                        commentSubmitBtn.setVisibility(View.VISIBLE);
                        commentSubmitProgressBar.setVisibility(View.GONE);

                        cmnt.setCommentId(documentReference.getId());
                        List<Comments> updatedCmnts = new ArrayList<>(globalClass.getAllComments().values());
                        updatedCmnts.add(0, cmnt);

                        commentAdapter = new CommentAdapter(PostCommentsActivity.this, updatedCmnts);
                        commentRecyclerView.setAdapter(commentAdapter);

                        commentRecyclerView.setVisibility(View.VISIBLE);
                        cmntDefaultPlaceholderLL.setVisibility(View.GONE);

                        firestore.collection(CollectionNames.POSTS)
                                .document(postid)
                                .update(Posts.POST_COMMENTS, FieldValue.increment(1));
                    }
                });
    }

    public void commentMenuDelete(final int position, String id) {
        DocumentReference commentDocRef = firestore.collection(CollectionNames.COMMENTS).document(id);
        batch.delete(commentDocRef);

        DocumentReference postDocRef = firestore.collection(CollectionNames.POSTS).document(getIntent().getStringExtra(Posts.POST_ID));
        batch.update(postDocRef, Posts.POST_COMMENTS, FieldValue.increment(-1));

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                commentAdapter.notifyItemRemoved(position);
            }
        });
    }


    public void updateCommentDialog(final String cmnt, final String id, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PostCommentsActivity.this);

        final EditText input = new EditText(PostCommentsActivity.this);
        input.setText(cmnt);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        builder.setView(input)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DocumentReference docRef = firestore.collection(CollectionNames.COMMENTS).document(id);

                        batch.update(docRef, Comments.COMMENT, input.getText().toString());
                        batch.update(docRef, Comments.COMMENT_TIMESTAMP, Timestamp.now());

                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                commentAdapter.notifyItemChanged(position);
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        builder.create().show();
    }


}
