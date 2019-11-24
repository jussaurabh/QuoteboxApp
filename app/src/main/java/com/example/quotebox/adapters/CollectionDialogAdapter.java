package com.example.quotebox.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.R;
import com.example.quotebox.ui.PostCollectionListDialogFragment;

import java.util.List;

public class CollectionDialogAdapter extends RecyclerView.Adapter<CollectionDialogAdapter.CollectionViewHolder> {

    private List<String> pcl;
    private PostCollectionListDialogFragment context;

    public CollectionDialogAdapter(PostCollectionListDialogFragment c, List<String> pcl) {
        Log.d("COLL_DIALOG_ADAP", pcl.toString());
        this.context = c;
        this.pcl = pcl;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CollectionViewHolder(LayoutInflater.from(this.context.getActivity()).inflate(R.layout.collection_names_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, final int position) {
        holder.collectionNameCB.setText(pcl.get(position));

        if (context.doesCollectionHasPost(pcl.get(position))) {
            holder.collectionNameCB.setChecked(true);
        }

        holder.collectionNameCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.d("COLL_DIALOG_ADAP", pcl.get(position) + " checked");
                    context.addItemToPostCollection(pcl.get(position));
                }
                else {
                    Log.d("COLL_DIALOG_ADAP", pcl.get(position) + " UNchecked");
                    context.removeItemToPostCollection(pcl.get(position));
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
