package com.akshay.minglishmantra_beta.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.ImageView;
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
import com.google.android.material.navigation.NavigationView;
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

public class ClassTestActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    TextView submitNowBtnTv;

    private FirebaseRecyclerAdapter<TestDetailsModal, MainTestHolder> firebaseRecyclerAdapter2 =null;
    private RecyclerView recyclerView;


    TestListModal modal;
    String pushId,courseName;
    int noOfQue;

    //storing progress
    class classTestStruct
    {
        public String postId,correctOption;

        public classTestStruct() {

        }

        public classTestStruct(String postId, String correctOption) {
            this.postId = postId;
            this.correctOption = correctOption;
        }
    }

    ArrayList<classTestStruct> classtestArrayList =new ArrayList<>();






    GridLayout gridLayout;

    DrawerLayout drawer;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_test);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();

        Gson gson = new Gson();
        modal = gson.fromJson(getIntent().getStringExtra("myjson"), TestListModal.class);

        courseName =getIntent().getStringExtra("course_name");
        pushId = getIntent().getStringExtra("id");
        noOfQue = Integer.parseInt(getIntent().getStringExtra("noOfQue"));



        TextView testNameTv =findViewById(R.id.activity_class_test_testNameTv);
        recyclerView =findViewById(R.id.Activity_OnlineTest_rvQuestion);
        submitNowBtnTv =findViewById(R.id.drawer_onlinetest_layout_submitnow);


        //update ui
        testNameTv.setText(modal.getSubject()+" | "+modal.getTestNanme()+" | "+modal.getQuestions()+" Que");


        mRef.child("students_metadata").child(mAuth.getCurrentUser().getUid()).child("test_progress").child(pushId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){


                            classTestStruct struct =new classTestStruct(snapshot.getKey(),snapshot.child("correct_option").getValue().toString());
                            classtestArrayList.add(struct);
                        }


                        /*
                        if(classtestArrayList.size()==noOfQue){

                            submitNowBtnTv.setVisibility(View.VISIBLE);
                        }

                         */

                        //setting up recyclerview
                        LinearLayoutManager manager2 = new LinearLayoutManager(getApplicationContext());
                        manager2.setReverseLayout(false);
                        manager2.setOrientation(RecyclerView.HORIZONTAL);
                        recyclerView.setLayoutManager(manager2);
                        attachRecyclerview();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }});





        drawer = findViewById(R.id.onlinetest_drawer);
        navigationView = findViewById(R.id.navigationViewOnlineTest);

        gridLayout =navigationView.findViewById(R.id.main_drawer_header_onlinetest).findViewById(R.id.drawer_onlinetest_layout_gridlayout);

        gridLayout.removeAllViews();

        for(int i=0;i<noOfQue;i++){
            View child = LayoutInflater.from(this).inflate(R.layout.classtest_single_mcq, gridLayout, false);
            child.setId(i);
            TextView tv =child.findViewById(R.id.test_mcqround_single_textview);
            tv.setText(""+(i+1));
            final int finalI = i;

            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawer.closeDrawer(GravityCompat.START);
                    recyclerView.smoothScrollToPosition(finalI);
                }});


            gridLayout.addView(child);
        }


        ImageView dialpad =findViewById(R.id.Activity_OnlineTest_dialpad1);

        dialpad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }});



        submitNowBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getResult();


            }});


    }


    private void attachRecyclerview() {

        firebaseRecyclerAdapter2 = new FirebaseRecyclerAdapter<TestDetailsModal, MainTestHolder>(

                TestDetailsModal.class,
                R.layout.classtest_single_question,
                MainTestHolder.class,
                mRef.child(courseName).child("test_questions").child(pushId)

        ) {

            @Override
            protected void populateViewHolder(MainTestHolder holder, TestDetailsModal modal, int position) {


                final String postId = getRef(position).getKey();



                //init question info

                holder.classQuestionNoTv.setText("Q."+(position+1));

                if(!modal.getQuestionUrl().equals("x")){
                    holder.classImageIv.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext())
                            .load(modal.getQuestionUrl())
                          //  .placeholder(R.drawable.loading4)
                            .into(holder.classImageIv);
                    //Picasso.get().load(modal.getQuestionUrl()).placeholder(R.drawable.loading4).into(holder.classImageIv);
                }

                if(!modal.getText1().equals("x")){
                    holder.classText1Tv.setVisibility(View.VISIBLE);
                    holder.classText1Tv.setText(modal.getText1());
                }

                if(!modal.getText2().equals("x")){
                    holder.classText2Tv.setVisibility(View.VISIBLE);
                    holder.classText2Tv.setText(modal.getText2());
                }



                if (checkContainsPost(postId)){


                    String option =classtestArrayList.get(indexOfPost(postId)).correctOption;
                    if(option.equals("A")){

                        holder.classOptionA.setChecked(true);

                    }else if(option.equals("B")){

                        holder.classOptionB.setChecked(true);

                    }else  if(option.equals("C")){

                        holder.classOptionC.setChecked(true);

                    }else {

                        holder.classOptionD.setChecked(true);

                    }


                }






                holder.classRrg.setOnCheckedChangeListener(new RecursiveRadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RecursiveRadioGroup group, int checkedId) {

                        classTestStruct classTestStruct = null;

                        if(checkedId == R.id.classtest_question_single_option1){

                            classTestStruct =new classTestStruct(postId,"A");



                        }else if(checkedId == R.id.classtest_question_single_option2){


                            classTestStruct =new classTestStruct(postId,"B");


                        }else if(checkedId == R.id.classtest_question_single_option3){

                            classTestStruct =new classTestStruct(postId,"C");

                        }else{

                            classTestStruct =new classTestStruct(postId,"D");

                        }



                        if(checkContainsPost(postId)) {

                            classtestArrayList.set(indexOfPost(postId),classTestStruct);

                        }else{

                            classtestArrayList.add(classTestStruct);
                        }



                        /*

                        if(practisetestArrayList.size()==noOfQue){

                            submitNowBtnTv.setVisibility(View.VISIBLE);
                        }

                         */


                    }});







            }};

        // firebaseRecyclerAdapter.
        recyclerView.setAdapter(firebaseRecyclerAdapter2);

    }





    @Override
    public void onBackPressed() {

        if (classtestArrayList.size() > 0) {
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

        int quesionsRemainedCount = Integer.parseInt(modal.getQuestions()) - classtestArrayList.size();
        textTv.setText("Only "+quesionsRemainedCount+" questions left ! Are you sure you want to leave the test now? Your progress will be saved for the session.");

        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.hide();
                mProgressDialog.show();

                HashMap updateMap =new HashMap();

                for (int i = 0; i < classtestArrayList.size(); i++) {

                    HashMap hashMap =new HashMap();
                    hashMap.put("correct_option",classtestArrayList.get(i).correctOption);

                    String path = "/students_metadata/"+mAuth.getCurrentUser().getUid()+"/test_progress/"+pushId+"/"+classtestArrayList.get(i).postId;
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
                        String selectedOption = classtestArrayList.get(indexOfPost(postId)).correctOption;

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


        for(classTestStruct obj : classtestArrayList){

            if(obj.postId.equals(postId)){

                return true;

            }


        }


        return false;

    }


    public int indexOfPost(String postId){

        int i=0;
        for(classTestStruct obj : classtestArrayList){

            if(obj.postId.equals(postId)){

                return i;

            }

            i++;

        }


        return -1;

    }




}
