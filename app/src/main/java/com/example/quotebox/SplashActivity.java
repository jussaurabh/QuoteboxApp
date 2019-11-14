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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;


public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    TextView splashScreenTag;
    SharedPreferencesConfig preferencesConfig;
    HashMap<String, Users> allUsersData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        preferencesConfig = new SharedPreferencesConfig(this);

        allUsersData = new HashMap<>();


        splashScreenTag = findViewById(R.id.splashScreenTag);


//        firestore.collection(new CollectionNames().getUserCollection()).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            for (QueryDocumentSnapshot doc : task.getResult()) {
//                                Users data = new Users();
//                                data.setUserAvatar(doc.getString(Users.USERAVATAR));
//                                data.setUsername(doc.getString(Users.USERNAME));
//                                data.setEmail(doc.getString(Users.EMAIL));
//                                data.setFavPosts((List<String>) doc.get(Users.FAV_POSTS));
//                                data.setNoOfQuotesPosted(Integer.parseInt(doc.get(Users.NO_OF_QUOTES_POSTED).toString()));
//                                data.setNoOfPoemPosted(Integer.parseInt(doc.get(Users.NO_OF_POEM_POSTED).toString()));
//                                data.setNoOfStoryPosted(Integer.parseInt(doc.get(Users.NO_OF_STORY_POSTED).toString()));
//
//                                allUsersData.put(doc.getId(), data);
//                            }
//
//                            String jsonData = new Gson().toJson(allUsersData);
//                            Log.d("SPLASH_ACT_LOG", "all user creds: " + jsonData);
//                            preferencesConfig.setAllUserCreds(jsonData);
//                        }
//                    }
//                });

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
