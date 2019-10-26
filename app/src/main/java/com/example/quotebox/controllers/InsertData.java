package com.example.quotebox.controllers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.quotebox.interfaces.OnInsertDataListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class InsertData {

    private OnInsertDataListener onInsertDataListener;
    private Context context;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public void addOnInsertDataListener(OnInsertDataListener onInsertDataListener) {
        this.onInsertDataListener = onInsertDataListener;
    }

    public InsertData() {
        this.onInsertDataListener = null;
        this.context = null;
    }

    public InsertData add(String collectionName, Map<String, Object> data) {
        firestore.collection(collectionName).add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                        }
                    }
                });

        return this;
    }

    public InsertData addWithDocumentId(String collectionName, String documentId, Map<String, Object> data) {
        firestore.collection(collectionName).document(documentId).set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

        return this;
    }
}
