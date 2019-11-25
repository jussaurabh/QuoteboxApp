package com.example.quotebox.models;

import com.google.firebase.Timestamp;

public class Comments {

    public static final String COMMENT = "comment";
    public static final String COMMENT_TIMESTAMP = "commentTimestamp";
    public static final String COMMENT_ID = "commentId";

    private String userId;
    private String userAvatar;
    private String username;
    private String comment;
    private Timestamp commentTimestamp;
    private String postId;
    private String commentId;


    public Comments() {}

    public Comments(
        String userId,
        String comment,
        Timestamp commentTimestamp,
        String postId
    ) {
        this.userId = userId;
        this.comment = comment;
        this.commentTimestamp = commentTimestamp;
        this.postId = postId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String _getCommentId() {
        return commentId;
    }

    public void _setCommentId(String _commentId) {
        this.commentId = _commentId;
    }

    public Timestamp getCommentTimestamp() {
        return commentTimestamp;
    }

    public void setCommentTimestamp(Timestamp commentTimestamp) {
        this.commentTimestamp = commentTimestamp;
    }

    public String _getUserAvatar() {
        return userAvatar;
    }

    public void _setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String _getUsername() {
        return username;
    }

    public void _setUsername(String username) {
        this.username = username;
    }
}
