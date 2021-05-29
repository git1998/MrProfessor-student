package com.akshay.minglishmantra_beta.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Activity.ClassTestActivity;
import com.akshay.minglishmantra_beta.Activity.PractiseTestActivity;
import com.akshay.minglishmantra_beta.Fragment.MainTestFragment;
import com.akshay.minglishmantra_beta.Modal.TestListModal;
import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.ViewHolders.TestListHolder;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TestRecentAdapter extends RecyclerView.Adapter<TestListHolder> {

    private SavedState mClickListener;
    ProgressDialog mProgressDialog;
    public String testFilter ="All";

    public ArrayList<TestListModal> mData =new ArrayList<>();
    ArrayList<MainTestFragment.testStruct> testStatusArrayList =new ArrayList<>();
    private LayoutInflater mInflater;
    public Context mContex;

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    Calendar c =Calendar.getInstance();

    //firebase
    DatabaseReference mRef;
    FirebaseAuth mAuth;

    Calendar cal = Calendar.getInstance();

    Date today = cal.getTime();



    // data is passed into the constructor
    public TestRecentAdapter(Context context, ArrayList<TestListModal> mData, ArrayList<MainTestFragment.testStruct> testStatusArrayList, DatabaseReference mRef, FirebaseAuth mAuth) {
        this.mInflater = LayoutInflater.from(context);
        mContex =context;
        this.mData =mData;
        this.testStatusArrayList =testStatusArrayList;

        //firebase
        this.mRef =mRef;
        this.mAuth =mAuth;


        //time
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);


        mProgressDialog = new ProgressDialog(mContex);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

    }

    // inflates the row layout from xml when needed
    @Override
    public TestListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.classroom_classtests_single_test, parent, false);

        final TestListHolder viewHolder = new TestListHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TestListHolder holder, final int position) {

        final TestListModal modal =getItem(position);
        final String postId =modal.getPostId();
        final String course_name =modal.getCourseName();
        final String testType =modal.getTestType();

        ViewGroup.LayoutParams tempParams =holder.itemView.getLayoutParams();

        if(testFilter.equals("All")){
            holder.itemView.setVisibility(View.VISIBLE);
            tempParams.height= ViewGroup.LayoutParams.WRAP_CONTENT;

        }else if(testFilter.equals("major test")){

            if(testType.equals("major test")) {
                holder.itemView.setVisibility(View.VISIBLE);
                tempParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
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



        //update ui
        holder.recentTestNameTv.setText(modal.getPaper()+" | "+modal.getTestNanme());
        holder.recentNoOfQueTv.setText(modal.getQuestions() +" Que");
        holder.recentDurationTv.setText("30 min");
        holder.recentMarksTv.setText(modal.getMarks());
        holder.recetDateTv.setText("Held on " +modal.getAvailable_date());


        holder.recentStartNow.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        holder.recentStartNow.setTextColor(mContex.getResources().getColor(R.color.cardColor));
        holder.recentStartNow.setBackground(mContex.getResources().getDrawable(R.drawable.box_cornered_googlebluefilled));

        final String testStatus =checkContainsPostByReturnTestStatus(postId);
        if(testStatus.equals("RESUME")){


            holder.recentStartNow.setText("RESUME");
            holder.recentStartNow.setBackgroundTintList(ColorStateList.valueOf(mContex.getResources().getColor(R.color.primaryYellow)));


        }else if(testStatus.equals("VIEW RESULT")){


            holder.recentStartNow.setText("Your Score "+testStatusArrayList.get(indexOfPost(postId)).test_marks+"/"+modal.getQuestions());
            holder.recentStartNow.setBackgroundTintList(ColorStateList.valueOf(mContex.getResources().getColor(R.color.grey)));


        }else{

            holder.recentStartNow.setText("START NOW");
            holder.recentStartNow.setBackgroundTintList(ColorStateList.valueOf(mContex.getResources().getColor(R.color.colorPrimaryGreen)));


        }




        holder.recentStartNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgressDialog.show();

                Gson gson = new Gson();
                final String myJson = gson.toJson(modal);


                if(testStatus.equals("RESUME")) {

                    Intent intent;
                    if(testType.equals("major test"))
                        intent= new Intent(mContex, ClassTestActivity.class);
                    else
                        intent= new Intent(mContex, PractiseTestActivity.class);

                    intent.putExtra("myjson", myJson);
                    intent.putExtra("course_name", course_name);
                    intent.putExtra("id",postId);
                    intent.putExtra("noOfQue", ""+modal.getQuestions());
                    mContex.startActivity(intent);

                    mProgressDialog.dismiss();


                }else if(testStatus.equals("VIEW RESULT")){


                    mProgressDialog.dismiss();

                }else{


                    HashMap hashMap=new HashMap();
                    hashMap.put("test_status","RESUME");
                    hashMap.put("test_marks","x");


                    DatabaseReference databaseReference = mRef.child("students_metadata").child(mAuth.getCurrentUser()
                            .getUid()).child("test").child(postId);


                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                MainTestFragment.testStruct testStruct = new MainTestFragment.testStruct(postId, "RESUME","x");

                                if (checkContainsPost(postId)) {

                                    testStatusArrayList.set(indexOfPost(postId), testStruct);

                                } else {

                                    testStatusArrayList.add(testStruct);
                                }


                                Intent intent;
                                if(testType.equals("major test"))
                                    intent= new Intent(mContex, ClassTestActivity.class);
                                else
                                    intent= new Intent(mContex, PractiseTestActivity.class);


                                intent.putExtra("myjson", myJson);
                                intent.putExtra("course_name", course_name);
                                intent.putExtra("id",postId);
                                intent.putExtra("noOfQue", ""+modal.getQuestions());
                                mContex.startActivity(intent);


                            }

                            mProgressDialog.dismiss();
                        }
                    });




                }



            }});







    }



    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }



    // convenience method for getting data at click position
    TestListModal getItem(int id) {
        return mData.get(id);
    }


    public interface SavedState {
        public void onItemClick(int position, boolean is_saved);
    }

    public void setOnClickListener(TestRecentAdapter.SavedState clickListener){
        mClickListener =clickListener;
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



    public static int convertDpToPixel(int dp, Context context){
        return dp * ((int) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }




}
