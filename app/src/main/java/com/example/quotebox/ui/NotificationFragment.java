package com.example.quotebox.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quotebox.FollowRequestActivity;
import com.example.quotebox.R;
import com.example.quotebox.adapters.NotificationAdapter;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.models.Notifications;
import com.example.quotebox.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class NotificationFragment extends Fragment {

    GlobalClass globalClass;
    FirebaseFirestore firestore;
    List<Notifications> notifyList;
    NotificationAdapter notificationAdapter;

    LinearLayout followRequestWrapperLL, notifyDefaultWrapperLL;
    TextView followRequestCountTV;
    RecyclerView notifyListRecyclerView;
    SwipeRefreshLayout notifySwipeRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_notification, container, false);
        init(v);

        getUsersNotifications();

        notifySwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUsersNotifications();
                notifySwipeRefresh.setRefreshing(false);
            }
        });


        if (globalClass.getAllUsersData().get(FirebaseAuth.getInstance().getCurrentUser().getUid()).getFollowRequestReceived().size() > 0) {
            int followRequestCount = globalClass.getAllUsersData().get(FirebaseAuth.getInstance().getCurrentUser().getUid()).getFollowRequestReceived().size();
            followRequestWrapperLL.setVisibility(View.VISIBLE);
            followRequestCountTV.setText(Integer.toString(followRequestCount));
        }
//        else {
//            notifyDefaultWrapperLL.setVisibility(View.VISIBLE);
//        }

        followRequestWrapperLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FollowRequestActivity.class));
            }
        });


        return v;
    }

    public void getUsersNotifications() {
        firestore.collection(CollectionNames.NOTIFICATIONS)
                .whereEqualTo(Users.USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            notifyList = task.getResult().toObjects(Notifications.class);

                            notifyListRecyclerView.setVisibility(View.VISIBLE);
                            notifyDefaultWrapperLL.setVisibility(View.GONE);

                            notificationAdapter = new NotificationAdapter(getContext(), notifyList, globalClass.getAllUsersData());
                            notifyListRecyclerView.setAdapter(notificationAdapter);
                        }
                    }
                });
    }

    public void init(View v) {
        globalClass = (GlobalClass) getContext().getApplicationContext();
        firestore = FirebaseFirestore.getInstance();

        followRequestWrapperLL = v.findViewById(R.id.followRequestWrapperLL);
        notifyDefaultWrapperLL = v.findViewById(R.id.notifyDefaultWrapperLL);
        followRequestCountTV = v.findViewById(R.id.followRequestCountTV);
        notifyListRecyclerView = v.findViewById(R.id.notifyListRecyclerView);
        notifySwipeRefresh = v.findViewById(R.id.notifySwipeRefresh);

        notifyListRecyclerView.setHasFixedSize(true);
        notifyListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        notifySwipeRefresh.setRefreshing(true);
    }
}
