package com.example.quotebox.adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.R;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    Context context;
    List<Posts> postsList;

    SharedPreferencesConfig preferencesConfig;

    TextView testText;
    RecyclerView collectionNamesRL;
    Dialog collectionNamesDialog;
    Button selectedCollectionSubmitBtn, createNewCollectionBtn;

    public PostsAdapter(Context context, List<Posts> posts) {
        this.context = context;
        this.postsList = posts;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        preferencesConfig = new SharedPreferencesConfig(this.context);
//        Log.d("PA_LOG", preferencesConfig.getAllUserCreds().toString());
        Log.d("PA_ALL_POSTS", postsList.toString());
        return new PostsViewHolder(LayoutInflater.from(this.context).inflate(R.layout.card_posts, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, final int position) {

        if (postsList.get(position).getPostImage() != null) {
            holder.cardPostImageIV.setVisibility(View.VISIBLE);
            Picasso.get().load(postsList.get(position).getPostImage()).into(holder.cardPostImageIV);
        }

        holder.authorPostTV.setText(postsList.get(position).getPost());
        holder.authorPostTitleTV.setText(postsList.get(position).getPostTitle());
        holder.authorUsernameTV.setText("@"+ postsList.get(position).getPostUser());
        holder.authorNameTV.setText("- " + postsList.get(position).getPostUser());
        holder.commentCountTV.setText(Integer.toString(postsList.get(position).getPostComments()));
        holder.favoriteCountTV.setText(Integer.toString(postsList.get(position).getPostLikes()));

        HashMap<String, Users> data = preferencesConfig.getAllUserCreds();

        if (data.get(postsList.get(position).getUserId()).getUserAvatar() != null) {
            Picasso.get().load(data.get(postsList.get(position).getUserId()).getUserAvatar())
                    .transform(new ImageCircleTransform())
                    .into(holder.authorAvatarIV);
        }


        holder.collectionAddImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collectionNamesDialog = new Dialog(context);
                collectionNamesDialog.setContentView(R.layout.collection_names_dialog);
                collectionNamesDialog.getWindow().setGravity(Gravity.BOTTOM);
                collectionNamesDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                selectedCollectionSubmitBtn = collectionNamesDialog.findViewById(R.id.selectedCollectionSubmitBtn);
                createNewCollectionBtn = collectionNamesDialog.findViewById(R.id.createNewCollectionBtn);
                testText = collectionNamesDialog.findViewById(R.id.testText);

                testText.setText(postsList.get(position).getPost());

                selectedCollectionSubmitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        collectionNamesDialog.cancel();
                    }
                });

                collectionNamesDialog.show();
            }
        });
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
