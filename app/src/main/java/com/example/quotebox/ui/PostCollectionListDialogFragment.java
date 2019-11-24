package com.example.quotebox.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.R;
import com.example.quotebox.adapters.CollectionDialogAdapter;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostCollectionListDialogFragment extends DialogFragment {
    final String LOGGED_IN_USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    GlobalClass globalClass;
    CollectionDialogAdapter collectionDialogAdapter;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    WriteBatch batch = firestore.batch();
    DocumentReference docRef = firestore.collection(CollectionNames.USERS).document(LOGGED_IN_USER_ID);

    Button createNewCollectionBtn, selectedCollectionSubmitBtn;
    RecyclerView collectionNamesRL;
    ProgressBar userCollSubmitProgressBar;

    final String SELECTED_POST_ID;
    HashMap<String, List<String>> userCollList, copyUserCollList;


    public PostCollectionListDialogFragment(String id) {
        this.SELECTED_POST_ID = id;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("PDL", "Dialog onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("PDL", "Dialog onDestroy");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.activity_post_collection_list, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        globalClass = (GlobalClass)getActivity().getApplicationContext();
        userCollList = globalClass.getAllUsersData().get(LOGGED_IN_USER_ID).getUserPostCollections();
        setCopyUserCollList();

        Log.d("PCD", new Gson().toJson(userCollList));

        builder.setView(view);

        createNewCollectionBtn = view.findViewById(R.id.createNewCollectionBtn);
        selectedCollectionSubmitBtn = view.findViewById(R.id.selectedCollectionSubmitBtn);
        userCollSubmitProgressBar = view.findViewById(R.id.userCollSubmitProgressBar);
        collectionNamesRL = view.findViewById(R.id.collectionNamesRL);
        collectionNamesRL.setHasFixedSize(true);
        collectionNamesRL.setLayoutManager(new LinearLayoutManager(getActivity()));

        selectUserCollections();

        selectedCollectionSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PCD", new Gson().toJson(userCollList));

                userCollSubmitProgressBar.setVisibility(View.VISIBLE);

                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            userCollSubmitProgressBar.setVisibility(View.GONE);
                            globalClass.getAllUsersData().get(LOGGED_IN_USER_ID).setUserPostCollections(copyUserCollList);
                            dismiss();
                        }
                        else {
                            Log.d("PCD", "Som eERRRR in updateing colelctinos");
                        }
                    }
                });

            }
        });

        return builder.create();
    }


    public void setCopyUserCollList() {
        copyUserCollList = new HashMap<>();

        for (HashMap.Entry<String, List<String>> entry : userCollList.entrySet()) {
            copyUserCollList.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
    }

    public void selectUserCollections() {
        List<String> pcl = new ArrayList<>();

        for (String p : copyUserCollList.keySet()) pcl.add(p);

        collectionDialogAdapter = new CollectionDialogAdapter(PostCollectionListDialogFragment.this, pcl);
        collectionNamesRL.setAdapter(collectionDialogAdapter);
    }

    public boolean doesCollectionHasPost(String col) {
        return copyUserCollList.get(col).contains(SELECTED_POST_ID);
    }

    public void addItemToPostCollection(String col) {
        copyUserCollList.get(col).add(SELECTED_POST_ID);
        batch.update(docRef, Users.USER_POST_COLLECTIONS + "." + col, FieldValue.arrayUnion(SELECTED_POST_ID));
    }

    public void removeItemToPostCollection(String col) {
        copyUserCollList.get(col).remove(SELECTED_POST_ID);
        batch.update(docRef, Users.USER_POST_COLLECTIONS + "." + col, FieldValue.arrayRemove(SELECTED_POST_ID));
    }

}
