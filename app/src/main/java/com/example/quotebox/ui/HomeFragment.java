package com.example.quotebox.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quotebox.R;
import com.example.quotebox.adapters.PostsAdapter;
import com.example.quotebox.helpers.CollectionNames;
import com.example.quotebox.models.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {

    private FirebaseFirestore firestore;
    private static final String LOGGED_IN_USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//    List<HashMap<String, String>> postsList;
    private List<Posts> postsList;
    private Posts postAllData;

    private PostsAdapter postsAdapter;
    private RecyclerView homeRecyclerView;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        postAllData = new Posts();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firestore = FirebaseFirestore.getInstance();

        postsList = new ArrayList<>();

//        if (!postAllData.isPostsListEmpty()) {
//            Log.d("HOME_FRAGMENT ", "post list is not empty");
            firestore.collection(new CollectionNames().getPostCollection())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for(QueryDocumentSnapshot docs : task.getResult()) {
                                    Posts posts = new Posts();
                                    posts.setPostImage(docs.getString(Posts.POST_IMAGE));
                                    posts.setPost(docs.getString(Posts.POST));
                                    posts.setPostTitle(docs.getString(Posts.POST_TITLE));
                                    posts.setPostUser(docs.getString(Posts.POST_USER));
                                    posts.setPostComments(Integer.parseInt(docs.get(Posts.POST_COMMENTS).toString()));
                                    posts.setPostLikes(Integer.parseInt(docs.get(Posts.POST_LIKES).toString()));
                                    posts.setUserId(docs.getString(Posts.USER_ID));
                                    posts.setPostType(docs.getString(Posts.POST_TYPE));

                                    postsList.add(posts);
                                }

                                postAllData.setAllPostsList(postsList);

                                postsAdapter = new PostsAdapter(getActivity(), postsList);
                                homeRecyclerView.setAdapter(postsAdapter);
                            }
                        }
                    });
//        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeRecyclerView = view.findViewById(R.id.homeRecyclerView);
        homeRecyclerView.setHasFixedSize(true);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

//        collectionNamesDialog = new Dialog(view.getContext());
//        collectionNamesDialog.setContentView(R.layout.collection_names_dialog);
//        collectionNamesRL = collectionNamesDialog.findViewById(R.id.collectionNamesRL);
//        createNewCollectionBtnTV = collectionNamesDialog.findViewById(R.id.createNewCollectionBtnTV);
//        selectedCollectionSubmitBtn = collectionNamesDialog.findViewById(R.id.selectedCollectionSubmitBtn);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_home, container, false);
        return view;
    }



}
