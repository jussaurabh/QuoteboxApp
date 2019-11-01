package com.example.quotebox.interfaces;

import com.example.quotebox.models.Posts;

import java.util.List;

public interface HomeFragmentListener {
    void onHomeFragmentDataLoad(List<Posts> postsList);
}
