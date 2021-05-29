package com.akshay.minglishmantra_beta.Fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Activity.ClassTestActivity;
import com.akshay.minglishmantra_beta.Activity.PractiseTestActivity;
import com.akshay.minglishmantra_beta.ApplicationClass;
import com.akshay.minglishmantra_beta.Modal.TestListModal;
import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.RecursiveRadioGroup;
import com.akshay.minglishmantra_beta.ViewHolders.TestListHolder;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ClassroomClasstestsFragment extends Fragment {

    ApplicationClass applicationClass;

    String testFilter="All";

    RecyclerView testRv;
    private FirebaseRecyclerAdapter<TestListModal, TestListHolder> firebaseRecyclerAdapter = null;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;


    private String selectCourseStr;


    //quiz
    Date today;
    ArrayList<MainTestFragment.testStruct> testStatusArrayList =new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.classroom_fragment_classtests, container, false);


        applicationClass = (ApplicationClass) getActivity().getApplicationContext();
        mAuth =FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();


        selectCourseStr =getActivity().getIntent().getStringExtra("course_name");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        today = cal.getTime();


        final RecursiveRadioGroup recursiveRadioGroup =view.findViewById(R.id.clssroom_fragment_classtests_subjectsRrg);


        testRv =view.findViewById(R.id.classroom_fragment_classtests_testRv);
        //setting up recyclerview
        //  recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        testRv.setLayoutManager(manager);



        mRef.child("students_metadata").child(mAuth.getCurrentUser().getUid()).child("test").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if( dataSnapshot.getChildrenCount()!=0) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MainTestFragment.testStruct postStruct =new MainTestFragment.testStruct(snapshot.getKey(),snapshot.child("test_status").getValue().toString(),snapshot.child("test_marks").getValue().toString());
                        testStatusArrayList.add(postStruct);
                    }

                }

                attachFirebaseRecycler();





                /* dynamic adding radiobuttons

                RadioButton radioButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.custom_radiobutton, null);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(20, 0, 0, 0);
                radioButton.setLayoutParams(params);
                radioButton.setText("All");
                radioButton.setChecked(true);

                recursiveRadioGroup.addView(radioButton);


                 */



                /*
                mRef.child("paper").child(selectCourseStr).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                            String paperNameStr =snapshot.child("name").getValue().toString();

                            RadioButton radioButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.custom_radiobutton, null);
                            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.setMargins(20, 0, 0, 0);
                            radioButton.setLayoutParams(params);
                            radioButton.setText(paperNameStr);

                            recursiveRadioGroup.addView(radioButton);

                        }





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }});


                 */



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});






        recursiveRadioGroup.setOnCheckedChangeListener(new RecursiveRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RecursiveRadioGroup group, int checkedId) {

                String filterTxt=recursiveRadioGroup.getCheckedItem().getText().toString();

                if(filterTxt.equals("All"))
                    testFilter = filterTxt;
                else if(filterTxt.equals("Test"))
                    testFilter ="major test";
                else
                    testFilter="topicwise test";
                firebaseRecyclerAdapter.notifyDataSetChanged();
            }});






        return view;
    }



    private void attachFirebaseRecycler() {


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TestListModal, TestListHolder>(

                TestListModal.class,
                R.layout.classroom_classtests_single_test,
                TestListHolder.class,
                mRef.child(selectCourseStr).child("test_list").orderByChild("timestamp")


        ) {

            @Override
            protected void populateViewHolder(final TestListHolder holder, final TestListModal modal, final int position) {



                    final String postId = getRef(position).getKey();
                final String testType =modal.getTestType();

                    //update ui
                    holder.recentTestNameTv.setText(modal.getPaper()+" | "+modal.getTestNanme());
                    holder.recentNoOfQueTv.setText(modal.getQuestions() +" Que");
                    holder.recentDurationTv.setText("30 min");
                    holder.recentMarksTv.setText(modal.getMarks() +" Marks");

                ViewGroup.LayoutParams tempParams =holder.itemView.getLayoutParams();

                if(testFilter.equals("All")){
                    holder.itemView.setVisibility(View.VISIBLE);
                    tempParams.height= ViewGroup.LayoutParams.WRAP_CONTENT;

                }else if(testFilter.equals("major test")){

                    if(testType.equals("major test")) {
                        holder.itemView.setVisibility(View.VISIBLE);
                        tempParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;;
                    }else{
                        holder.itemView.setVisibility(View.GONE);
                        tempParams.height=0;}
                }else {

                    if (testType.equals("topicwise test")){
                        holder.itemView.setVisibility(View.VISIBLE);
                        tempParams.height=ViewGroup.LayoutParams.WRAP_CONTENT;;}

                    else{
                        holder.itemView.setVisibility(View.GONE);
                        tempParams.height=0;}
                }





                String valid_until = modal.getAvailable_date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    final Boolean isUpcoming;

                    Date testDate = null;
                    try {


                        testDate = sdf.parse(valid_until);
                        if (today.after(testDate)) {   //recent tests

                            isUpcoming=false;
                            holder.recetDateTv.setText("Held on " +modal.getAvailable_date());
                            holder.recentStartNow.setTextColor(getResources().getColor(R.color.cardColor));
                            holder.recentStartNow.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }




                        else {

                            if (today.equals(testDate)) { //today

                                isUpcoming=false;
                                holder.recetDateTv.setText("Available now");
                                holder.recetDateTv.setTextColor(getResources().getColor(R.color.colorPrimaryGreen));
                                holder.recentStartNow.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                                holder.recentStartNow.setTextColor(getResources().getColor(R.color.cardColor));


                            } else {   //upcoming
                               isUpcoming=true;
                                holder.recetDateTv.setText("Available on " +modal.getAvailable_date());
                                holder.recetDateTv.setTextColor(getResources().getColor(R.color.jast_grey));

                            }



                        }


                        final String testStatus = checkContainsPostByReturnTestStatus(postId);
                        if(!isUpcoming) {
                            if (testStatus.equals("RESUME")) {


                                holder.recentStartNow.setText("RESUME");
                                holder.recentStartNow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryYellow)));


                            } else if (testStatus.equals("VIEW RESULT")) {

                                holder.recentStartNow.setText("Your Score " + testStatusArrayList.get(indexOfPost(postId)).test_marks + "/" + modal.getQuestions());
                                holder.recentStartNow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));


                            } else {

                                holder.recentStartNow.setText("START NOW");
                                holder.recentStartNow.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryGreen)));
                            }

                        }



                        holder.recentStartNow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(!isUpcoming) {


                                    Gson gson = new Gson();
                                    final String myJson = gson.toJson(modal);


                                    if (testStatus.equals("RESUME")) {

                                        Intent intent;
                                        if(testType.equals("major test"))
                                            intent= new Intent(getActivity().getApplicationContext(), ClassTestActivity.class);
                                        else
                                            intent= new Intent(getActivity().getApplicationContext(), PractiseTestActivity.class);

                                        intent.putExtra("myjson", myJson);
                                        intent.putExtra("course_name", selectCourseStr);
                                        intent.putExtra("id", getRef(position).getKey());
                                        intent.putExtra("noOfQue", "" + modal.getQuestions());
                                        startActivity(intent);

                                    } else if (testStatus.equals("VIEW RESULT")) {


                                    } else {


                                        HashMap hashMap = new HashMap();
                                        hashMap.put("test_status", "RESUME");
                                        hashMap.put("test_marks", "x");


                                        DatabaseReference databaseReference = mRef.child("students_metadata").child(mAuth.getCurrentUser()
                                                .getUid()).child("test").child(postId);


                                        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {

                                                    MainTestFragment.testStruct testStruct = new MainTestFragment.testStruct(postId, "RESUME", "x");

                                                    if (checkContainsPost(postId)) {

                                                        testStatusArrayList.set(indexOfPost(postId), testStruct);

                                                    } else {

                                                        testStatusArrayList.add(testStruct);
                                                    }


                                                    Intent intent;
                                                    if(testType.equals("major test"))
                                                        intent= new Intent(getActivity().getApplicationContext(), ClassTestActivity.class);
                                                    else
                                                        intent= new Intent(getActivity().getApplicationContext(), PractiseTestActivity.class);

                                                    intent.putExtra("myjson", myJson);
                                                    intent.putExtra("course_name", selectCourseStr);
                                                    intent.putExtra("id", getRef(position).getKey());
                                                    intent.putExtra("noOfQue", "" + modal.getQuestions());
                                                    startActivity(intent);

                                                }
                                            }
                                        });


                                    }


                                }else{ //upcoimg


                                    Toast.makeText(getContext(),"Avaliable on "+modal.getAvailable_date(),Toast.LENGTH_SHORT).show();

                                }



                            }});






                    } catch (ParseException e) {
                        e.printStackTrace();
                    }





            }};


        // firebaseRecyclerAdapter.
        testRv.setAdapter(firebaseRecyclerAdapter);

    }





    public String checkContainsPostByReturnTestStatus(String postId){


        for(MainTestFragment.testStruct obj : testStatusArrayList){

            if(obj.postId.equals(postId)){


                return obj.test_status;

            }


        }


        return "START NOW";

    }



    public String checkContainsPostByReturnTestMarks(String postId){


        for(MainTestFragment.testStruct obj : testStatusArrayList){

            if(obj.postId.equals(postId)){


                return obj.test_marks;

            }


        }


        return "x";

    }



    public Boolean checkContainsPost(String postId){


        for(MainTestFragment.testStruct obj : testStatusArrayList){

            if(obj.postId.equals(postId)){

                return true;

            }


        }


        return false;

    }


    public int indexOfPost(String postId){

        int i=0;
        for(MainTestFragment.testStruct obj : testStatusArrayList){

            if(obj.postId.equals(postId)){

                return i;

            }

            i++;

        }


        return -1;

    }




}
