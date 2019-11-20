package com.example.quotebox.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.PostCollectionListActivity;
import com.example.quotebox.R;

import java.util.List;

public class CollectionDialogAdapter extends RecyclerView.Adapter<CollectionDialogAdapter.CollectionViewHolder> {

    private List<String> pcl;
    private PostCollectionListActivity context;

    public CollectionDialogAdapter(PostCollectionListActivity c, List<String> pcl) {
        Log.d("COLL_DIALOG_ADAP", pcl.toString());
        this.context = c;
        this.pcl = pcl;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CollectionViewHolder(LayoutInflater.from(this.context).inflate(R.layout.collection_names_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, final int position) {
        holder.collectionNameCB.setText(pcl.get(position));

        Log.d("COLL_DIALOG_ADAP", pcl.get(position));

        holder.collectionNameCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.d("COLL_DIALOG_ADAP", pcl.get(position) + " checked");
                }
                else {
                    Log.d("COLL_DIALOG_ADAP", pcl.get(position) + " UNchecked");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pcl.size();
    }

    class CollectionViewHolder extends RecyclerView.ViewHolder {

        CheckBox collectionNameCB;

        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            collectionNameCB = itemView.findViewById(R.id.collectionNameCB);
        }
    }
}
