package com.example.quotebox.interfaces;

import com.example.quotebox.models.Posts;

import java.util.List;

public interface QuotePostsListener {
    public void setUserQuotePosts(List<Posts> p, String s);
}
