package com.akshay.minglishmantra_beta.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Activity.MainDetailedActivity;
import com.akshay.minglishmantra_beta.Activity.PractiseTestActivity;
import com.akshay.minglishmantra_beta.Fragment.MainPostFragment;
import com.akshay.minglishmantra_beta.Modal.PostModal;
import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.ViewHolders.PostHolder;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PostAdapter extends RecyclerView.Adapter<PostHolder> {

    private SavedState mClickListener;
    ProgressDialog mProgressDialog;

    public ArrayList<PostModal> mData =new ArrayList<>();
    ArrayList<MainPostFragment.postStruct> postStatusArrayList =new ArrayList<>();
    public Boolean isQuiz,isNotice;
    private LayoutInflater mInflater;
    public Context mContex;


    //firebase
    DatabaseReference mRef;
    FirebaseAuth mAuth;


    //
    Boolean isParamsAcquired=false;


    // data is passed into the constructor
    public PostAdapter(Context context, ArrayList<PostModal> mData, ArrayList<MainPostFragment.postStruct> postStatusArrayList, DatabaseReference mRef, FirebaseAuth mAuth, Boolean isNotice, Boolean isQuiz) {
        this.mInflater = LayoutInflater.from(context);
        mContex =context;
        this.mData =mData;
        this.postStatusArrayList =postStatusArrayList;

        this.isNotice =isNotice;
        this.isQuiz =isQuiz;

        //firebase
        this.mRef =mRef;
        this.mAuth =mAuth;

        mProgressDialog = new ProgressDialog(mContex);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);


    }


    // inflates the row layout from xml when needed
    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.post_single_post, parent, false);

        final PostHolder viewHolder = new PostHolder(view);

        viewHolder.setOnClickListener(new PostHolder.ClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(View view, final int position) {

                final PostModal modal =getItem(position);
                final String course_name =modal.getCourseName();
                final String postId =modal.getPostId();

                if(view.getId()==R.id.home_single_post_upvoteTv) {

                    /*
                    final String uploadText ;
                    TextView upvoteTv = (TextView) view;

                    if(viewHolder.upvoteTv.getText().toString().equals("SAVE POST")) {

                        uploadText = "SAVED POST";
                        upvoteTv.setTextColor(mContex.getResources().getColor(R.color.colorPrimaryGreen));
                        upvoteTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bookmark_black_24dp,0,0,0);
                        upvoteTv.setCompoundDrawableTintList(ColorStateList.valueOf(mContex.getResources().getColor(R.color.colorPrimaryGreen)));
                        Toast.makeText(mContex,"You saved post",Toast.LENGTH_SHORT).show();

                    }else{

                        uploadText ="SAVE POST";
                        upvoteTv.setTextColor(mContex.getResources().getColor(R.color.jast_grey));
                        upvoteTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bookmark_border_black_24dp,0,0,0);
                        upvoteTv.setCompoundDrawableTintList(ColorStateList.valueOf(mContex.getResources().getColor(R.color.grey)));
                        Toast.makeText(mContex,"You unsaved post",Toast.LENGTH_SHORT).show();
                    }


                    viewHolder.upvoteTv.setText(uploadText);
                    HashMap hashMap =new HashMap();
                    hashMap.put("quiz_status",checkContainsPostByReturnQuizStatus(postId));
                    hashMap.put("quiz_marks",checkContainsPostByReturnQuizMarks(postId));
                    hashMap.put("save_status",uploadText);


                    DatabaseReference databaseReference = mRef.child("students_metadata").child(mAuth.getCurrentUser()
                            .getUid()).child("post").child(postId);


                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                MainPostFragment.postStruct postStruct =new MainPostFragment.postStruct(postId,checkContainsPostByReturnQuizStatus(postId),uploadText,checkContainsPostByReturnQuizMarks(postId));

                                if(checkContainsPost(postId)) {

                                    postStatusArrayList.set(indexOfPost(postId),postStruct);

                                }else{

                                    postStatusArrayList.add(postStruct);
                                }


                            }
                        }});



                     */

                    Toast.makeText(mContex,"Feature unavailable",Toast.LENGTH_SHORT).show();

                }

                else if(view.getId()==R.id.home_single_post_commentTv){

                    Gson gson = new Gson();
                    String myJson = gson.toJson(modal);

                    Intent intent = new Intent(mContex, MainDetailedActivity.class);
                    intent.putExtra("fragment", "comment_post");
                    intent.putExtra("course_name",course_name );
                    intent.putExtra("id",postId );
                    intent.putExtra("myjson", myJson);
                    mContex.startActivity(intent);





                }else if(view.getId()==R.id.home_single_post_shareTv){

                    Toast.makeText(mContex,"Feature unavailable",Toast.LENGTH_SHORT).show();


                }else if(view.getId()==R.id.home_single_post_moreVertIv){

                    Toast.makeText(mContex,"Feature unavailable",Toast.LENGTH_SHORT).show();
                }



            }});


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, final int position) {


        final PostModal modal =getItem(position);
        final String course_name =modal.getCourseName();
        final String postId =modal.getPostId();

        //update ui
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String date =format1.format(Date.parse(modal.getDate()));


        holder.commentTv.setText(modal.getComments()+" COMMENTS");
        holder.date_posttypeTv.setText(date+" PM");
        holder.senderNameTv.setText(modal.getSender_name());
        Glide.with(mContex)
                .load(modal.getSender_image())
                .placeholder(R.drawable.profile_placeholder)
                .into(holder.senderImageIv);
      //  Picasso.get().load(modal.getSender_image()).placeholder(R.drawable.profile_placeholder).into(holder.senderImageIv);


        if(modal.getType().equals("notice")){

            if(isNotice) {


                holder.itemView.setVisibility(View.VISIBLE);
                RecyclerView.LayoutParams params=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.bottomMargin=convertDpToPixel(8,mContex);
                holder.itemView.setLayoutParams(params);

                /*

                ViewGroup.MarginLayoutParams marginLayoutParams =new ViewGroup.MarginLayoutParams(holder.itemView.getLayoutParams());
                //ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                marginLayoutParams.bottomMargin=convertDpToPixel(8,mContex);
                //   marginLayoutParams.setMargins(0,0,0,convertDpToPixel(8,mContex));


                ViewGroup.LayoutParams tempParams =holder.itemView.getLayoutParams();
                tempParams.height=ViewGroup.LayoutParams.WRAP_CONTENT;
                tempParams.width=ViewGroup.LayoutParams.MATCH_PARENT;

                 */



                holder.text1Tv.setVisibility(View.VISIBLE);
                holder.imageIv.setVisibility(View.VISIBLE);

                if (modal.getText().equals("x"))
                    holder.text1Tv.setVisibility(View.GONE);
                else
                    holder.text1Tv.setText(modal.getText());


                if (modal.getImageUrl().equals("x"))
                    holder.imageIv.setVisibility(View.GONE);

                else {

                    Glide.with(mContex)
                            .load(modal.getImageUrl())
                      //      .placeholder(R.drawable.loading4)
                            .into(holder.imageIv);
                //    Picasso.get().load(modal.getImageUrl()).into(holder.imageIv);


                }
            }else{


/*
                ViewGroup.MarginLayoutParams marginLayoutParams =new ViewGroup.MarginLayoutParams(holder.itemView.getLayoutParams());
                //ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                marginLayoutParams.bottomMargin=0;
                // marginLayoutParams.setMargins(0,0,0,0);

                ViewGroup.LayoutParams tempParams =holder.itemView.getLayoutParams();
                tempParams.height=0;
                tempParams.width=0;

 */


                holder.itemView.setVisibility(View.GONE);
                RecyclerView.LayoutParams params=new RecyclerView.LayoutParams(0, 0);
                params.bottomMargin=0;
                holder.itemView.setLayoutParams(params);

            }


        }else
        {

            if(isQuiz) {

                /*

                ViewGroup.MarginLayoutParams marginLayoutParams =new ViewGroup.MarginLayoutParams(holder.itemView.getLayoutParams());
                //ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                marginLayoutParams.bottomMargin=convertDpToPixel(8,mContex);
                //marginLayoutParams.setMargins(0,0,0,convertDpToPixel(8,mContex));

                ViewGroup.LayoutParams tempParams =holder.itemView.getLayoutParams();
                tempParams.height=ViewGroup.LayoutParams.WRAP_CONTENT;
                tempParams.width=ViewGroup.LayoutParams.MATCH_PARENT;

                 */

                holder.itemView.setVisibility(View.VISIBLE);
                RecyclerView.LayoutParams params=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.bottomMargin=convertDpToPixel(8,mContex);
                holder.itemView.setLayoutParams(params);




                holder.quizLl.setVisibility(View.VISIBLE);

                holder.quizTittleTv.setText(modal.getSubject());
                holder.quizQueTimeTv.setText(modal.getQuestions() + " Questions");


                final String quizStatus = checkContainsPostByReturnQuizStatus(postId);

                if (quizStatus.equals("RESUME")) {
                    holder.startQuizTv.setText("RESUME");
                    holder.startQuizTv.setBackgroundTintList(ColorStateList.valueOf(mContex.getResources().getColor(R.color.primaryYellow)));
                } else if (quizStatus.equals("VIEW RESULT")) {
                    holder.startQuizTv.setText("VIEW RESULT");
                    holder.startQuizTv.setBackgroundTintList(ColorStateList.valueOf(mContex.getResources().getColor(R.color.grey)));

                    holder.quizImageviewIv.setImageDrawable(mContex.getResources().getDrawable(R.drawable.ic_check_black_24dp));
                    holder.quizQueTimeTv.setText("Your score is " + postStatusArrayList.get(indexOfPost(postId)).quiz_marks + "/" + modal.getQuestions());

                } else {


                }


                holder.startQuizTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        mProgressDialog.show();

                        Gson gson = new Gson();
                        final String myJson = gson.toJson(modal);

                        if (quizStatus.equals("RESUME")) {

                            Intent intent = new Intent(mContex, PractiseTestActivity.class);
                            intent.putExtra("myjson", myJson);
                            intent.putExtra("course_name", course_name);
                            intent.putExtra("node", "quiz_questions");
                            intent.putExtra("noOfQue", modal.getQuestions());
                            intent.putExtra("id", postId);

                            mContex.startActivity(intent);

                            mProgressDialog.dismiss();


                        } else if (quizStatus.equals("VIEW RESULT")) {

                            mProgressDialog.dismiss();

                        } else {

                            HashMap hashMap = new HashMap();
                            hashMap.put("quiz_status", "RESUME");
                            hashMap.put("quiz_marks", "x");
                            hashMap.put("save_status", checkContainsPostByReturnSaveStatus(postId));


                            DatabaseReference databaseReference = mRef.child("students_metadata").child(mAuth.getCurrentUser()
                                    .getUid()).child("post").child(postId);


                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        MainPostFragment.postStruct postStruct = new MainPostFragment.postStruct(postId, "RESUME", checkContainsPostByReturnSaveStatus(postId), "x");

                                        if (checkContainsPost(postId)) {

                                            postStatusArrayList.set(indexOfPost(postId), postStruct);

                                        } else {

                                            postStatusArrayList.add(postStruct);
                                        }


                                    }

                                    Intent intent = new Intent(mContex, PractiseTestActivity.class);
                                    intent.putExtra("myjson", myJson);
                                    intent.putExtra("course_name", course_name);
                                    intent.putExtra("node", "quiz_questions");
                                    intent.putExtra("noOfQue", modal.getQuestions());
                                    intent.putExtra("id", postId);

                                    mContex.startActivity(intent);

                                    mProgressDialog.dismiss();


                                }
                            });


                        }


                    }
                });


            }   // if (isQuiz) loop ends
            else{


                /*
                ViewGroup.MarginLayoutParams marginLayoutParams =new ViewGroup.MarginLayoutParams(holder.itemView.getLayoutParams());
                //ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                marginLayoutParams.bottomMargin=0;

                ViewGroup.LayoutParams tempParams =holder.itemView.getLayoutParams();
                tempParams.height=0;
                tempParams.width=0;

                 */


                holder.itemView.setVisibility(View.GONE);
                RecyclerView.LayoutParams params=new RecyclerView.LayoutParams(0, 0);
                params.bottomMargin=0;
                holder.itemView.setLayoutParams(params);
            }



        }



        //bottom vars
        String saveStatus =checkContainsPostByReturnSaveStatus(postId);
        if(saveStatus.equals("SAVED POST")){

            holder.upvoteTv.setText("SAVED POST");
            holder.upvoteTv.setTextColor(mContex.getResources().getColor(R.color.colorPrimaryGreen));
            holder.upvoteTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bookmark_black_24dp,0,0,0);
            holder.upvoteTv.setCompoundDrawableTintList(ColorStateList.valueOf(mContex.getResources().getColor(R.color.colorPrimaryGreen)));

        }





    }



    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }



    // convenience method for getting data at click position
    PostModal getItem(int id) {
        return mData.get(id);
    }


    public interface SavedState {
        public void onItemClick(int position, boolean is_saved);
    }

    public void setOnClickListener(PostAdapter.SavedState clickListener){
        mClickListener =clickListener;
    }




    public String checkContainsPostByReturnQuizStatus(String postId){


        for(MainPostFragment.postStruct obj : postStatusArrayList){

            if(obj.postId.equals(postId)){


                return obj.quiz_status;

            }


        }


        return "START QUIZ";

    }


    public String checkContainsPostByReturnQuizMarks(String postId){


        for(MainPostFragment.postStruct obj : postStatusArrayList){

            if(obj.postId.equals(postId)){


                return obj.quiz_marks;

            }


        }


        return "x";

    }


    public String checkContainsPostByReturnSaveStatus(String postId){


        for(MainPostFragment.postStruct obj : postStatusArrayList){

            if(obj.postId.equals(postId)){


                return obj.save_status;

            }


        }


        return "SAVE POST";

    }




    public Boolean checkContainsPost(String postId){


        for(MainPostFragment.postStruct obj : postStatusArrayList){

            if(obj.postId.equals(postId)){

                return true;

            }


        }


        return false;

    }


    public int indexOfPost(String postId){

        int i=0;
        for(MainPostFragment.postStruct obj : postStatusArrayList){

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
