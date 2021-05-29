package com.akshay.minglishmantra_beta.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Activity.MaterialVideoActivity;
import com.akshay.minglishmantra_beta.Modal.PaperListModal;
import com.akshay.minglishmantra_beta.R;

import com.akshay.minglishmantra_beta.ViewHolders.PaperListHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClassroomMaterialFragment extends Fragment {
    RecyclerView selectUnitRv;

    private FirebaseRecyclerAdapter<PaperListModal, PaperListHolder> firebaseRecyclerAdapter = null;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    ViewGroup mContainer;

    private ArrayList<String> expandArray=new ArrayList<>();
    private ArrayList<ArrayList<String>> data =new ArrayList<>();

    private String selectCourseStr;

    public ClassroomMaterialFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.classroom_material_fragment, container, false);

        mContainer =container;


        mAuth =FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().getRoot();

        selectCourseStr =getActivity().getIntent().getStringExtra("course_name");


        selectUnitRv =view.findViewById(R.id.main_live_fragment_liveRv);
        //setting up recyclerview
        //selectUnitRv.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(false);
        selectUnitRv.setLayoutManager(manager);



        mRef.child(selectCourseStr).child("subject").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    ArrayList<String> temp =new ArrayList<>();
                    for(DataSnapshot innerSnapshot : snapshot.getChildren()){

                        temp.add(innerSnapshot.child("name").getValue().toString());

                    }

                    data.add(temp);


                }




                attachFirebaseRecycler();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});




        return view;

    }



    private void attachFirebaseRecycler() {

        Log.d("abhi","0");

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PaperListModal, PaperListHolder>(

                PaperListModal.class,
                R.layout.classroom_material_single_content,
                PaperListHolder.class,
                mRef.child(selectCourseStr).child("paper")

        ) {

            @Override
            protected void populateViewHolder(final PaperListHolder holder, final PaperListModal modal, final int position) {


                holder.tittleTv.setText(modal.getName());


                //addview() the single subject layout
                if (expandArray.size() < position + 1) {
                    expandArray.add(position, "no");
                }


                if(expandArray.get(position).equals("yes")){

                    holder.progressBarPb.setVisibility(View.VISIBLE);

                    mRef.child(selectCourseStr).child("subject").child(modal.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            holder.addviewLl.removeAllViews();

                            int count =1;

                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                final String tittleStr =snapshot.child("name").getValue().toString();

                                final View child = getLayoutInflater().inflate(R.layout.classroom_material_single_content_in, mContainer, false);
                                TextView tittleTv =child.findViewById(R.id.enrolled_material_single_content_in_tittleTv);
                                TextView srNoTv =child.findViewById(R.id.enrolled_material_single_content_in_srNoTv);

                                tittleTv.setText(tittleStr);
                                srNoTv.setText(""+count);

                                child.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        //getArguments
                                        /*
                                        Bundle args = new Bundle();
                                        args.putString("subject",tittleStr);
                                        Navigation.findNavController(getActivity(),R.id.host_detailed).navigate(R.id.enrolledMaterialDeep1Fragment,args);

                                         */
                                        Intent intent =new Intent(getActivity(), MaterialVideoActivity.class);
                                        intent.putExtra("course_name",selectCourseStr);
                                        intent.putExtra("subject",tittleStr);
                                        startActivity(intent);


                                    }});

                                holder.addviewLl.addView(child);
                                count++;

                            }


                            holder.progressBarPb.setVisibility(View.GONE);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }});







                }else {

                    holder.addviewLl.removeAllViews();


                }




                holder.parentRl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(expandArray.get(position).equals("no")){

                            expandArray.set(position,"yes");
                            holder.progressBarPb.setVisibility(View.VISIBLE);

                        }else{

                            expandArray.set(position,"no");
                        }

                        firebaseRecyclerAdapter.notifyDataSetChanged();

                    }});



            }};

        // firebaseRecyclerAdapter.
        selectUnitRv.setAdapter(firebaseRecyclerAdapter);

    }





}
