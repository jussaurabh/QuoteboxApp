package com.example.quotebox.controllers;

import androidx.annotation.NonNull;

import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.interfaces.PostListeners;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PostController {

    private FirebaseFirestore firestore;
    private String postCollection;
    private List<Posts> allPosts;
    private Posts post;

    private PostListeners.OnGetAllPostCompleteListener getAllPostCompleteListener;
    private PostListeners.OnGetPostCompleteListener getPostCompleteListener;
    private PostListeners.OnPostLikeUpdateListener postLikeUpdateListener;

    public PostController() {
        this.firestore = FirebaseFirestore.getInstance();
        this.postCollection = new CollectionNames().getPostCollection();
        getAllPostCompleteListener = null;
        getPostCompleteListener = null;
        postLikeUpdateListener = null;
    }

    public PostController updatePostLikeCount(final String postid, final boolean status) {
        getPost(postid).addOnGetPostCompleteListener(new PostListeners.OnGetPostCompleteListener() {
            @Override
            public void onGetPost(final Posts post) {
                firestore.collection(postCollection).document(postid)
                        .update(Posts.POST_LIKES, FieldValue.increment(status ? 1 : -1))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if (postLikeUpdateListener != null)
                                        postLikeUpdateListener.onPostLikeUpdate(status ?
                                                post.getPostLikes() + 1 :
                                                post.getPostLikes() - 1);
                                }
                            }
                        });

                firestore.collection(new CollectionNames().getUserCollection())
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .update(Users.FAV_POSTS, status ? FieldValue.arrayUnion(postid) : FieldValue.arrayRemove(postid));


            }
        });

        return this;
    }

    public PostController getPost(String id) {
        firestore.collection(postCollection).document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();

                        Posts post = new Posts();
                        post.setPostId(doc.getId());
                        post.setPost(doc.getString(Posts.POST));
                        post.setPostType(doc.getString(Posts.POST_TYPE));
                        post.setPostTitle(doc.getString(Posts.POST_TITLE));
                        post.setPostUser(doc.getString(Posts.POST_USER));
                        post.setPostImage(doc.getString(Posts.POST_IMAGE));
                        post.setPostTimestamp(doc.getTimestamp(Posts.POST_TIMESTAMP));
                        post.setUserId(doc.getString(Posts.USER_ID));
                        post.setPostLikes(Integer.parseInt(doc.get(Posts.POST_LIKES).toString()));
                        post.setPostComments(Integer.parseInt(doc.get(Posts.POST_COMMENTS).toString()));

                        if (getPostCompleteListener != null)
                            getPostCompleteListener.onGetPost(post);
                    }
                });

        return this;
    }

    public PostController getAllPosts() {
        firestore.collection(postCollection)
                .orderBy(Posts.POST_TIMESTAMP, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Posts> posts = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Posts post = new Posts();
                            post.setPostId(doc.getId());
                            post.setPost(doc.getString(Posts.POST));
                            post.setPostType(doc.getString(Posts.POST_TYPE));
                            post.setPostTitle(doc.getString(Posts.POST_TITLE));
                            post.setPostUser(doc.getString(Posts.POST_USER));
                            post.setPostImage(doc.getString(Posts.POST_IMAGE));
                            post.setPostTimestamp(doc.getTimestamp(Posts.POST_TIMESTAMP));
                            post.setUserId(doc.getString(Posts.USER_ID));
                            post.setPostLikes(Integer.parseInt(doc.get(Posts.POST_LIKES).toString()));
                            post.setPostComments(Integer.parseInt(doc.get(Posts.POST_COMMENTS).toString()));

                            posts.add(post);
                        }

                        if (getAllPostCompleteListener != null)
                            getAllPostCompleteListener.onGetAllPost(posts);
                    }
                });

        return this;
    }

//    public PostController getPoemPosts(String uid) {
//        getAllPosts().addOnGetAllPostCompleteListener(new PostListeners.OnGetAllPostCompleteListener() {
//            @Override
//            public void onGetAllPost(List<Posts> postsList) {
//
//            }
//        });
//
//        return this;
//    }

    public void setAllPosts(List<Posts> allPosts) {
        this.allPosts = allPosts;
    }

    public void setPost(Posts post) {
        this.post = post;
    }

    public void addOnGetAllPostCompleteListener(PostListeners.OnGetAllPostCompleteListener getAllPostCompleteListener) {
        this.getAllPostCompleteListener = getAllPostCompleteListener;
    }

    public void addOnGetPostCompleteListener(PostListeners.OnGetPostCompleteListener getPostCompleteListener) {
        this.getPostCompleteListener = getPostCompleteListener;
    }

    public void addOnPostLikeUpdateListener(PostListeners.OnPostLikeUpdateListener postLikeUpdateListener) {
        this.postLikeUpdateListener = postLikeUpdateListener;
    }
}
