package com.example.quotebox.helpers;

public class CollectionNames {

    private String USERS = "users";
    private String POSTS = "posts";
    private String COMMENTS = "comments";
    private String POST_COLLECTIONS = "postCollections";

    public CollectionNames() {}


    public String getUserCollectionName() {
        return USERS;
    }

    public String getPostCollectionName() {
        return POSTS;
    }

    public String getCommentCollectionName() {
        return COMMENTS;
    }

    public String getPostCollectionListName() {return POST_COLLECTIONS;}
}
