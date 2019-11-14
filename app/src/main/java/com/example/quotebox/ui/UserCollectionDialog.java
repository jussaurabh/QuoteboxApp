package com.example.quotebox.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.R;

public class UserCollectionDialog extends Dialog {

    public UserCollectionDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public UserCollectionDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    Activity activity;
    Dialog dialog;
    Button createNewCollectionBtn, selectedCollectionSubmitBtn;
    RecyclerView collectionNamesRL;
    RecyclerView.Adapter adapter;

    public UserCollectionDialog(Activity a, RecyclerView.Adapter adapter) {
        super(a);
        this.activity = a;
        this.adapter = adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.collection_names_dialog);

        collectionNamesRL = findViewById(R.id.collectionNamesRL);
        collectionNamesRL.setLayoutManager(new LinearLayoutManager(activity));

        collectionNamesRL.setAdapter(adapter);
    }
}
