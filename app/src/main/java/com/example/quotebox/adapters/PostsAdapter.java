package com.example.quotebox.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.PostCommentsActivity;
import com.example.quotebox.ProfileActivity;
import com.example.quotebox.R;
import com.example.quotebox.controllers.PostController;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.interfaces.PostListeners;
import com.example.quotebox.models.Posts;
import com.example.quotebox.models.Users;
import com.example.quotebox.ui.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    Context context;
    List<Posts> postsList;

    FirebaseFirestore firestore;
    SharedPreferencesConfig preferencesConfig;
    GlobalClass globalClass;
//    CollectionDialogAdapter collectionDialogAdapter;
    PostController postController;

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

        firestore = FirebaseFirestore.getInstance();
        preferencesConfig = new SharedPreferencesConfig(this.context);
        globalClass = (GlobalClass) PostsAdapter.this.context.getApplicationContext();
        postController= new PostController();

        return new PostsViewHolder(LayoutInflater.from(this.context).inflate(R.layout.card_posts, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PostsViewHolder holder, final int position) {

        final HashMap<String, Users> allUsersData = globalClass.getAllUsersData();
        final Users loggedInUserData = globalClass.getLoggedInUserData();
        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String postid = postsList.get(position).getPostId();

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
                postController.updatePostLikeCount(postsList.get(position).getPostId(), FirebaseAuth.getInstance().getCurrentUser().getUid(), true)
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

                postController.updatePostLikeCount(postsList.get(position).getPostId(), FirebaseAuth.getInstance().getCurrentUser().getUid(), false)
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


        if (allUsersData.get(userid).getFavPosts().contains(postsList.get(position).getPostId())) {
            holder.favoriteImageBtn.setVisibility(View.GONE);
            holder.unFavoriteIB.setVisibility(View.VISIBLE);
        }


        holder.postCardMenuImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.postCardMenuImageBtn);
                popup.inflate(R.menu.post_card_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.report_post: {
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


    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    class PostsViewHolder extends RecyclerView.ViewHolder {

        ImageView authorAvatarIV, cardPostImageIV;
        ImageButton favoriteImageBtn, unFavoriteIB, commentImageBtn, collectionAddImageBtn, postCardMenuImageBtn;
        TextView authorPostTitleTV, authorUsernameTV, authorPostTV, authorNameTV, favoriteCountTV, commentCountTV, postDateTV;
        ProgressBar postFavBtnProgressBar;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);

            authorAvatarIV = itemView.findViewById(R.id.author_avatar);
            cardPostImageIV = itemView.findViewById(R.id.cardPostImageIV);
            favoriteImageBtn = itemView.findViewById(R.id.favoriteImageButton);
            unFavoriteIB = itemView.findViewById(R.id.unFavoriteIB);
            commentImageBtn = itemView.findViewById(R.id.commentImageButton);
            collectionAddImageBtn = itemView.findViewById(R.id.collectionAddImageButton);
            authorPostTitleTV = itemView.findViewById(R.id.author_post_title_name);
            authorUsernameTV = itemView.findViewById(R.id.author_username);
            authorPostTV = itemView.findViewById(R.id.author_post);
            authorNameTV = itemView.findViewById(R.id.author_name);
            favoriteCountTV = itemView.findViewById(R.id.favoriteCountTV);
            commentCountTV = itemView.findViewById(R.id.commentCountTV);
            postDateTV = itemView.findViewById(R.id.postDateTV);
            postFavBtnProgressBar = itemView.findViewById(R.id.postFavBtnPB);
            postCardMenuImageBtn = itemView.findViewById(R.id.postCardMenuImageBtn);

            collectionAddImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    collectionNamesDialog = new Dialog(view.getContext());
                    collectionNamesDialog.setContentView(R.layout.collection_names_dialog);
                    collectionNamesDialog.getWindow().setGravity(Gravity.BOTTOM);
                    collectionNamesDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                    selectedCollectionSubmitBtn = collectionNamesDialog.findViewById(R.id.selectedCollectionSubmitBtn);
                    createNewCollectionBtn = collectionNamesDialog.findViewById(R.id.createNewCollectionBtn);
                    collectionNamesRL = collectionNamesDialog.findViewById(R.id.collectionNamesRL);
                    collectionNamesRL.setHasFixedSize(true);
                    collectionNamesRL.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    collectionNamesDialog.show();

                    List<String> pcl = new ArrayList<>();
                    pcl.add(0, "col1");
                    pcl.add(1, "col2");
                    pcl.add(2, "col3");

                    Log.d("POST_ADAP", "collec name : " + pcl.toString());


//                    collectionDialogAdapter = new CollectionDialogAdapter(pcl, this);
//                    collectionNamesRL.setAdapter(collectionNamesAdapter);

//                    selectedCollectionSubmitBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            collectionNamesDialog.cancel();
//                        }
//                    });

                }
            });




        }
    }
}
