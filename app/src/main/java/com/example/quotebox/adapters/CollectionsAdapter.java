package com.example.quotebox.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.CollectionPostsActivity;
import com.example.quotebox.R;
import com.example.quotebox.models.Users;
import com.example.quotebox.ui.CollectionFragment;

import java.util.HashMap;
import java.util.List;

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.CollectionViewHolder> {

    List<String> collNames;
    HashMap<String, List<String>> cps;
    CollectionFragment context;

    public CollectionsAdapter(CollectionFragment cf, List<String> l, HashMap<String, List<String>> cps) {
        this.context = cf;
        this.collNames = l;
        this.cps = cps;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CollectionViewHolder(LayoutInflater.from(this.context.getActivity()).inflate(R.layout.collections_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, final int position) {
        holder.collectionNameTV.setText(collNames.get(position));
        holder.collectionPostCountTV.setText("Posts (" + cps.get(collNames.get(position)).size() + ")");

        holder.collPostWrapperLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = new Bundle();
                extras.putString(Users.COLLECTION_NAME, collNames.get(position));
                extras.putString(Users.POSTS_IN_COLLECTION, Integer.toString(cps.get(collNames.get(position)).size()));

                context.getActivity().startActivity(new Intent(
                        context.getActivity(),
                        CollectionPostsActivity.class
                ).putExtras(extras));
            }
        });
    }

    @Override
    public int getItemCount() {
        return collNames.size();
    }

    class CollectionViewHolder extends RecyclerView.ViewHolder {

        TextView collectionNameTV, collectionPostCountTV;
        LinearLayout collPostWrapperLL;

        public CollectionViewHolder(@NonNull View iv) {
            super(iv);

            collectionNameTV = iv.findViewById(R.id.collectionNameTV);
            collPostWrapperLL = iv.findViewById(R.id.collPostWrapperLL);
            collectionPostCountTV = iv.findViewById(R.id.collectionPostCountTV);
        }
    }
}
