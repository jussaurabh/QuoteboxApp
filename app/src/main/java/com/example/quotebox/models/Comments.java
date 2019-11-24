package com.example.quotebox.models;

import com.google.firebase.Timestamp;

public class Comments {

    public static final String USER_ID = "userId";
    public static final String USER_AVATAR = "userAvatar";
    public static final String USERNAME = "username";
    public static final String COMMENT = "comment";
    public static final String COMMENT_TIMESTAMP = "commentTimestamp";
    public static final String POST_ID = "postId";
    public static final String LIKES_COUNT = "likesCount";
    public static final String DISLIKES_COUNT = "dislikesCount";
    public static final String COMMENT_ID = "commentId";

    private String userId;
    private String userAvatar;
    private String username;
    private String comment;
    private Timestamp commentTimestamp;
    private String postId;
    private int likesCount;
    private int dislikesCount;
    private String commentId;


    public Comments() {}

    public Comments(
        String userId,
        String userAvatar,
        String username,
        String comment,
        Timestamp commentTimestamp,
        String postId,
        int likesCount,
        int dislikesCount
    ) {
        this.userId = userId;
        this.userAvatar = userAvatar;
        this.username = username;
        this.comment = comment;
        this.commentTimestamp = commentTimestamp;
        this.postId = postId;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String _commentId) {
        this.commentId = _commentId;
    }

    public Timestamp getCommentTimestamp() {
        return commentTimestamp;
    }

    public void setCommentTimestamp(Timestamp commentTimestamp) {
        this.commentTimestamp = commentTimestamp;
    }
}