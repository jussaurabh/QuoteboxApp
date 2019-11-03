package com.example.quotebox.interfaces;

import com.example.quotebox.models.Posts;

import java.util.List;

public interface StoryPostsListener {
    void setUserStoryPosts(List<Posts> p);
}
