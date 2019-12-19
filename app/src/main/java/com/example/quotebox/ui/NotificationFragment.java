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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quotebox.FollowRequestActivity;
import com.example.quotebox.R;
import com.example.quotebox.globals.GlobalClass;
import com.google.firebase.auth.FirebaseAuth;

public class NotificationFragment extends Fragment {

    GlobalClass globalClass;

    LinearLayout followRequestWrapperLL, notifyDefaultWrapperLL;
    TextView followRequestCountTV;
    RecyclerView notifyListRecyclerView;
    SwipeRefreshLayout notifySwipeRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_notification, container, false);
        init(v);

        if (globalClass.getAllUsersData().get(FirebaseAuth.getInstance().getCurrentUser().getUid()).getFollowRequestReceived().size() > 0) {
            int followRequestCount = globalClass.getAllUsersData().get(FirebaseAuth.getInstance().getCurrentUser().getUid()).getFollowRequestReceived().size();
            followRequestWrapperLL.setVisibility(View.VISIBLE);
            followRequestCountTV.setText(Integer.toString(followRequestCount));
        } else {
            notifyDefaultWrapperLL.setVisibility(View.VISIBLE);
        }

        followRequestWrapperLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FollowRequestActivity.class));
            }
        });


        return v;
    }

    public void init(View v) {
        globalClass = (GlobalClass) getContext().getApplicationContext();

        followRequestWrapperLL = v.findViewById(R.id.followRequestWrapperLL);
        notifyDefaultWrapperLL = v.findViewById(R.id.notifyDefaultWrapperLL);
        followRequestCountTV = v.findViewById(R.id.followRequestCountTV);
        notifyListRecyclerView = v.findViewById(R.id.notifyListRecyclerView);
        notifySwipeRefresh = v.findViewById(R.id.notifySwipeRefresh);

//        notifySwipeRefresh.setRefreshing(true);
    }
}
