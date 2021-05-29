package com.akshay.minglishmantra_beta.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Modal.TestModal;
import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.ViewHolders.TestHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginSelectcourseFragment extends Fragment {

    View mView;

    RecyclerView selectCourseRv;
    private FirebaseRecyclerAdapter<TestModal, TestHolder> firebaseRecyclerAdapter = null;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.premium_main_fragment, container, false);


        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().getRoot();


        selectCourseRv =mView.findViewById(R.id.login_selectcourse_fragment_recyclerviewRv);
        //setting up recyclerview
        //  recyclerView.setHasFixedSize(true);
        GridLayoutManager manager =  new GridLayoutManager(getContext(), 2);
        manager.setReverseLayout(false);
        selectCourseRv.setLayoutManager(manager);

        attachFirebaseRecycler();



        return mView;
    }

    private void attachFirebaseRecycler() {


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TestModal, TestHolder>(

                TestModal.class,
                R.layout.premium_main_single_course,
                TestHolder.class,
                mRef.child("test").limitToFirst(10)

        ) {
            @Override
            protected void populateViewHolder(final TestHolder holder, TestModal modal, final int position) {


            }
        };


        // firebaseRecyclerAdapter.
        Log.d("abhi", "2");
        selectCourseRv.setAdapter(firebaseRecyclerAdapter);


    }
}
