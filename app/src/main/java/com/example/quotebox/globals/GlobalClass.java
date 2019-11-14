package com.example.quotebox.globals;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.interfaces.PoemPostsListener;
import com.example.quotebox.interfaces.QuotePostsListener;
import com.example.quotebox.interfaces.StoryPostsListener;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlobalClass extends Application {

    private QuotePostsListener quotePostsListener;
    private PoemPostsListener poemPostsListener;
    private StoryPostsListener storyPostsListener;

    private boolean isUserLoggedIn = false;
    private Users loggedInUserData;
    private List<Posts> quotepostdata, poempostdata, storypostdata;
    private HashMap<String, Users> allUsers = new HashMap<>();

    private FirebaseFirestore firestore;



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

    public void setSelectedUserPosts(String userid) {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection(new CollectionNames().getPostCollection())
                .whereEqualTo(Posts.USER_ID, userid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        quotepostdata = new ArrayList<>();
                        poempostdata = new ArrayList<>();
                        storypostdata = new ArrayList<>();
                        for (QueryDocumentSnapshot docs : task.getResult()) {
                            Posts post = new Posts();
                            post.setPostId(docs.getId());
                            post.setPost(docs.getString(Posts.POST));
                            post.setPostImage(docs.getString(Posts.POST_IMAGE));
                            post.setPostType(docs.getString(Posts.POST_TYPE));
                            post.setPostTitle(docs.getString(Posts.POST_TITLE));
                            post.setUserId(docs.getString(Posts.USER_ID));
                            post.setPostUser(docs.getString(Posts.POST_USER));
                            post.setPostLikes(Integer.parseInt(docs.get(Posts.POST_LIKES).toString()));
                            post.setPostComments(Integer.parseInt(docs.get(Posts.POST_COMMENTS).toString()));
                            post.setPostTimestamp(docs.getTimestamp(Posts.POST_TIMESTAMP));

                            if (docs.getString(Posts.POST_TYPE).equals(Posts.QUOTE_TYPE_POST)) quotepostdata.add(post);
                            if (docs.getString(Posts.POST_TYPE).equals(Posts.POEM_TYPE_POST)) poempostdata.add(post);
                            if (docs.getString(Posts.POST_TYPE).equals(Posts.STORY_TYPE_POST)) storypostdata.add(post);

                        }

                    }
                });
    }

    public List<Posts> getQuotePostData() {return quotepostdata;}

    public List<Posts> getPoemPostData() {return poempostdata;}

    public List<Posts> getStoryPostData() {
        return storypostdata;
    }

    public void passQuotePosts(QuotePostsListener qpl) {
        this.quotePostsListener = qpl;
        quotePostsListener.setUserQuotePosts(quotepostdata);
    }

    public void passPoemPosts(PoemPostsListener ppl) {
        this.poemPostsListener = ppl;
        poemPostsListener.setUserPoemPosts(poempostdata);
    }

    public void passStoryPosts(StoryPostsListener spl) {
        this.storyPostsListener = spl;
        storyPostsListener.setUserStoryPosts(storypostdata);
    }


    public void setAllUsersData() {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection(new CollectionNames().getUserCollection()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Users user = new Users();

                            user.setUsername(doc.getString(Users.USERNAME));
                            user.setUserAvatar(doc.getString(Users.USERAVATAR));
                            user.setFavPosts((List<String>) doc.get(Users.FAV_POSTS));

                            allUsers.put(doc.getId(), user);
                        }

                        Log.d("GLOBAL_LOG", new Gson().toJson(allUsers));
                    }
                });
    }

    public HashMap<String, Users> getAllUsersData() {
        return allUsers;
    }
}
