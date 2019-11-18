package com.example.quotebox.interfaces;

import com.example.quotebox.models.Comments;

import java.util.List;

public class CommentEventListeners {

    public interface OnGetAllCommentsCompleteListener {
        void onGetAllCommments(List<Comments> comments);
    }

    public interface OnCommentPostListener {
        void onCommentPost();
    }

    public interface OnCommentUpdateListener {
        void onCommentUpdate();
    }
}
