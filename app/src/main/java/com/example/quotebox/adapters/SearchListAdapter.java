package com.example.quotebox.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.ProfileActivity;
import com.example.quotebox.R;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.models.Users;
import com.example.quotebox.ui.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder> implements Filterable {

    List<Users> usersList;
    List<Users> allUsersList;
    Context context;

    public SearchListAdapter(Context c, List<Users> users) {
        this.context = c;
        this.usersList = users;
        this.allUsersList = new ArrayList<>(users);
    }

    @NonNull
    @Override
    public SearchListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchListViewHolder(LayoutInflater.from(this.context).inflate(R.layout.search_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListViewHolder holder, final int position) {
        holder.searchUsernameTV.setText(usersList.get(position).getUsername());
        holder.searchAuthorTV.setText(usersList.get(position).getUsername());

        if (usersList.get(position).getUserAvatar() != null) {
            Picasso.get().load(usersList.get(position).getUserAvatar()).transform(new ImageCircleTransform())
                    .into(holder.userAvatarImageView);
        }

        holder.searchUsernameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usersList.get(position)._getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, new ProfileFragment()).commit();
                } else {
                    context.startActivity(new Intent(context, ProfileActivity.class)
                            .putExtra(Users.USER_ID, usersList.get(position)._getUserId()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Users> filteredUsers = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    filteredUsers.addAll(allUsersList);
                }
                else {
                    String filteredPattern = charSequence.toString().toLowerCase().trim();
                    for (Users u : allUsersList) {
                        if (u.getUsername().toLowerCase().contains(filteredPattern)) {
                            filteredUsers.add(u);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredUsers;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                usersList.clear();
                usersList.addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    class SearchListViewHolder extends RecyclerView.ViewHolder {

        ImageView userAvatarImageView;
        TextView searchUsernameTV, searchAuthorTV;

        public SearchListViewHolder(@NonNull View iv) {
            super(iv);

            userAvatarImageView = iv.findViewById(R.id.userAvatarImageView);
            searchUsernameTV = iv.findViewById(R.id.searchUsernameTV);
            searchAuthorTV = iv.findViewById(R.id.searchAuthorTV);
        }
    }
}
