package com.akshay.minglishmantra_beta.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akshay.minglishmantra_beta.Modal.TestDetailsModal;

import com.akshay.minglishmantra_beta.Modal.TestListModal;
import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.RecursiveRadioGroup;
import com.akshay.minglishmantra_beta.ViewHolders.MainTestHolder;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PractiseTestActivity extends AppCompatActivity {


    RecyclerView recyclerViewRv;
    TextView submitNowBtnTv;

    private FirebaseRecyclerAdapter<TestDetailsModal, MainTestHolder> firebaseRecyclerAdapter = null;

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    TestListModal modal;
    String courseName,pushId,nodeStr;
    int noOfQue;


    //storing progress
    class practiseTestStruct
    {
        public String postId,correctOption;

        public practiseTestStruct() {

        }

        public practiseTestStruct(String postId, String correctOption) {
            this.postId = postId;
            this.correctOption = correctOption;
        }
    }

    ArrayList<practiseTestStruct> practisetestArrayList =new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practise_test);

        Gson gson = new Gson();
        modal = gson.fromJson(getIntent().getStringExtra("myjson"), TestListModal.class);

        courseName =getIntent().getStringExtra("course_name");
        pushId = getIntent().getStringExtra("id");
        //nodeStr = getIntent().getStringExtra("node");
        noOfQue = Integer.parseInt(getIntent().getStringExtra("noOfQue"));

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();


        TextView testNameTv = findViewById(R.id.activity_practise_test_testNameTv);
        TextView quesMarksTv = findViewById(R.id.activity_practise_test_quesMarksTv);
        TextView timeTv = findViewById(R.id.activity_practise_test_timeTv);
        recyclerViewRv = findViewById(R.id.activity_test_recyclerviewRv);
        submitNowBtnTv =findViewById(R.id.activity_practise_test_submitNowBtn);


        //update ui
        testNameTv.setText(modal.getSubject()+" | "+modal.getTestNanme());
        quesMarksTv.setText(modal.getQuestions()+" questions | "+modal.getMarks() + " Marks");


        mRef.child("students_metadata").child(mAuth.getCurrentUser().getUid()).child("test_progress").child(pushId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot snapshot : dataSnapshot.getChildren()){


                    practiseTestStruct postStruct =new practiseTestStruct(snapshot.getKey(),snapshot.child("correct_option").getValue().toString());
                    practisetestArrayList.add(postStruct);
                }


                if(practisetestArrayList.size()==noOfQue){

                    submitNowBtnTv.setVisibility(View.VISIBLE);
                }

                //setting up recyclerview
                //  recyclerView.setHasFixedSize(true);
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                manager.setReverseLayout(false);
                recyclerViewRv.setLayoutManager(manager);
                attachFirebaseRecycler();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});



        submitNowBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getResult();


            }});

    }


    private void attachFirebaseRecycler() {


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TestDetailsModal, MainTestHolder>(

                TestDetailsModal.class,
                R.layout.practisetest_question_single,
                MainTestHolder.class,
                mRef.child(courseName).child("test_questions").child(pushId)

        ) {
            @Override
            protected void populateViewHolder(final MainTestHolder holder, TestDetailsModal modal, final int position) {


                final String postId = getRef(position).getKey();


                //init question info

                holder.practiseQuestionNoTv.setText("Q."+(position+1));


                if(!modal.getQuestionUrl().equals("x")){
                    holder.practiseImageIv.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext())
                            .load(modal.getQuestionUrl())
                          //  .placeholder(R.drawable.loading4)
                            .into(holder.practiseImageIv);
                  //  Picasso.get().load(modal.getQuestionUrl()).placeholder(R.drawable.loading4).into(holder.practiseImageIv);
                    Log.d("hot",modal.getQuestionUrl());
                }

                if(!modal.getText1().equals("x")){
                    holder.practiseText1Tv.setVisibility(View.VISIBLE);
                    holder.practiseText1Tv.setText(modal.getText1());
                }

                if(!modal.getText2().equals("x")){
                    holder.practiseText2Tv.setVisibility(View.VISIBLE);
                    holder.practiseText2Tv.setText(modal.getText2());
                }





                if (checkContainsPost(postId)){


                    String option =practisetestArrayList.get(indexOfPost(postId)).correctOption;
                    if(option.equals("A")){

                        holder.practiseOptionA.setChecked(true);

                    }else if(option.equals("B")){

                        holder.practiseOptionB.setChecked(true);

                    }else  if(option.equals("C")){

                        holder.practiseOptionC.setChecked(true);

                    }else {

                        holder.practiseOptionD.setChecked(true);

                    }


                }





                holder.practiseRrg.setOnCheckedChangeListener(new RecursiveRadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RecursiveRadioGroup group, int checkedId) {

                        practiseTestStruct practiseTestStruct = null;

                        if(checkedId == R.id.practisetest_question_single_optionA){

                            practiseTestStruct =new practiseTestStruct(postId,"A");



                        }else if(checkedId == R.id.practisetest_question_single_optionB){


                            practiseTestStruct =new practiseTestStruct(postId,"B");


                        }else if(checkedId == R.id.practisetest_question_single_optionC){

                            practiseTestStruct =new practiseTestStruct(postId,"C");

                        }else{

                            practiseTestStruct =new practiseTestStruct(postId,"D");

                        }



                        if(checkContainsPost(postId)) {

                            practisetestArrayList.set(indexOfPost(postId),practiseTestStruct);

                        }else{

                            practisetestArrayList.add(practiseTestStruct);
                        }




                        if(practisetestArrayList.size()==noOfQue){

                            submitNowBtnTv.setVisibility(View.VISIBLE);
                        }


                    }});






            }
        };


        // firebaseRecyclerAdapter.
        recyclerViewRv.setAdapter(firebaseRecyclerAdapter);


    }


    @Override
    public void onBackPressed() {

        if (practisetestArrayList.size() > 0) {
            progessSavingDialog();
        }else{

            finish();

        }


    }

    private void progessSavingDialog() {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_simpleexit);


        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Saving progress...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        Window window = dialog.getWindow();
        // window.setGravity(Gravity.CENTER);

        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setTitle(null);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        final TextView nextTv =dialog.findViewById(R.id.dialog_simpleexit_nextTv);
        final TextView textTv =dialog.findViewById(R.id.dialog_simpleexit_textTv);

        int quesionsRemainedCount = Integer.parseInt(modal.getQuestions()) - practisetestArrayList.size();
        textTv.setText("Only "+quesionsRemainedCount+" questions left ! Are you sure you want to leave the test now? Your progress will be saved for the session.");


        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.hide();
                mProgressDialog.show();

                HashMap updateMap =new HashMap();

                for (int i = 0; i < practisetestArrayList.size(); i++) {

                    HashMap hashMap =new HashMap();
                    hashMap.put("correct_option",practisetestArrayList.get(i).correctOption);

                    String path = "/students_metadata/"+mAuth.getCurrentUser().getUid()+"/test_progress/"+pushId+"/"+practisetestArrayList.get(i).postId;
                    updateMap.put(path,hashMap);

                }



                mRef.updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if(task.isSuccessful()){

                            mProgressDialog.dismiss();
                            finish();


                        }

                    }});



            }});



        dialog.show();

    }


/*
    //for quiz
    private void getResult() {


        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Getting your result...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        mProgressDialog.show();

        mRef.child(courseName).child("").child(pushId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int countRight=0;

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    String postId =snapshot.getKey();
                    String correctOption =snapshot.child("correctOption").getValue().toString();
                    String selectedOption =practisetestArrayList.get(indexOfPost(postId)).correctOption;

                    if(correctOption.equals(selectedOption))
                        countRight++;


                }

                Map<String, Object> map = new HashMap<>();
                map.put("/students_metadata/" + mAuth.getCurrentUser().getUid() + "/post/" +pushId +"/quiz_status/", "VIEW RESULT");
                map.put("/students_metadata/" + mAuth.getCurrentUser().getUid() + "/post/" +pushId +"/quiz_marks/", ""+countRight);


                mRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        mProgressDialog.dismiss();

                        if(task.isSuccessful()){

                            finish();
                        }
                    }});






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});


    }



 */


    private void getResult() {


        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Getting your result...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        mProgressDialog.show();

        mRef.child(courseName).child("test_questions").child(pushId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int countRight=0;

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    String postId =snapshot.getKey();

                    if(checkContainsPost(postId)) {

                        String correctOption = snapshot.child("correctOption").getValue().toString();
                        String selectedOption = practisetestArrayList.get(indexOfPost(postId)).correctOption;

                        if (correctOption.equals(selectedOption))
                            countRight++;


                    }


                }

                Map<String, Object> map = new HashMap<>();
                map.put("/students_metadata/" + mAuth.getCurrentUser().getUid() + "/test/" +pushId +"/test_status/", "VIEW RESULT");
                map.put("/students_metadata/" + mAuth.getCurrentUser().getUid() + "/test/" +pushId +"/test_marks/", ""+countRight);


                mRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        mProgressDialog.dismiss();

                        if(task.isSuccessful()){

                            finish();
                        }
                    }});






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});


    }





    public Boolean checkContainsPost(String postId){


        for(practiseTestStruct obj : practisetestArrayList){

            if(obj.postId.equals(postId)){

                return true;

            }


        }


        return false;

    }


    public int indexOfPost(String postId){

        int i=0;
        for(practiseTestStruct obj : practisetestArrayList){

            if(obj.postId.equals(postId)){

                return i;

            }

            i++;

        }


        return -1;

    }




}
