package com.akshay.minglishmantra_beta.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Adapter.PostAdapter;
import com.akshay.minglishmantra_beta.ApplicationClass;
import com.akshay.minglishmantra_beta.Modal.PostModal;

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
import java.util.Date;

public class MainPostFragment extends Fragment {

    ApplicationClass applicationClass;

    public static class postStruct
    {
        public String postId,quiz_status,quiz_marks,save_status;

        public postStruct() {

        }

        public postStruct(String postId, String quiz_status, String save_status,String quiz_marks) {
            this.postId = postId;
            this.quiz_status = quiz_status;
            this.save_status = save_status;
            this.quiz_marks =quiz_marks;
        }

    }




    //quiz
    ArrayList<postStruct> postStatusArrayList =new ArrayList<>();
    public ArrayList<PostModal> mData =new ArrayList<>();
    Boolean isQuiz=true,isNotice=true;

    PostAdapter adapter;


    TextView filterTv;
    RecyclerView postRv;
    TextView loadingTv;
    RelativeLayout filterRl;
    Dialog dialog;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    View mView;
    ViewGroup mContainer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.main_home_fragment, container, false);
        mContainer =container;

        postStatusArrayList.clear();
        mData.clear();

        applicationClass = (ApplicationClass) getActivity().getApplicationContext();
        mAuth =FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().getRoot();


        filterTv =mView.findViewById(R.id.main_home_fragment_allCoursesTv);
        postRv =mView.findViewById(R.id.main_home_fragment_recyclerviewRv);
        loadingTv =mView.findViewById(R.id.main_home_fragment_loadingTv);
        filterRl =mView.findViewById(R.id.main_home_fragment_allPostRl);


        //adapter init
        // set up the RecyclerView
        postRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        adapter = new PostAdapter(getContext(),mData,postStatusArrayList,mRef,mAuth,isNotice,isQuiz);
        postRv.setAdapter(adapter);




        mRef.child("students_metadata").child(mAuth.getCurrentUser().getUid()).child("post").orderByChild("date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if( dataSnapshot.getChildrenCount()!=0) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        postStruct postStruct =new postStruct(snapshot.getKey()
                                ,snapshot.child("quiz_status").getValue().toString()
                                ,snapshot.child("save_status").getValue().toString(),snapshot.child("quiz_marks").getValue().toString());
                        postStatusArrayList.add(postStruct);
                    }
                }


                int count=0;

                for(final String courseName : applicationClass.getEnrolledCourse()){

                    final String tempCoursename =courseName;

                    final int finalCount = count;


                    mRef.child(courseName).child("posts").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                            PostModal modal = dataSnapshot.getValue(PostModal.class);
                            modal.setPostId(dataSnapshot.getKey());
                            modal.setCourseName(tempCoursename);
                            Log.d("hushar","onChildAdded:"+ modal.getDate());

                            int insertIndex = getInsertIndex(modal);
                            mData.add(insertIndex, modal);
                            adapter.notifyItemInserted(insertIndex);


                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            PostModal modal = dataSnapshot.getValue(PostModal.class);

                            int updateIndex = indexOfMData(dataSnapshot.getKey());
                            mData.get(updateIndex).setText(modal.getText());
                            adapter.notifyItemChanged(updateIndex);

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            String postId =dataSnapshot.getKey();
                            int removeIndex = indexOfMData(postId);
                            mData.remove(removeIndex);
                            adapter.notifyItemRemoved(removeIndex);

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {


                        }});

                    mRef.child(courseName).child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(finalCount ==applicationClass.getEnrolledCourse().size()-1){

                                if(mData.isEmpty())
                                    loadingTv.setText("No post found !!!");
                                else
                                    loadingTv.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }});



                    count++;

                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});



        filterRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               dialog.show();

            }});


        showDialog();

        return mView;
    }

    private int getInsertIndex(PostModal modal) {

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
        for (PostModal tempModal : mData){  // 12 ,10 , 8 , 6 , 9

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
        for(PostModal modal : mData){

            if(modal.postId.equals(postId)){

                return i;

            }

            i++;

        }


        return -1;

    }




    @Override
    public void onStart() {
        super.onStart();




    }



    private void showDialog() {

        dialog = new BottomSheetDialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.post_dialog_filter);

        Window window = dialog.getWindow();
        // window.setGravity(Gravity.CENTER);

        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.setTitle(null);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        final LinearLayout addViewLl =dialog.findViewById(R.id.home_dialog_filter_addViewLl);

        for(int i=0;i<3;i++) {

            final View child = getLayoutInflater().inflate(R.layout.doubt_main_single_chapters, mContainer, false);
            child.setId(i);
            final TextView tittleTv = child.findViewById(R.id.doubt_main_single_chapters_tittleTv);
            View viewV = child.findViewById(R.id.doubt_main_single_chapters_viewV);
            viewV.setVisibility(View.INVISIBLE);

            if(i==0) {
                tittleTv.setText("All Posts");
                tittleTv.setTextColor(getResources().getColor(R.color.colorPrimaryGreen));

            } else if(i==1){
                tittleTv.setText("Notices");
            }else
                tittleTv.setText("Quizzes");


            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tittleTv.setTextColor(getResources().getColor(R.color.colorPrimaryGreen));
                    String tittleStr =tittleTv.getText().toString();
                    int p =child.getId();

                    if(tittleStr.equals("All Posts")){

                        adapter.isQuiz=true;
                        adapter.isNotice=true;

                    }else if(tittleStr.equals("Notices")){

                        adapter.isNotice=true;
                        adapter.isQuiz=false;

                    }else{ //quiz
                        adapter.isQuiz=true;
                        adapter.isNotice=false;
                    }


                    for (int k = 0; k < 3; k++) {

                        if(k!=p) {
                            TextView textView = addViewLl.getChildAt(k ).findViewById(R.id.doubt_main_single_chapters_tittleTv);
                            textView.setTextColor(getResources().getColor(R.color.black));
                        }


                    }

                    adapter.notifyDataSetChanged();
                    filterTv.setText(tittleTv.getText().toString());
                    dialog.hide();

                }});




            addViewLl.addView(child);
        }





    }



    //important methods

    private void varInit(String courseName) {

/*

        for(PostModal k : mData){

            Log.d("nana", k.getDate());

        }

        Collections.sort(mData, new Comparator<PostModal>() {
            @Override
            public int compare(PostModal lhs, PostModal rhs) {

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                String lhsString =format.format(Date.parse(lhs.getDate()));
                String rhsString =format.format(Date.parse(rhs.getDate()));

                try {
                    Date date =format.parse(lhsString);
                    Long ttLhs =date.getTime();

                    try {
                        Date rhsDate =format.parse(rhsString);
                        Long ttRhs =rhsDate.getTime();


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




        for(PostModal k : mData){

            Log.d("nanaAfter", k.getDate());

        }




 */



    }



}
