package com.example.quotebox.ui;

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

import com.example.quotebox.R;

public class NotificationFragment extends Fragment {

    LinearLayout followRequestWrapperLL, notifyDefaultWrapperLL;
    TextView followRequestCountTV;
    RecyclerView notifyListRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_notification, container, false);

        followRequestWrapperLL = v.findViewById(R.id.followRequestWrapperLL);
        notifyDefaultWrapperLL = v.findViewById(R.id.notifyDefaultWrapperLL);
        followRequestCountTV = v.findViewById(R.id.followRequestCountTV);
        notifyListRecyclerView = v.findViewById(R.id.notifyListRecyclerView);

        return v;
    }
}
