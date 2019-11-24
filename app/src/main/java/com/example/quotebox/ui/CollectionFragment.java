package com.example.quotebox.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.R;

public class CollectionFragment extends Fragment {

    RecyclerView collectionNamesRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_collection, container, false);

        collectionNamesRecyclerView = view.findViewById(R.id.collectionNamesRecyclerView);
        collectionNamesRecyclerView.setHasFixedSize(true);
        collectionNamesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
}
