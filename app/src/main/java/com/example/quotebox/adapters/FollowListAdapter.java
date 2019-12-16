package com.example.quotebox.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.ProfileActivity;
import com.example.quotebox.R;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.FollowListViewHolder> {

    List<Users> followUserList;
    Context context;
    String LOGGED_IN_USER;
    GlobalClass globalClass;
    WriteBatch batch;
    FirebaseFirestore firestore;
    String LOGGED_IN_UID;

    public FollowListAdapter(Context context, List<Users> users, String logged_in_user) {
        this.followUserList = users;
        this.context = context;
        this.LOGGED_IN_USER = logged_in_user;
        this.globalClass = (GlobalClass) context.getApplicationContext();
        this.firestore = FirebaseFirestore.getInstance();
        this.batch = firestore.batch();
        this.LOGGED_IN_UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    @NonNull
    @Override
    public FollowListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FollowListViewHolder(LayoutInflater.from(this.context).inflate(R.layout.follow_list_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FollowListViewHolder holder, final int position) {
        // user who is being followed
        final DocumentReference followingDoc = firestore.collection(CollectionNames.USERS).document(followUserList.get(position)._getUserId());
        // user who is gonna follow
        final DocumentReference followerDoc = firestore.collection(CollectionNames.USERS).document(LOGGED_IN_UID);

        holder.followAuthornameTV.setText(followUserList.get(position).getUsername());
        holder.followUsernameTV.setText(followUserList.get(position).getUsername());

        if (followUserList.get(position)._getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.listFollowBtn.setVisibility(View.GONE);
        }

        if (followUserList.get(position).getUserAvatar() != null) {
            Picasso.get().load(followUserList.get(position).getUserAvatar())
                    .transform(new ImageCircleTransform())
                    .into(holder.avatarImageView);
        }

        if (followUserList.get(position).getFollowerUsers().contains(LOGGED_IN_USER)) {
            holder.listFollowingBtn.setVisibility(View.VISIBLE);
            holder.listFollowBtn.setVisibility(View.GONE);
        }

        holder.followUsernameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ProfileActivity.class).putExtra(Users.USER_ID, followUserList.get(position)._getUserId()));
            }
        });

        holder.listFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.listFollowBtn.setVisibility(View.GONE);
                holder.followListFollowBtnProgressBar.setVisibility(View.VISIBLE);

                // if the account is private
                if (globalClass.getAllUsersData().get(followUserList.get(position)._getUserId()).isPrivateProfile()) {
                    batch.update(followerDoc, Users.FOLLOW_REQUEST_SENT, FieldValue.arrayUnion(followUserList.get(position)._getUserId()));
                    batch.update(followingDoc, Users.FOLLOW_REQUEST_RECEIVED, FieldValue.arrayUnion(LOGGED_IN_UID));

                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            holder.followListFollowBtnProgressBar.setVisibility(View.GONE);
                            holder.listFollowRequestTV.setVisibility(View.VISIBLE);

                            globalClass.getAllUsersData().get(LOGGED_IN_UID)
                                    .getFollowRequestSent().add(followUserList.get(position)._getUserId());

                            globalClass.getAllUsersData().get(followUserList.get(position)._getUserId())
                                    .getFollowRequestReceived().add(LOGGED_IN_UID);
                        }
                    });
                }
                else {
                    batch.update(followerDoc, Users.FOLLOWING_USERS, FieldValue.arrayUnion(followUserList.get(position)._getUserId()));
                    batch.update(followingDoc, Users.FOLLOWER_USERS, FieldValue.arrayUnion(LOGGED_IN_UID));

                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            holder.followListFollowBtnProgressBar.setVisibility(View.GONE);
                            holder.listFollowingBtn.setVisibility(View.VISIBLE);

                            globalClass.getAllUsersData().get(LOGGED_IN_UID).getFollowingUsers()
                                    .add(followUserList.get(position)._getUserId());

                            globalClass.getAllUsersData().get(followUserList.get(position)._getUserId())
                                    .getFollowerUsers().add(LOGGED_IN_UID);
                        }
                    });
                }
            }
        });

        holder.listFollowingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.listFollowingBtn.setVisibility(View.GONE);
                holder.followListFollowBtnProgressBar.setVisibility(View.GONE);

                batch.update(followerDoc, Users.FOLLOWING_USERS, FieldValue.arrayRemove(followUserList.get(position)._getUserId()));
                batch.update(followingDoc, Users.FOLLOWER_USERS, FieldValue.arrayRemove(LOGGED_IN_UID));

                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        holder.followListFollowBtnProgressBar.setVisibility(View.GONE);
                        holder.listFollowBtn.setVisibility(View.VISIBLE);

                        globalClass.getAllUsersData().get(LOGGED_IN_UID).getFollowingUsers()
                                .remove(followUserList.get(position)._getUserId());

                        globalClass.getAllUsersData().get(followUserList.get(position)._getUserId())
                                .getFollowerUsers().remove(LOGGED_IN_UID);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return followUserList.size();
    }

    class FollowListViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarImageView;
        TextView followUsernameTV, followAuthornameTV, listFollowRequestTV;
        Button listFollowBtn, listFollowingBtn;
        ProgressBar followListFollowBtnProgressBar;

        public FollowListViewHolder(@NonNull View iv) {
            super(iv);

            avatarImageView= iv.findViewById(R.id.avatarImageView);
            followUsernameTV = iv.findViewById(R.id.followUsernameTV);
            followAuthornameTV = iv.findViewById(R.id.followAuthornameTV);
            listFollowBtn = iv.findViewById(R.id.listFollowBtn);
            listFollowingBtn = iv.findViewById(R.id.listFollowingBtn);
            listFollowRequestTV = iv.findViewById(R.id.listFollowRequestTV);
            followListFollowBtnProgressBar = iv.findViewById(R.id.followListFollowBtnProgressBar);
        }
    }
}
