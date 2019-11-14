package com.example.quotebox.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.R;

import java.util.List;

public class CollectionDialogAdapter extends RecyclerView.Adapter<CollectionDialogAdapter.CollectionViewHolder> {

    private List<String> pcl;

    public CollectionDialogAdapter(List<String> pcl) {
        this.pcl = pcl;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class CollectionViewHolder extends RecyclerView.ViewHolder {

        CheckBox collectionNameCB;

        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            collectionNameCB = itemView.findViewById(R.id.collectionNameCB);
        }
    }
}
