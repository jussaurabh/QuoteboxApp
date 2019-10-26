package com.example.quotebox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.helpers.ImageCircleTransform;
import com.example.quotebox.helpers.SharedPreferencesConfig;
import com.example.quotebox.models.Users;
import com.example.quotebox.ui.CollectionFragment;
import com.example.quotebox.ui.HomeFragment;
import com.example.quotebox.ui.NotificationFragment;
import com.example.quotebox.ui.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferencesConfig preferencesConfig;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    Users users;
    CollectionNames collectionNames;

    private DrawerLayout drawerLayout;

    TextView nav_header_username, nav_header_email;
    ImageView nav_header_avatar;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton homeFramentFloatingButton, poemPostFab, quotePostFab;
    ConstraintLayout quoteFabWrapper, poemFabWrapper;

    boolean isFloatingBtnOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();

        preferencesConfig = new SharedPreferencesConfig(getApplicationContext());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        collectionNames = new CollectionNames();
        users = new Users();

        getUserData();

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        navigationView = findViewById(R.id.nav_view);
        android.view.View navHeaderView = navigationView.getHeaderView(0);
        nav_header_avatar = navHeaderView.findViewById(R.id.nav_header_avatar);
        nav_header_email = navHeaderView.findViewById(R.id.nav_header_email);
        nav_header_username = navHeaderView.findViewById(R.id.nav_header_username);
        navigationView.setNavigationItemSelectedListener(this);

        homeFramentFloatingButton = findViewById(R.id.homeFramentFloatingButton);
        quoteFabWrapper = findViewById(R.id.quoteFabWrapper);
        poemFabWrapper = findViewById(R.id.poemFabWrapper);
        quotePostFab = findViewById(R.id.quotePostFab);
        poemPostFab = findViewById(R.id.poemPostFab);


        if (firebaseUser.getPhotoUrl() != null) {
            Picasso.get().load(firebaseUser.getPhotoUrl())
                    .transform(new ImageCircleTransform())
                    .into(nav_header_avatar);
        }




        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_activity_home);


        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        homeFramentFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quoteFabWrapper.getVisibility() == View.GONE &&
                        poemFabWrapper.getVisibility() == View.GONE) {
                    displayFabBtn();
                }
                else {
                    hideFabBtn();
                }


            }
        });

        quotePostFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFabBtn();
                startActivity(new Intent(HomeActivity.this, PostQuote.class));
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_logout: {
                firebaseAuth.signOut();
                preferencesConfig.setLoginStatus(false);
                finish();
                startActivity(new Intent(HomeActivity.this, LandingActivity.class));
                break;
            }

        }
        return true;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.bottom_nav_home: {
                    selectedFragment = new HomeFragment();
                    break;
                }
                case R.id.bottom_nav_collection: {
                    selectedFragment = new CollectionFragment();
                    break;
                }
                case R.id.bottom_nav_notification: {
                    selectedFragment = new NotificationFragment();
                    break;
                }
                case R.id.bottom_nav_account: {
                    selectedFragment = new ProfileFragment();
                    break;
                }
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();

            return true;
        }
    };


    public void getUserData() {
        firestore.collection(collectionNames.getUserCollection()).whereEqualTo(Users.EMAIL, firebaseUser.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> userList = task.getResult().getDocuments();

                        users = new Users(
                                userList.get(0).getString(Users.USERNAME),
                                userList.get(0).getString(Users.EMAIL),
                                userList.get(0).getString(Users.PASSWORD),
                                userList.get(0).getString(Users.USERAVATAR),
                                userList.get(0).getString(Users.USERABOUTME),
                                null,
                                Integer.parseInt(userList.get(0).get(Users.QUOTEPOSTLIKES).toString()),
                                Integer.parseInt(userList.get(0).get(Users.NOOFQUOTESPOSTED).toString())
                        );

                        nav_header_username.setText(users.getUsername());
                        nav_header_email.setText(users.getEmail());
                    }
                });
    }


    private void displayFabBtn() {
        homeFramentFloatingButton.setImageResource(R.drawable.ic_close_white);
        quoteFabWrapper.setVisibility(View.VISIBLE);
        poemFabWrapper.setVisibility(View.VISIBLE);
    }

    private void hideFabBtn() {
        homeFramentFloatingButton.setImageResource(R.drawable.ic_edit_white);
        quoteFabWrapper.setVisibility(View.GONE);
        poemFabWrapper.setVisibility(View.GONE);
    }

}
