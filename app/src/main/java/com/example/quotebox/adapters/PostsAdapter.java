package com.example.quotebox.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.R;
import com.example.quotebox.models.Posts;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    Context context;
    List<Posts> postsList;

    public PostsAdapter(Context context, List<Posts> posts) {
        Log.d("POST_ADAPTER ", posts.toString());
        this.context = context;
        this.postsList = posts;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostsViewHolder(LayoutInflater.from(this.context).inflate(R.layout.card_posts, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        Log.d("POST_ADAPTER_LOG",
                "Author post: " + postsList.get(position).getPostUser());

        if (postsList.get(position).getPostImage() != null) {
            holder.cardPostImageIV.setVisibility(View.VISIBLE);
            Picasso.get().load(postsList.get(position).getPostImage()).into(holder.cardPostImageIV);
        }

        holder.authorPostTV.setText(postsList.get(position).getPost());
        holder.authorPostTitleTV.setText(postsList.get(position).getPostTitle());
        holder.authorUsernameTV.setText(postsList.get(position).getPostUser());
        holder.authorNameTV.setText("- " + postsList.get(position).getPostUser());
        holder.commentCountTV.setText(Integer.toString(postsList.get(position).getPostComments()));
        holder.favoriteCountTV.setText(Integer.toString(postsList.get(position).getPostLikes()));

//        Picasso.get().load(R.mipmap.ic_avatar_placeholder_round)
//                .transform(new ImageCircleTransform())
//                .into(holder.authorAvatarIV);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    class PostsViewHolder extends RecyclerView.ViewHolder {

        ImageView authorAvatarIV, cardPostImageIV;
        ImageButton favoriteImageButton, commentImageButton, collectionAddImageButton;
        TextView authorPostTitleTV, authorUsernameTV, authorPostTV, authorNameTV, favoriteCountTV, commentCountTV, postDateTV;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);

            authorAvatarIV = itemView.findViewById(R.id.author_avatar);
            cardPostImageIV = itemView.findViewById(R.id.cardPostImageIV);
            favoriteImageButton = itemView.findViewById(R.id.favoriteImageButton);
            commentImageButton = itemView.findViewById(R.id.commentImageButton);
            collectionAddImageButton = itemView.findViewById(R.id.collectionAddImageButton);
            authorPostTitleTV = itemView.findViewById(R.id.author_post_title_name);
            authorUsernameTV = itemView.findViewById(R.id.author_username);
            authorPostTV = itemView.findViewById(R.id.author_post);
            authorNameTV = itemView.findViewById(R.id.author_name);
            favoriteCountTV = itemView.findViewById(R.id.favoriteCountTV);
            commentCountTV = itemView.findViewById(R.id.commentCountTV);
            postDateTV = itemView.findViewById(R.id.postDateTV);
        }
    }
}
