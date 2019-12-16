package com.example.quotebox.controllers;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.interfaces.OnSignupSuccessListener;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuthController {

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private OnSignupSuccessListener onSignupSuccessListener;


    public AuthController() {
        this.firestore = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        onSignupSuccessListener = null;
    }


    public AuthController signup(Activity activity, final String username, final String email, final String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final String signUpUserId = task.getResult().getUser().getUid();

                            HashMap<String, List<String>> userPostColl = new HashMap<>();
                            userPostColl.put(Users.DEFAULT_POST_COLLECTION, new ArrayList<String>());

                            final Users users = new Users(
                                    username,
                                    email,
                                    password,
                                    null,
                                    null,
                                    null,
                                    new ArrayList<String>(),    // favPosts
                                    0,
                                    0,
                                    0,
                                    new ArrayList<String>(),    // followerUsers
                                    new ArrayList<String>(),    // followingUsers
                                    userPostColl,
                                    false,
                                    new ArrayList<String>(),    // followRequestSent
                                    new ArrayList<String>()     // followRequestReceived
                            );

                            firestore.collection(CollectionNames.USERS)
                                    .document(signUpUserId)
                                    .set(users)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (onSignupSuccessListener != null) {
                                                onSignupSuccessListener.onSignupSuccess(signUpUserId, users);
                                            }

                                        }
                                    });
                        }
                        else {
                            if (onSignupSuccessListener != null)
                                onSignupSuccessListener.onSignupSuccess(null,null);
                        }
                    }
                });

        return this;
    }

    public void addOnSignupSuccessListener(OnSignupSuccessListener onSignupSuccessListener) {
        this.onSignupSuccessListener = onSignupSuccessListener;
    }

}
