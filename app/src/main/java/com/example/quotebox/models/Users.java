package com.example.quotebox.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Users {

    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String USERAVATAR = "userAvatar";
    public static final String USERABOUTME = "userAboutMe";
    public static final String USERDOB = "userDOB";
    public static final String QUOTEPOSTLIKES = "quotePostLikes";
    public static final String NO_OF_QUOTES_POSTED = "noOfQuotesPosted";
    public static final String NO_OF_POEM_POSTED = "noOfPoemPosted";
    public static final String NO_OF_STORY_POSTED = "noOfStoryPosted";


    private String username;
    private String email;
    private String password;
    private String userAvatar;
    private String userAboutMe;
    private Date userDOB;
    private int quotePostLikes;
    private int noOfQuotesPosted;
    private int noOfPoemPosted;
    private int noOfStoryPosted;

    public Users() {

    }

    public Users(String username,
                 String email,
                 String password,
                 String userAvatar,
                 String userAboutMe,
                 Date userDOB,
                 int quotePostLikes,
                 int noOfQuotesPosted,
                 int noOfPoemPosted,
                 int noOfStoryPosted) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userAvatar = userAvatar;
        this.userAboutMe = userAboutMe;
        this.userDOB = userDOB;
        this.quotePostLikes = quotePostLikes;
        this.noOfQuotesPosted = noOfQuotesPosted;
        this.noOfPoemPosted = noOfPoemPosted;
        this.noOfStoryPosted = noOfStoryPosted;
    }
    
//    public Map<String,Object> getUsersCreds() {
//        Map<String, Object> userData = new HashMap<>();
//        userData.put(USERNAME, username);
//        userData.put(EMAIL, email);
//        userData.put(PASSWORD, password);
//        userData.put(USERAVATAR, userAvatar);
//        userData.put(USERABOUTME, userAboutMe);
//        userData.put(USERDOB, userDOB);
//        userData.put(QUOTEPOSTLIKES, quotePostLikes);
//        userData.put(NO_OF_QUOTES_POSTED, noOfQuotesPosted);
//
//        return userData;
//    }

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

    public int getQuotePostLikes() {
        return quotePostLikes;
    }

    public void setQuotePostLikes(int quotePostLikes) {
        this.quotePostLikes = quotePostLikes;
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
}
