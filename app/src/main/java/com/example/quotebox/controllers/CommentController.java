package com.example.quotebox.controllers;

import androidx.annotation.NonNull;

import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.interfaces.CommentEventListeners;
import com.example.quotebox.models.Comments;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CommentController extends CommentEventListeners {

    private FirebaseFirestore firestore;
    private String commentCollection;

    private OnCommentPostListener commentPostListener;

    public CommentController() {
        this.firestore = FirebaseFirestore.getInstance();
        this.commentCollection = new CollectionNames().getCommentCollection();
        this.commentPostListener = null;
    }

    public CommentController post(Comments comment) {
        firestore.collection(commentCollection).add(comment)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful() && commentPostListener != null) {

                        }
                    }
                });

        return this;
    }


    public void addOnCommentPostListener(OnCommentPostListener commentPostListener) {
        this.commentPostListener = commentPostListener;
    }
}
