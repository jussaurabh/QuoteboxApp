package com.example.quotebox.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Posts {

    public static final String QUOTE_TYPE_POST = "quote";
    public static final String POEM_TYPE_POST = "poem";
    public static final String STORY_TYPE_POST = "story";

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

    private String postId;
    private String postTitle;
    private String postUser;
    private String post;
    private String postImage;
    private String postType;
    private String userId;
    private int postLikes;
    private int postComments;
    private List<Posts> allPostsList = new ArrayList<>();

    public Posts() {}

    public Posts(String postTitle,
                 String post,
                 String postImage,
                 String postType,
                 String userId,
                 String postUser,
                 int postLikes,
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

//    public Map<String, Object> getPostsCreds() {
//        Map<String, Object> postCreds = new HashMap<>();
//        postCreds.put(POST_TITLE, postTitle);
//        postCreds.put(POST, post);
//        postCreds.put(POST_IMAGE, postImage);
//        postCreds.put(POST_TYPE, postType);
//        postCreds.put(USER_ID, userId);
//        postCreds.put(POST_LIKES, postLikes);
//        postCreds.put(POST_COMMENTS, postComments);
//
//        return postCreds;
//    }


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

    public int getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(int postLikes) {
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

    public void setAllPostsList(List<Posts> allPostsList) {
        this.allPostsList = allPostsList;
    }

    public boolean isPostsListEmpty() {
        if (allPostsList.size() > 0) return false;

        return true;
    }

    public String getPostUser() {
        return postUser;
    }

    public void setPostUser(String postUser) {
        this.postUser = postUser;
    }
}
