package com.example.quotebox.models;


import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Posts {

    public static final String QUOTE_TYPE_POST = "Quote";
    public static final String POEM_TYPE_POST = "Poem";
    public static final String STORY_TYPE_POST = "Story";

    // field names
    public static final String POST_TITLE = "postTitle";
    public static final String POST_ID = "postId";
    public static final String POST_USER = "postUser";
    public static final String POST = "post";
    public static final String POST_IMAGE = "postImage";
    public static final String POST_TYPE = "postType";
    public static final String USER_ID = "userId";
    public static final String POST_LIKES = "postLikes";
    public static final String POST_COMMENTS = "postComments";
    public static final String POST_TIMESTAMP = "postTimestamp";

    private String postId;
    private String postTitle;
    private String postUser;
    private String post;
    private String postImage;
    private String postType;
    private String userId;
    private List<String> postLikes;
    private int postComments;
    private Timestamp postTimestamp;

    public Posts() {}

    public Posts(String postTitle,
                 String post,
                 String postImage,
                 String postType,
                 String userId,
                 String postUser,
                 List<String> postLikes,
                 int postComments) {
        this.postTitle = postTitle;
        this.post = post;
        this.postImage = postImage;
        this.postType = postType;
        this.userId = userId;
        this.postUser = postUser;
        this.postLikes = postLikes;
        this.postComments = postComments;
    }


    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(List<String> postLikes) {
        this.postLikes = postLikes;
    }

    public int getPostComments() {
        return postComments;
    }

    public void setPostComments(int postComments) {
        this.postComments = postComments;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostUser() {
        return postUser;
    }

    public void setPostUser(String postUser) {
        this.postUser = postUser;
    }

    public Timestamp getPostTimestamp() {
        return postTimestamp;
    }

    public void setPostTimestamp(Timestamp postTimestamp) {
        this.postTimestamp = postTimestamp;
    }
}
