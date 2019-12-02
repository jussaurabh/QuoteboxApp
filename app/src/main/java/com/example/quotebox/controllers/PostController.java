package com.example.quotebox.controllers;

import androidx.annotation.NonNull;

import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.interfaces.PostListeners;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostController extends PostListeners {

    private FirebaseFirestore firestore;
    private List<Posts> allPosts;
    private Posts post;
    HashMap<String, Users> allUsersData;

    private OnGetAllPostCompleteListener getAllPostCompleteListener;
    private OnGetPostCompleteListener getPostCompleteListener;
    private OnPostLikeUpdateListener postLikeUpdateListener;

    public PostController() {
        this.firestore = FirebaseFirestore.getInstance();
        getAllPostCompleteListener = null;
        getPostCompleteListener = null;
        postLikeUpdateListener = null;
    }

    public PostController(HashMap<String, Users> alluserdata) {
        this.allUsersData = alluserdata;
        this.firestore = FirebaseFirestore.getInstance();
        getAllPostCompleteListener = null;
        getPostCompleteListener = null;
        postLikeUpdateListener = null;
    }

    public PostController updatePostLikeCount(final String postid, final String userid, final boolean status) {
        getPost(postid).addOnGetPostCompleteListener(new OnGetPostCompleteListener() {
            @Override
            public void onGetPost(final Posts post) {
                firestore.collection(CollectionNames.POSTS).document(postid)
                        .update(Posts.POST_LIKES, status ? FieldValue.arrayUnion(userid) : FieldValue.arrayRemove(userid))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if (postLikeUpdateListener != null)
                                        postLikeUpdateListener.onPostLikeUpdate(
                                                status ?
                                                        post.getPostLikes().size() + 1 :
                                                        post.getPostLikes().size() - 1
                                        );
                                }
                            }
                        });

                firestore.collection(CollectionNames.USERS)
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .update(Users.FAV_POSTS, status ? FieldValue.arrayUnion(postid) : FieldValue.arrayRemove(postid));


            }
        });

        return this;
    }

    public PostController getPost(String id) {
        firestore.collection(CollectionNames.POSTS).document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();

                        Posts post = new Posts();
                        post._setPostId(doc.getId());
                        post.setPost(doc.getString(Posts.POST));
                        post.setPostType(doc.getString(Posts.POST_TYPE));
                        post.setPostTitle(doc.getString(Posts.POST_TITLE));
                        post.setPostUser(doc.getString(Posts.POST_USER));
                        post.setPostImage(doc.getString(Posts.POST_IMAGE));
                        post.setPostTimestamp(doc.getTimestamp(Posts.POST_TIMESTAMP));
                        post.setUserId(doc.getString(Users.USER_ID));
                        post.setPostLikes((List<String>) doc.get(Posts.POST_LIKES));
                        post.setPostComments(Integer.parseInt(doc.get(Posts.POST_COMMENTS).toString()));

                        if (getPostCompleteListener != null)
                            getPostCompleteListener.onGetPost(post);
                    }
                });

        return this;
    }

    public PostController getAllPosts() {
        firestore.collection(CollectionNames.POSTS)
                .orderBy(Posts.POST_TIMESTAMP, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<Posts> posts = new ArrayList<>();

                            String logged_in_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Users logged_in_user = allUsersData.get(logged_in_uid);

                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                boolean isPrivate = false;
                                if (allUsersData.get(doc.getString(Users.USER_ID)) != null)
                                     isPrivate = allUsersData.get(doc.getString(Users.USER_ID)).isPrivateProfile();


                                if (logged_in_uid.equals(doc.getString(Users.USER_ID)) || !isPrivate || (isPrivate && logged_in_user.getFollowingUsers().contains(doc.getString(Users.USER_ID)))) {
                                    Posts post = new Posts();
                                    post._setPostId(doc.getId());
                                    post.setPost(doc.getString(Posts.POST));
                                    post.setPostType(doc.getString(Posts.POST_TYPE));
                                    post.setPostTitle(doc.getString(Posts.POST_TITLE));
                                    post.setPostUser(doc.getString(Posts.POST_USER));
                                    post.setPostImage(doc.getString(Posts.POST_IMAGE));
                                    post.setPostTimestamp(doc.getTimestamp(Posts.POST_TIMESTAMP));
                                    post.setUserId(doc.getString(Users.USER_ID));
                                    post.setPostLikes((List<String>) doc.get(Posts.POST_LIKES));
                                    post.setPostComments(Integer.parseInt(doc.get(Posts.POST_COMMENTS).toString()));

                                    posts.add(post);
                                }
                            }

                            if (getAllPostCompleteListener != null)
                                getAllPostCompleteListener.onGetAllPost(posts);
                        }

                    }
                });

        return this;
    }


    public void setAllPosts(List<Posts> allPosts) {
        this.allPosts = allPosts;
    }

    public void setPost(Posts post) {
        this.post = post;
    }

    public void addOnGetAllPostCompleteListener(OnGetAllPostCompleteListener getAllPostCompleteListener) {
        this.getAllPostCompleteListener = getAllPostCompleteListener;
    }

    public void addOnGetPostCompleteListener(OnGetPostCompleteListener getPostCompleteListener) {
        this.getPostCompleteListener = getPostCompleteListener;
    }

    public void addOnPostLikeUpdateListener(OnPostLikeUpdateListener postLikeUpdateListener) {
        this.postLikeUpdateListener = postLikeUpdateListener;
    }
}
