package com.example.quotebox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    TextView splashScreenTag;
    SharedPreferencesConfig preferencesConfig;
    HashMap<String, HashMap<String, String>> allUsersData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        allUsersData = new HashMap<>();

        preferencesConfig = new SharedPreferencesConfig(this);

        firestore.collection(new CollectionNames().getUserCollection()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                HashMap<String, String> data = new HashMap<>();
                                data.put(Users.USERAVATAR, doc.getString(Users.USERAVATAR));
                                data.put(Users.USERNAME, doc.getString(Users.USERNAME));

                                allUsersData.put(doc.getId(), data);

                                Log.d("SPLASH_ACTIVITY_LOG", allUsersData.toString());
                            }

                            Gson gson = new Gson();
                            String jsonData = gson.toJson(allUsersData);
                            Log.d("SPLASH_ACTIVITY", jsonData);
                            preferencesConfig.setLoggedInUserCreds(jsonData);

                        }
                    }
                });

        splashScreenTag = findViewById(R.id.splashScreenTag);

        Animation fadeInAnime = AnimationUtils.loadAnimation(this, R.anim.fade_in_transition);

        splashScreenTag.startAnimation(fadeInAnime);

        String msg = (firebaseAuth.getCurrentUser() != null) ? "true" : "false";
        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_LONG).show();


        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();

                    if (firebaseAuth.getCurrentUser() != null) {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    }
                    else {
                        startActivity(new Intent(SplashActivity.this, LandingActivity.class));
                    }
                }
            }
        };

        timer.start();
    }
}
