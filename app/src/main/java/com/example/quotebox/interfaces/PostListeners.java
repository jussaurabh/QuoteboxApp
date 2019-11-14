package com.example.quotebox.interfaces;

import com.example.quotebox.models.Posts;

import java.util.List;

public class PostListeners {

    public interface OnGetAllPostCompleteListener {
        void onGetAllPost(List<Posts> postsList);
    }

    public interface OnGetPostCompleteListener {
        void onGetPost(Posts post);
    }

    public interface OnPostLikeUpdateListener {
        void onPostLikeUpdate(int postLikeCount);
    }


}
