package com.example.quotebox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quotebox.adapters.CollectionDialogAdapter;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PostCollectionListActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    CollectionDialogAdapter collectionDialogAdapter;
    GlobalClass globalClass;

    Button createNewCollectionBtn, selectedCollectionSubmitBtn;
    RecyclerView collectionNamesRL;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_collection_list);

        firestore = FirebaseFirestore.getInstance();
        globalClass = (GlobalClass) getApplicationContext();

        createNewCollectionBtn = findViewById(R.id.createNewCollectionBtn);
        selectedCollectionSubmitBtn = findViewById(R.id.selectedCollectionSubmitBtn);
        collectionNamesRL = findViewById(R.id.collectionNamesRL);
        toolbar = findViewById(R.id.toolbar);

        collectionNamesRL.setHasFixedSize(true);
        collectionNamesRL.setLayoutManager(new LinearLayoutManager(PostCollectionListActivity.this));

        toolbar.setTitle("Save to collection");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(PostCollectionListActivity.this, HomeActivity.class));
            }
        });


        firestore.collection(new CollectionNames().getUserCollectionName())
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Users user = task.getResult().toObject(Users.class);

                        List<String> pl = new ArrayList<>();

                        for (String l : user.getUserPostCollections().keySet()) {
                            pl.add(l);
                        }

                        collectionDialogAdapter = new CollectionDialogAdapter(PostCollectionListActivity.this, pl);
                        collectionNamesRL.setAdapter(collectionDialogAdapter);
                    }
                });

//        List<String> pl = new ArrayList<>();
//        pl.add("somenwfav");
//        pl.add("coll1");
//        pl.add("some colname");
//
//        collectionDialogAdapter = new CollectionDialogAdapter(PostCollectionListActivity.this, pl);
//        collectionNamesRL.setAdapter(collectionDialogAdapter);
    }


}
