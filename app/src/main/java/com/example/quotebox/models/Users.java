package com.example.quotebox.models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Users {

    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String USERAVATAR = "userAvatar";
    public static final String USER_ABOUT_ME = "userAboutMe";
    public static final String USER_DOB = "userDOB";
    public static final String NO_OF_QUOTES_POSTED = "noOfQuotesPosted";
    public static final String NO_OF_POEM_POSTED = "noOfPoemPosted";
    public static final String NO_OF_STORY_POSTED = "noOfStoryPosted";
    public static final String FAV_POSTS = "favPosts";
    public static final String FOLLOWER_USERS = "followerUsers";
    public static final String FOLLOWING_USERS = "followingUsers";
    public static final String IS_PRIVATE_PROFILE = "privateProfile";

    public static final String USER_POST_COLLECTIONS = "userPostCollections";
    public static final String DEFAULT_POST_COLLECTION = "Favorites";
    public static final String COLLECTION_NAME = "collName";
    public static final String POSTS_IN_COLLECTION = "postCountInCollection";


    private String username;
    private String email;
    private String password;
    private String userAvatar;
    private String userAboutMe;
    private Date userDOB;
    private List<String> favPosts;
    private int noOfQuotesPosted;
    private int noOfPoemPosted;
    private int noOfStoryPosted;
    private List<String> followerUsers;
    private List<String> followingUsers;
    private HashMap<String, List<String>> userPostCollections;
    private boolean isPrivateProfile;

    public Users() { }

    public Users(String username,
                 String email,
                 String password,
                 String userAvatar,
                 String userAboutMe,
                 Date userDOB,
                 List<String> favPosts,
                 int noOfQuotesPosted,
                 int noOfPoemPosted,
                 int noOfStoryPosted,
                 List<String> followerUsers,
                 List<String> followingUsers,
                 HashMap<String, List<String>> userPostCollections,
                 boolean isprivate) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userAvatar = userAvatar;
        this.userAboutMe = userAboutMe;
        this.userDOB = userDOB;
        this.favPosts = favPosts;
        this.noOfQuotesPosted = noOfQuotesPosted;
        this.noOfPoemPosted = noOfPoemPosted;
        this.noOfStoryPosted = noOfStoryPosted;
        this.followerUsers = followerUsers;
        this.followingUsers = followingUsers;
        this.userPostCollections = userPostCollections;
        this.isPrivateProfile = isprivate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserAboutMe() {
        return userAboutMe;
    }

    public void setUserAboutMe(String userAboutMe) {
        this.userAboutMe = userAboutMe;
    }

    public Date getUserDOB() {
        return userDOB;
    }

    public void setUserDOB(Date userDOB) {
        this.userDOB = userDOB;
    }

    public int getNoOfQuotesPosted() {
        return noOfQuotesPosted;
    }

    public void setNoOfQuotesPosted(int noOfQuotesPosted) {
        this.noOfQuotesPosted = noOfQuotesPosted;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNoOfPoemPosted() {
        return noOfPoemPosted;
    }

    public void setNoOfPoemPosted(int noOfPoemPosted) {
        this.noOfPoemPosted = noOfPoemPosted;
    }

    public int getNoOfStoryPosted() {
        return noOfStoryPosted;
    }

    public void setNoOfStoryPosted(int noOfStoryPosted) {
        this.noOfStoryPosted = noOfStoryPosted;
    }

    public List<String> getFavPosts() {
        return favPosts;
    }

    public void setFavPosts(List<String> favPosts) {
        this.favPosts = favPosts;
    }

    public List<String> getFollowerUsers() {
        return followerUsers;
    }

    public void setFollowerUsers(List<String> followerUsers) {
        this.followerUsers = followerUsers;
    }

    public List<String> getFollowingUsers() {
        return followingUsers;
    }

    public void setFollowingUsers(List<String> followingUsers) {
        this.followingUsers = followingUsers;
    }

    public HashMap<String, List<String>> getUserPostCollections() {
        return userPostCollections;
    }

    public void setUserPostCollections(HashMap<String, List<String>> userPostCollections) {
        this.userPostCollections = userPostCollections;
    }

    public boolean isPrivateProfile() {
        return isPrivateProfile;
    }

    public void setPrivateProfile(boolean privateProfile) {
        isPrivateProfile = privateProfile;
    }

//    public boolean isProfilePrivate() {
//        return isProfilePrivate;
//    }
//
//    public void setProfilePrivate(boolean profilePrivate) {
//        isProfilePrivate = profilePrivate;
//    }
}
