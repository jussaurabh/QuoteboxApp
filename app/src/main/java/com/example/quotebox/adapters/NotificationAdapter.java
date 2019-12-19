package com.example.quotebox.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.ProfileActivity;
import com.example.quotebox.R;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.models.Notifications;
import com.example.quotebox.models.Users;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<Notifications> notifyList;
    private HashMap<String, Users> allUserData;

    public NotificationAdapter(Context c, List<Notifications> nl, HashMap<String, Users> usr) {
        this.context = c;
        this.notifyList = nl;
        this.allUserData = usr;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(this.context).inflate(R.layout.notify_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        final String UID_NOTIFIER = notifyList.get(position).getNotifierId();

        if (allUserData.get(UID_NOTIFIER).getUserAvatar() != null) {
            Picasso.get().load(allUserData.get(UID_NOTIFIER).getUserAvatar())
                    .transform(new ImageCircleTransform())
                    .into(holder.notifyUserAvatarIV);
        }

        holder.notifyUsernameTV.setText(allUserData.get(UID_NOTIFIER).getUsername());
        holder.notifyContentTV.setText(notifyList.get(position).getNotifyContent());
        holder.notifyDateTV.setText(notifyList.get(position)._getNotifyDate());
        holder.notifyTimeTV.setText(notifyList.get(position)._getNotifyTime());

        holder.notifyListWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ProfileActivity.class).putExtra(Users.USER_ID, UID_NOTIFIER));
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifyList.size();
    }


    class NotificationViewHolder extends RecyclerView.ViewHolder {

        ImageView notifyUserAvatarIV;
        TextView notifyUsernameTV, notifyContentTV, notifyDateTV, notifyTimeTV;
        LinearLayout notifyListWrapper;

        public NotificationViewHolder(@NonNull View iv) {
            super(iv);

            notifyUserAvatarIV = iv.findViewById(R.id.notifyUserAvatarIV);
            notifyUsernameTV = iv.findViewById(R.id.notifyUsernameTV);
            notifyContentTV = iv.findViewById(R.id.notifyContentTV);
            notifyDateTV = iv.findViewById(R.id.notifyDateTV);
            notifyTimeTV = iv.findViewById(R.id.notifyTimeTV);
            notifyListWrapper = iv.findViewById(R.id.notifyListWrapper);
        }
    }
}
