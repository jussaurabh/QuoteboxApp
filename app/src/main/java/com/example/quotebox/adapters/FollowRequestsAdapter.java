package com.example.quotebox.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.FollowRequestActivity;
import com.example.quotebox.ProfileActivity;
import com.example.quotebox.R;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.models.Users;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FollowRequestsAdapter extends RecyclerView.Adapter<FollowRequestsAdapter.FollowRequestViewHolder> {

    private FollowRequestActivity context;
    private List<Users> usersList;

    public FollowRequestsAdapter(FollowRequestActivity c, List<Users> users) {
        this.context = c;
        this.usersList = users;
    }

    @NonNull
    @Override
    public FollowRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FollowRequestViewHolder(LayoutInflater.from(this.context).inflate(R.layout.notify_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FollowRequestViewHolder holder, final int position) {
        holder.notifyUsernameTV.setText(usersList.get(position).getUsername());
        holder.notifyContentTV.setText("@" + usersList.get(position).getUsername());

        if (usersList.get(position).getUserAvatar() != null) {
            Picasso.get().load(usersList.get(position).getUserAvatar()).transform(new ImageCircleTransform())
                    .into(holder.notifyUserAvatarIV);
        }

        holder.notifyUsernameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ProfileActivity.class).putExtra(Users.USER_ID, usersList.get(position)._getUserId()));
            }
        });

        holder.notifyConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.confirmFollowRequest(usersList.get(position)._getUserId(), position);
            }
        });

        holder.notifyDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.deleteFollowRequest(usersList.get(position)._getUserId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class FollowRequestViewHolder extends RecyclerView.ViewHolder {

        ImageView notifyUserAvatarIV;
        TextView notifyUsernameTV, notifyContentTV, notifyDateTV, notifyTimeTV;
        Button notifyConfirmBtn, notifyDeleteBtn;
        LinearLayout notifyDateTimeWrapperLL;

        public FollowRequestViewHolder(@NonNull View iv) {
            super(iv);

            notifyUserAvatarIV = iv.findViewById(R.id.notifyUserAvatarIV);
            notifyUsernameTV = iv.findViewById(R.id.notifyUsernameTV);
            notifyContentTV = iv.findViewById(R.id.notifyContentTV);
            notifyDateTV = iv.findViewById(R.id.notifyDateTV);
            notifyTimeTV = iv.findViewById(R.id.notifyTimeTV);
            notifyConfirmBtn = iv.findViewById(R.id.notifyConfirmBtn);
            notifyDeleteBtn = iv.findViewById(R.id.notifyDeleteBtn);
            notifyDateTimeWrapperLL = iv.findViewById(R.id.notifyDateTimeWrapperLL);

            notifyConfirmBtn.setVisibility(View.VISIBLE);
            notifyDeleteBtn.setVisibility(View.VISIBLE);
            notifyDateTimeWrapperLL.setVisibility(View.GONE);
        }
    }
}
