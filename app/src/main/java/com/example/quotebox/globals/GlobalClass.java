package com.example.quotebox.globals;

import android.app.Application;

import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;

public class GlobalClass extends Application {

    private boolean isUserLoggedIn = false;
    private Users loggedInUserData;

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        isUserLoggedIn = userLoggedIn;
    }

    public Users getLoggedInUserData() {
        return loggedInUserData;
    }

    public void setLoggedInUserData(Users loggedInUserData) {
        this.loggedInUserData = loggedInUserData;
    }

    public int incrementPostCount(String postType) {
        switch (postType) {
            case Posts.QUOTE_TYPE_POST: {
                loggedInUserData.setNoOfQuotesPosted(loggedInUserData.getNoOfQuotesPosted() + 1);
                break;
            }
            case Posts.POEM_TYPE_POST: {
                loggedInUserData.setNoOfPoemPosted(loggedInUserData.getNoOfPoemPosted() + 1);
                break;
            }
            case Posts.STORY_TYPE_POST: {
                loggedInUserData.setNoOfStoryPosted(loggedInUserData.getNoOfStoryPosted() + 1);
                break;
            }
        }

        if (Posts.QUOTE_TYPE_POST.equals(postType)) return loggedInUserData.getNoOfQuotesPosted();
        if (Posts.POEM_TYPE_POST.equals(postType)) return loggedInUserData.getNoOfPoemPosted();
        if (Posts.STORY_TYPE_POST.equals(postType)) return loggedInUserData.getNoOfStoryPosted();

        return 0;
    }

    public int decrementPostCount(String postType) {
        switch (postType) {
            case Posts.QUOTE_TYPE_POST: {
                loggedInUserData.setNoOfQuotesPosted(loggedInUserData.getNoOfQuotesPosted() - 1);
                break;
            }
            case Posts.POEM_TYPE_POST: {
                loggedInUserData.setNoOfPoemPosted(loggedInUserData.getNoOfPoemPosted() - 1);
                break;
            }
            case Posts.STORY_TYPE_POST: {
                loggedInUserData.setNoOfStoryPosted(loggedInUserData.getNoOfStoryPosted() - 1);
                break;
            }
        }

        if (Posts.QUOTE_TYPE_POST.equals(postType)) return loggedInUserData.getNoOfQuotesPosted();
        if (Posts.POEM_TYPE_POST.equals(postType)) return loggedInUserData.getNoOfPoemPosted();
        if (Posts.STORY_TYPE_POST.equals(postType)) return loggedInUserData.getNoOfStoryPosted();

        return 0;
    }
}
