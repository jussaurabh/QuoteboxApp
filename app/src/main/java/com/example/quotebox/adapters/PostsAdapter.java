package com.example.quotebox.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.PostCommentsActivity;
import com.example.quotebox.ProfileActivity;
import com.example.quotebox.R;
import com.example.quotebox.controllers.PostController;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.interfaces.PostListeners;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.example.quotebox.ui.PostCollectionListDialogFragment;
import com.example.quotebox.ui.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    Context context;
    List<Posts> postsList;

    FirebaseFirestore firestore;
    WriteBatch batch;
    SharedPreferencesConfig preferencesConfig;
    GlobalClass globalClass;
    PostController postController;
    PostCollectionListDialogFragment postCollectionListDialogFragment;
    FragmentManager fragmentManager;

    public PostsAdapter(Context context, List<Posts> posts) {
        this.context = context;
        this.postsList = posts;
    }

//    public PostsAdapter(List<Posts> pl) {
//        this.postsList = pl;
//    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        firestore = FirebaseFirestore.getInstance();
        batch = firestore.batch();
        preferencesConfig = new SharedPreferencesConfig(this.context);
        globalClass = (GlobalClass) PostsAdapter.this.context.getApplicationContext();
        postController= new PostController(globalClass.getAllUsersData());

        return new PostsViewHolder(LayoutInflater.from(this.context).inflate(R.layout.card_posts, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PostsViewHolder holder, final int position) {

        final HashMap<String, Users> allUsersData = globalClass.getAllUsersData();
        final Users loggedInUserData = globalClass.getLoggedInUserData();
        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String postid = postsList.get(position)._getPostId();

        Date d = new Date(postsList.get(position).getPostTimestamp().getSeconds() * 1000);
        DateFormat dateFormat = new SimpleDateFormat("MMM d, ''yy");

        if (postsList.get(position).getPostImage() != null) {
            holder.cardPostImageIV.setVisibility(View.VISIBLE);
            Picasso.get().load(postsList.get(position).getPostImage()).into(holder.cardPostImageIV);
        }

        holder.authorPostTV.setText(postsList.get(position).getPost());
        holder.authorPostTitleTV.setText(postsList.get(position).getPostTitle());
        holder.authorUsernameTV.setText("@"+ postsList.get(position).getPostUser());
        holder.authorNameTV.setText("- " + postsList.get(position).getPostUser());
        holder.commentCountTV.setText(Integer.toString(postsList.get(position).getPostComments()));
        holder.favoriteCountTV.setText(Integer.toString(postsList.get(position).getPostLikes().size()));
        holder.postDateTV.setText(dateFormat.format(d));

        final String uname = holder.authorUsernameTV.getText().toString().split("@")[1];

        if (allUsersData.get(postsList.get(position).getUserId()).getUserAvatar() != null) {
            Picasso.get().load(allUsersData.get(postsList.get(position).getUserId()).getUserAvatar())
                    .transform(new ImageCircleTransform())
                    .into(holder.authorAvatarIV);
        }

        holder.authorUsernameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uname.equals(globalClass.getLoggedInUserData().getUsername())) {
                    ((FragmentActivity) PostsAdapter.this.context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_container, new ProfileFragment())
                            .commit();
                }
                else {
                    PostsAdapter.this.context
                            .startActivity(new Intent(
                                    PostsAdapter.this.context,
                                    ProfileActivity.class).putExtra(Users.USER_ID, postsList.get(position).getUserId())
                            );
                }
            }
        });

        holder.favoriteImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.postFavBtnProgressBar.setVisibility(View.VISIBLE);
                holder.favoriteImageBtn.setVisibility(View.GONE);
                postController.updatePostLikeCount(postsList.get(position)._getPostId(), userid, true)
                        .addOnPostLikeUpdateListener(new PostListeners.OnPostLikeUpdateListener() {
                            @Override
                            public void onPostLikeUpdate(int postLikeCount) {
                                holder.postFavBtnProgressBar.setVisibility(View.GONE);
                                holder.unFavoriteIB.setVisibility(View.VISIBLE);

                                holder.favoriteCountTV.setText(Integer.toString(postLikeCount));

                                allUsersData.get(userid).getFavPosts().add(postid);

                                loggedInUserData.getFavPosts().add(postid);
                            }
                        });
            }
        });

        holder.unFavoriteIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.postFavBtnProgressBar.setVisibility(View.VISIBLE);
                holder.unFavoriteIB.setVisibility(View.GONE);

                postController.updatePostLikeCount(postsList.get(position)._getPostId(), userid, false)
                        .addOnPostLikeUpdateListener(new PostListeners.OnPostLikeUpdateListener() {
                            @Override
                            public void onPostLikeUpdate(int postLikeCount) {
                                holder.postFavBtnProgressBar.setVisibility(View.GONE);
                                holder.favoriteImageBtn.setVisibility(View.VISIBLE);

                                holder.favoriteCountTV.setText(Integer.toString(postLikeCount));

                                loggedInUserData.getFavPosts().remove(postid);

                                allUsersData.get(userid).getFavPosts().remove(postid);
                            }
                        });
            }
        });


        if (allUsersData.get(userid).getFavPosts().contains(postsList.get(position)._getPostId())) {
            holder.favoriteImageBtn.setVisibility(View.GONE);
            holder.unFavoriteIB.setVisibility(View.VISIBLE);
        }


        holder.postCardMenuImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.postCardMenuImageBtn);
                popup.inflate(postsList.get(position).getUserId().equals(userid) ?
                        R.menu.edit_or_delete_menu : R.menu.post_card_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.report_post: {
                                break;
                            }
                            case R.id.delete_post: {
                                DocumentReference postDocRef = firestore.collection(CollectionNames.POSTS).document(postid);
                                batch.delete(postDocRef);

                                DocumentReference userDocRef = firestore.collection(CollectionNames.USERS).document(userid);
                                switch (postsList.get(position).getPostType()) {
                                    case Posts.QUOTE_TYPE_POST: {
                                        batch.update(userDocRef, Users.NO_OF_QUOTES_POSTED, FieldValue.increment(-1));
                                        break;
                                    }
                                    case Posts.POEM_TYPE_POST: {
                                        batch.update(userDocRef, Users.NO_OF_POEM_POSTED, FieldValue.increment(-1));
                                        break;
                                    }
                                    case Posts.STORY_TYPE_POST: {
                                        batch.update(userDocRef, Users.NO_OF_STORY_POSTED, FieldValue.increment(-1));
                                        break;
                                    }
                                }

                                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        postsList.remove(postid);
                                    }
                                });

                                break;
                            }
                        }

                        return false;
                    }
                });

                popup.show();
            }
        });


        holder.commentImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, PostCommentsActivity.class).putExtra(Posts.POST_ID, postid));
            }
        });


        for (List<String> ls : allUsersData.get(userid).getUserPostCollections().values()) {
            if (ls.contains(postid)) {
                holder.collectionAddImageBtn.setVisibility(View.GONE);
                holder.postAddedToCollectionImageBtn.setVisibility(View.VISIBLE);
                break;
            }
        }


        holder.collectionAddImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                postCollectionListDialogFragment = new PostCollectionListDialogFragment(postid);
                postCollectionListDialogFragment.show(fragmentManager, "");
            }
        });


        holder.postAddedToCollectionImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                postCollectionListDialogFragment = new PostCollectionListDialogFragment(postid);
                postCollectionListDialogFragment.show(fragmentManager, "");
            }
        });


    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }


    class PostsViewHolder extends RecyclerView.ViewHolder {

        ImageView authorAvatarIV, cardPostImageIV;
        ImageButton favoriteImageBtn, unFavoriteIB, commentImageBtn, collectionAddImageBtn, postCardMenuImageBtn, postAddedToCollectionImageBtn;
        TextView authorPostTitleTV, authorUsernameTV, authorPostTV, authorNameTV, favoriteCountTV, commentCountTV, postDateTV;
        ProgressBar postFavBtnProgressBar, postCollectionBtnProgressBar;

        public PostsViewHolder(@NonNull final View itemView) {
            super(itemView);

            authorAvatarIV = itemView.findViewById(R.id.author_avatar);
            cardPostImageIV = itemView.findViewById(R.id.cardPostImageIV);
            favoriteImageBtn = itemView.findViewById(R.id.favoriteImageButton);
            unFavoriteIB = itemView.findViewById(R.id.unFavoriteIB);
            commentImageBtn = itemView.findViewById(R.id.commentImageButton);
            collectionAddImageBtn = itemView.findViewById(R.id.collectionAddImageButton);
            postAddedToCollectionImageBtn = itemView.findViewById(R.id.postAddedToCollectionImageBtn);
            postCollectionBtnProgressBar = itemView.findViewById(R.id.postCollectionBtnProgressBar);
            authorPostTitleTV = itemView.findViewById(R.id.author_post_title_name);
            authorUsernameTV = itemView.findViewById(R.id.author_username);
            authorPostTV = itemView.findViewById(R.id.author_post);
            authorNameTV = itemView.findViewById(R.id.author_name);
            favoriteCountTV = itemView.findViewById(R.id.favoriteCountTV);
            commentCountTV = itemView.findViewById(R.id.commentCountTV);
            postDateTV = itemView.findViewById(R.id.postDateTV);
            postFavBtnProgressBar = itemView.findViewById(R.id.postFavBtnPB);
            postCardMenuImageBtn = itemView.findViewById(R.id.postCardMenuImageBtn);

        }
    }
}
