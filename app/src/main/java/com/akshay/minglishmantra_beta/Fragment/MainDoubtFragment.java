package com.akshay.minglishmantra_beta.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Activity.MainDetailedActivity;
import com.akshay.minglishmantra_beta.Adapter.DoubtAdapter;
import com.akshay.minglishmantra_beta.ApplicationClass;
import com.akshay.minglishmantra_beta.Modal.DoubtModal;

import com.akshay.minglishmantra_beta.R;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainDoubtFragment extends Fragment {

    ApplicationClass applicationClass;

    RecyclerView doubtRv;
    DoubtAdapter doubtAdapter;
    ArrayList<DoubtModal> mData =new ArrayList<>();

    TextView loadingTv;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;


    //filters
    TextView filterByCourseTv,filterBySubjectTv;
    Dialog dialogCourse,dialogSubject;
    String selectedCourseStr="x",selectedSubjectStr="x";


    View mView;
    ViewGroup mContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.main_doubt_fragment, container, false);
        mContainer =container;


        mData.clear();

        applicationClass = (ApplicationClass) getActivity().getApplicationContext();
        mAuth =FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().getRoot();

        LinearLayout selectedSubjectLl =mView.findViewById(R.id.fragment_question_addSubjectRootLinear);
        LinearLayout askDoubtLl =mView.findViewById(R.id.main_doubt_fragment_askDoubtLl);
        doubtRv =mView.findViewById(R.id.main_doubt_fragment_doubtRv);
        filterByCourseTv =mView.findViewById(R.id.main_doubt_fragment_filterByCourseTv);
        filterBySubjectTv =mView.findViewById(R.id.main_doubt_fragment_filterBySubjectTv);

        loadingTv =mView.findViewById(R.id.main_doubt_fragment_loadingTv);




        //setting adapter

        doubtRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        doubtAdapter = new DoubtAdapter(getContext(),mData,mRef,mAuth);
        doubtRv.setAdapter(doubtAdapter);


        int count =0;
        for(String courseName : applicationClass.getEnrolledCourse()){

            final String tempCoursename =courseName;
            final int finalCount = count;

            mRef.child(courseName).child("doubts").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    DoubtModal modal =dataSnapshot.getValue(DoubtModal.class);
                    modal.setPostId(dataSnapshot.getKey());
                    modal.setCourseName(tempCoursename);

                    int insertIndex = getInsertIndex(modal);
                    mData.add(insertIndex, modal);
                    doubtAdapter.notifyItemInserted(insertIndex);


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            mRef.child(courseName).child("doubts").addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(finalCount ==applicationClass.getEnrolledCourse().size()-1){

                        if(mData.isEmpty())
                            loadingTv.setText("No doubts found !!!");
                        else
                            loadingTv.setVisibility(View.GONE);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }});


            count++;
        }





        askDoubtLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), MainDetailedActivity.class);
                intent.putExtra("fragment", "doubt");
                startActivity(intent);

            }});

        filterByCourseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogCourse.show();

            }});

        /*

        filterBySubjectTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedSubjectStr.equals("x")) {
                    showDialogSubject();
                }else{

                    dialogSubject.show();
                }

            }});


         */

        showDialogCourse();

        return mView;
    }



    private int getInsertIndex(DoubtModal modal) {

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String string =format.format(Date.parse(modal.getDate()));

        Date date = null;
        try {
            date = format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Long timestamp =date.getTime();


        int tempCount=0;
        for (DoubtModal tempModal : mData){  // 12 ,10 , 8 , 6 , 9

            String innerString =format.format(Date.parse(tempModal.getDate()));
            Date innerDate = null;

            try {
                innerDate = format.parse(innerString); } catch (ParseException e) { e.printStackTrace(); }

            Long innerTimestamp =innerDate.getTime();


            if(timestamp > innerTimestamp){

                return tempCount;
            }

            tempCount++;

        }




        return tempCount;

    }




    public int indexOfMData(String postId){

        int i=0;
        for(DoubtModal modal : mData){

            if(modal.postId.equals(postId)){

                return i;

            }

            i++;

        }


        return -1;

    }





    private void showDialogCourse() {

        dialogCourse = new BottomSheetDialog(getContext());
        dialogCourse.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCourse.setContentView(R.layout.post_dialog_filter);

        Window window = dialogCourse.getWindow();
        // window.setGravity(Gravity.CENTER);

        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialogCourse.setTitle(null);
        dialogCourse.setCancelable(true);
        dialogCourse.setCanceledOnTouchOutside(true);

        final LinearLayout addViewLl = dialogCourse.findViewById(R.id.home_dialog_filter_addViewLl);
        TextView dialogTittleTv = dialogCourse.findViewById(R.id.home_dialog_filter_tittleTv);
        dialogTittleTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_black_24dp, 0);
        dialogTittleTv.setText("CHOOSE COURSE");


        //add 'All' manually
        final View child = getLayoutInflater().inflate(R.layout.doubt_main_single_chapters, mContainer, false);
        TextView tittleTv = child.findViewById(R.id.doubt_main_single_chapters_tittleTv);
        tittleTv.setText("All");



        tittleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCourse.hide();

                filterByCourseTv.setText("Filter By : " + "All");

                doubtAdapter.testFilter = "All";
                doubtAdapter.notifyDataSetChanged();


                dialogCourse.hide();
            }
        });

        addViewLl.addView(child);



                mRef.child("student").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (dataSnapshot.getChildrenCount() != 0) {


                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                final String courseName = snapshot.getKey();
                                final View child = getLayoutInflater().inflate(R.layout.doubt_main_single_chapters, mContainer, false);


                                TextView tittleTv = child.findViewById(R.id.doubt_main_single_chapters_tittleTv);
                                tittleTv.setText(courseName);


                                tittleTv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialogCourse.hide();

                                        filterByCourseTv.setText("Filter By : " + courseName);
                                        selectedCourseStr = courseName;

                                        doubtAdapter.testFilter = courseName;
                                        doubtAdapter.notifyDataSetChanged();


                                        dialogCourse.hide();


                                        // for subject filter also// selectedSubjectStr="x";
                                        //   filterBySubjectTv.setText("Choose Subject : All");
                                        //   filterBySubjectTv.setVisibility(View.VISIBLE);

                                    }
                                });

                                addViewLl.addView(child);

                            }

                        } else {


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }


            private void showDialogSubject() {

                dialogSubject = new BottomSheetDialog(getContext());
                dialogSubject.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogSubject.setContentView(R.layout.post_dialog_filter);

                Window window = dialogSubject.getWindow();
                // window.setGravity(Gravity.CENTER);

                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                dialogSubject.setTitle(null);
                dialogSubject.setCancelable(true);
                dialogSubject.setCanceledOnTouchOutside(true);

                final LinearLayout addViewLl = dialogSubject.findViewById(R.id.home_dialog_filter_addViewLl);
                TextView dialogTittleTv = dialogSubject.findViewById(R.id.home_dialog_filter_tittleTv);
                dialogTittleTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_black_24dp, 0);
                dialogTittleTv.setText("CHOOSE SUBJECT");

                mRef.child("course").orderByChild("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        if (dataSnapshot.getChildrenCount() != 0) {

                            addViewLl.removeAllViewsInLayout();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                final String name = snapshot.child("name").getValue().toString();
                                final View child = getLayoutInflater().inflate(R.layout.doubt_main_single_chapters, mContainer, false);


                                TextView tittleTv = child.findViewById(R.id.doubt_main_single_chapters_tittleTv);
                                tittleTv.setText(name);


                                tittleTv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        dialogSubject.hide();

                                        filterBySubjectTv.setText("Filter By : " + name);
                                        selectedSubjectStr = name;

                                    }
                                });

                                addViewLl.addView(child);

                            }

                        } else {


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                dialogSubject.show();

            }


            //important methods


            private void varInit() {


                for (DoubtModal k : mData) {

                    Log.d("nana", k.getDate());

                }

                Collections.sort(mData, new Comparator<DoubtModal>() {
                    @Override
                    public int compare(DoubtModal lhs, DoubtModal rhs) {

                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                        String lhsString = format.format(Date.parse(lhs.getDate()));
                        String rhsString = format.format(Date.parse(rhs.getDate()));

                        try {
                            Date date = format.parse(lhsString);
                            Long ttLhs = date.getTime();

                            try {
                                Date rhsDate = format.parse(rhsString);
                                Long ttRhs = rhsDate.getTime();


                                return ttLhs.compareTo(ttRhs);


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        return 0;

                    }
                });


                for (DoubtModal k : mData) {

                    Log.d("nanaAfter", k.getDate());

                }


                // set up the RecyclerView
                doubtRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                DoubtAdapter adapter = new DoubtAdapter(getContext(), mData, mRef, mAuth);
                doubtRv.setAdapter(adapter);


            }

    }




