package com.akshay.minglishmantra_beta.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Modal.TestModal;
import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.ViewHolders.TestHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DrawerPaymentFragment extends Fragment {

    RecyclerView paymentListRv;

    private FirebaseRecyclerAdapter<TestModal, TestHolder> firebaseRecyclerAdapterPayment = null;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.drawer_payment_fragment, container, false);

        mAuth =FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        paymentListRv =view.findViewById(R.id.drawer_payment_fragment_recyclerviewRv);

        //setting up recyclerview
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(false);
        paymentListRv.setLayoutManager(manager);

        attachFirebaseRecycler();

        return view;
    }


    private void attachFirebaseRecycler() {

        firebaseRecyclerAdapterPayment = new FirebaseRecyclerAdapter<TestModal, TestHolder>(

                TestModal.class,
                R.layout.drawer_payment_single_payment,
                TestHolder.class,
                mRef.child("test").limitToFirst(10)

        ) {
            @Override
            protected void populateViewHolder(final TestHolder holder, TestModal modal, final int position) {




            }
        };


        // setting firebaseRecyclerAdapter
        paymentListRv.setAdapter(firebaseRecyclerAdapterPayment);


    }

}
