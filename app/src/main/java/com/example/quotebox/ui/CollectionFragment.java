package com.example.quotebox.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quotebox.R;
import com.example.quotebox.adapters.CollectionsAdapter;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CollectionFragment extends Fragment {

    FirebaseFirestore firestore;
    GlobalClass globalClass;
    CollectionsAdapter collectionsAdapter;
    final String LOGGED_IN_USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    AlertDialog.Builder builder;
    List<String> collNames;
    HashMap<String, List<String>> allUsersCollection = new HashMap<>();

    RecyclerView collectionNamesRecyclerView;
    SwipeRefreshLayout collectionNameSwipRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_collection, container, false);

        firestore = FirebaseFirestore.getInstance();
        globalClass = (GlobalClass)getActivity().getApplicationContext();
        allUsersCollection = globalClass.getAllUsersData().get(LOGGED_IN_USER_ID).getUserPostCollections();
        builder = new AlertDialog.Builder(getActivity());

        collectionNameSwipRefresh = view.findViewById(R.id.collectionNameSwipRefresh);
        collectionNamesRecyclerView = view.findViewById(R.id.collectionNamesRecyclerView);
        collectionNamesRecyclerView.setHasFixedSize(true);
        collectionNamesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getAllCollectionNames();

        collectionNameSwipRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllCollectionNames();
                collectionNameSwipRefresh.setRefreshing(false);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(collectionNamesRecyclerView);

        return view;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
    ) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            builder.setMessage("All the posts will be deleted with " + collNames.get(position).toUpperCase() + " collection.")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {
                            allUsersCollection.remove(collNames.get(position));

                            firestore.collection(CollectionNames.USERS)
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update(Users.USER_POST_COLLECTIONS, allUsersCollection)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            collectionsAdapter.notifyItemRemoved(position);
                                            collNames.remove(position);
                                            dialogInterface.dismiss();
                                        }
                                    });

                        }
                    });

            builder.create().show();

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red))
                    .addActionIcon(R.drawable.ic_delete_white)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public void getAllCollectionNames() {
        collNames = new ArrayList<>();
        for (String l : allUsersCollection.keySet()) {
            collNames.add(l);
        }

        collectionsAdapter = new CollectionsAdapter(CollectionFragment.this, collNames, allUsersCollection);
        collectionNamesRecyclerView.setAdapter(collectionsAdapter);
    }
}
