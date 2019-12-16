package com.example.quotebox.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.R;
import com.example.quotebox.adapters.SearchListAdapter;
import com.example.quotebox.globals.GlobalClass;
import com.example.quotebox.models.Users;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    GlobalClass globalClass;
    SearchListAdapter searchListAdapter;
    List<Users> allUsersList;

    RecyclerView searchRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_search, container, false);

        globalClass = (GlobalClass) getActivity().getApplicationContext();
        allUsersList = new ArrayList<>();

        allUsersList.addAll(globalClass.getAllUsersData().values());

        searchRecyclerView = view.findViewById(R.id.searchRecyclerView);
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchListAdapter = new SearchListAdapter(getContext(), allUsersList);
        searchRecyclerView.setAdapter(searchListAdapter);

        Log.d("SEARCH", new Gson().toJson(allUsersList));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchListAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

}
