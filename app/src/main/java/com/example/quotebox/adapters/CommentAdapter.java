package com.example.quotebox.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.R;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.models.Comments;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comments> commentsList;

    public CommentAdapter(Context context, List<Comments> cmntlist) {
        this.context = context;
        this.commentsList = cmntlist;
        Log.d("COMMENT_ADP_LOG", "" + cmntlist.size());
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(this.context).inflate(R.layout.comment_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, final int position) {

        DateFormat cmntDate = new SimpleDateFormat("MMM d, ''yy");
        Date date = new Date(commentsList.get(position).getCommentTimestamp().getSeconds() * 1000);
        String cmntDateStr = cmntDate.format(date);

        Comments cmnt = commentsList.get(position);

        holder.commentUsernameTV.setText(cmnt.getUsername());
        holder.commentDateTV.setText(cmntDateStr);
        holder.commentTV.setText(cmnt.getComment());
        holder.commentLikeCountTV.setText(Integer.toString(cmnt.getLikesCount()));
        holder.commentDislikeCountTV.setText(Integer.toString(cmnt.getDislikesCount()));

        if (commentsList.get(position).getUserAvatar() != null) {
            Picasso.get().load(commentsList.get(position).getUserAvatar())
                    .transform(new ImageCircleTransform())
                    .into(holder.commentUserAvatarImageView);
        }

        holder.commentMenuImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.commentMenuImageBtn);

                popup.inflate(
                        commentsList.get(position).getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        ? R.menu.edit_or_delete_menu : R.menu.post_card_menu
                );

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete_Post:
                            case R.id.report_post:
                            case R.id.edit_Post: {
                                break;
                            }
                        }

                        return false;
                    }
                });

                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView commentUserAvatarImageView;
        TextView commentUsernameTV, commentDateTV, commentTV, commentLikeCountTV, commentDislikeCountTV;
        ImageButton commentLikeImageBtn, commentDislikeImageBtn, commentMenuImageBtn;

        public CommentViewHolder(@NonNull View iv) {
            super(iv);

            commentUserAvatarImageView = iv.findViewById(R.id.commentUserAvatarImageView);
            commentUsernameTV = iv.findViewById(R.id.commentUsernameTV);
            commentDateTV = iv.findViewById(R.id.commentDateTV);
            commentTV = iv.findViewById(R.id.commentTV);
            commentLikeCountTV = iv.findViewById(R.id.commentLikeCountTV);
            commentDislikeCountTV = iv.findViewById(R.id.commentDislikeCountTV);
            commentLikeImageBtn = iv.findViewById(R.id.commentLikeImageBtn);
            commentDislikeImageBtn = iv.findViewById(R.id.commentDislikeImageBtn);
            commentMenuImageBtn = iv.findViewById(R.id.commentMenuImageBtn);
        }
    }
}
