package com.example.quotebox.globals;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.interfaces.PoemPostsListener;
import com.example.quotebox.interfaces.QuotePostsListener;
import com.example.quotebox.interfaces.StoryPostsListener;
import com.example.quotebox.models.Comments;
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

    private Users loggedInUserData;
    private List<Posts> quotepostdata, poempostdata, storypostdata;
    private HashMap<String, Comments> allComments = new HashMap<>();
    private HashMap<String, Users> allUsers = new HashMap<>();
    HashMap<String, Posts> allPosts = new HashMap<>();

    private FirebaseFirestore firestore;

    public void setAllPosts(HashMap<String, Posts> p) {
        this.allPosts = p;
    }

    public HashMap<String, Posts> getAllPosts() {
        return allPosts;
    }

    public Users getLoggedInUserData() {
        return loggedInUserData;
    }

    public void setLoggedInUserData(Users loggedInUserData) {
        this.loggedInUserData = loggedInUserData;
    }

    public void setSelectedUserPosts(String userid) {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection(CollectionNames.POSTS)
                .whereEqualTo(Users.USER_ID, userid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        quotepostdata = new ArrayList<>();
                        poempostdata = new ArrayList<>();
                        storypostdata = new ArrayList<>();
                        for (QueryDocumentSnapshot docs : task.getResult()) {
                            Posts post = new Posts();
                            post._setPostId(docs.getId());
                            post.setPost(docs.getString(Posts.POST));
                            post.setPostImage(docs.getString(Posts.POST_IMAGE));
                            post.setPostType(docs.getString(Posts.POST_TYPE));
                            post.setPostTitle(docs.getString(Posts.POST_TITLE));
                            post.setUserId(docs.getString(Users.USER_ID));
                            post.setPostUser(docs.getString(Posts.POST_USER));
                            post.setPostLikes((List<String>) docs.get(Posts.POST_LIKES));
                            post.setPostComments(Integer.parseInt(docs.get(Posts.POST_COMMENTS).toString()));
                            post.setPostTimestamp(docs.getTimestamp(Posts.POST_TIMESTAMP));

                            if (docs.getString(Posts.POST_TYPE).equals(Posts.QUOTE_TYPE_POST)) quotepostdata.add(post);
                            if (docs.getString(Posts.POST_TYPE).equals(Posts.POEM_TYPE_POST)) poempostdata.add(post);
                            if (docs.getString(Posts.POST_TYPE).equals(Posts.STORY_TYPE_POST)) storypostdata.add(post);

                        }

                    }
                });
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
        firestore.collection(CollectionNames.USERS).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Users user = new Users();

                            user.setUsername(doc.getString(Users.USERNAME));
                            user.setUserAvatar(doc.getString(Users.USERAVATAR));
                            user.setFavPosts((List<String>) doc.get(Users.FAV_POSTS));
                            user.setNoOfStoryPosted(Integer.parseInt(doc.get(Users.NO_OF_STORY_POSTED).toString()));
                            user.setNoOfPoemPosted(Integer.parseInt(doc.get(Users.NO_OF_POEM_POSTED).toString()));
                            user.setNoOfQuotesPosted(Integer.parseInt(doc.get(Users.NO_OF_QUOTES_POSTED).toString()));
                            user.setFollowingUsers((List<String>) doc.get(Users.FOLLOWING_USERS));
                            user.setFollowerUsers((List<String>) doc.get(Users.FOLLOWER_USERS));
                            user.setUserPostCollections((HashMap<String, List<String>>) doc.get(Users.USER_POST_COLLECTIONS));
                            user.setPrivateProfile(doc.getBoolean(Users.IS_PRIVATE_PROFILE));

                            allUsers.put(doc.getId(), user);
                        }

                        Log.d("GLOBAL_LOG", new Gson().toJson(allUsers));
                    }
                });
    }

    public HashMap<String, Users> getAllUsersData() {
        return allUsers;
    }

    public HashMap<String, Comments> getAllComments() {
        return allComments;
    }

    public void setAllComments(HashMap<String, Comments> allComments) {
        this.allComments = allComments;
    }

}
