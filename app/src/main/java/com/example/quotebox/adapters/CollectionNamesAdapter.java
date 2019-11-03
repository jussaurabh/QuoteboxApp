package com.example.quotebox.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.R;

import java.util.List;

public class CollectionNamesAdapter extends RecyclerView.Adapter<CollectionNamesAdapter.CollectionNamesViewHolder> {

    List<String> postCollectionNames;
    Context context;

    public CollectionNamesAdapter(Context context, List<String> pcn) {
        this.postCollectionNames = pcn;
        this.context = context;
    }

    @NonNull
    @Override
    public CollectionNamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CollectionNamesViewHolder(LayoutInflater.from(this.context).inflate(R.layout.collection_names_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionNamesViewHolder holder, int position) {
        Log.d("COLL_NAME_ADAP", "coll name: " + postCollectionNames.get(position));
        holder.collectionNameCB.setText(postCollectionNames.get(position));
    }

    @Override
    public int getItemCount() {
        return postCollectionNames.size();
    }

    class CollectionNamesViewHolder extends RecyclerView.ViewHolder{

        CheckBox collectionNameCB;

        public CollectionNamesViewHolder(@NonNull View itemView) {
            super(itemView);

            collectionNameCB = itemView.findViewById(R.id.collectionNameCB);
        }
    }
}
