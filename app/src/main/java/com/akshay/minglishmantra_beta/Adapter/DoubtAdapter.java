package com.akshay.minglishmantra_beta.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Activity.MainDetailedActivity;
import com.akshay.minglishmantra_beta.Fragment.MainPostFragment;
import com.akshay.minglishmantra_beta.Modal.DoubtModal;

import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.ViewHolders.DoubtHolder;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DoubtAdapter extends RecyclerView.Adapter<DoubtHolder> {

    private SavedState mClickListener;
    public String testFilter="All";

    public ArrayList<DoubtModal> mData =new ArrayList<>();
    ArrayList<MainPostFragment.postStruct> postStatusArrayList =new ArrayList<>();
    public ArrayList<Boolean> isSavedArray =new ArrayList<>();
    private LayoutInflater mInflater;
    public Context mContex;


    //firebase
    DatabaseReference mRef;
    FirebaseAuth mAuth;



    // data is passed into the constructor
    public DoubtAdapter(Context context, ArrayList<DoubtModal> mData, DatabaseReference mRef, FirebaseAuth mAuth) {
        this.mInflater = LayoutInflater.from(context);
        mContex =context;
        this.mData =mData;
        this.postStatusArrayList =postStatusArrayList;

        //firebase
        this.mRef =mRef;
        this.mAuth =mAuth;

    }

    // inflates the row layout from xml when needed
    @Override
    public DoubtHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.doubt_single_doubt, parent, false);

        final DoubtHolder viewHolder = new DoubtHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DoubtHolder holder, final int position) {

        final DoubtModal modal =getItem(position);
        final String postId =modal.getPostId();

        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String date =format1.format(Date.parse(modal.getDate()));



        ViewGroup.LayoutParams tempParams =holder.itemView.getLayoutParams();
        int pixel =convertDpToPixel(180,mContex);

        if(testFilter.equals("All")){
            holder.itemView.setVisibility(View.VISIBLE);
            tempParams.height=ViewGroup.LayoutParams.WRAP_CONTENT;

        }else if(testFilter.equals(modal.getCourse())){

            holder.itemView.setVisibility(View.VISIBLE);
            tempParams.height=ViewGroup.LayoutParams.WRAP_CONTENT;

        }else {

            holder.itemView.setVisibility(View.GONE);
            tempParams.height=0;

        }






        //update ui
        holder.subjectOrPaperTv.setText(modal.getPaper());
        holder.dateAndTopicTv.setText(date + "   ."+modal.getSubject());

        if(modal.getText1().equals("x"))
            holder.text1Tv.setVisibility(View.GONE);
        else
            holder.text1Tv.setText(modal.getText1());


        if(modal.getImageUrl().equals("x"))
            holder.imageViewIv.setVisibility(View.GONE);

        else {

            Glide.with(mContex)
                    .load(modal.getImageUrl())
                    .into(holder.imageViewIv);
        //    Picasso.get().load(modal.getImageUrl()).into(holder.imageViewIv);
        }



        holder.answerBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new Gson();
                String myJson = gson.toJson(modal);

                Intent intent = new Intent(mContex, MainDetailedActivity.class);
                intent.putExtra("fragment", "comment_doubt");
                intent.putExtra("myjson", myJson);
                mContex.startActivity(intent);

            }});



    }



    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }



    // convenience method for getting data at click position
    DoubtModal getItem(int id) {
        return mData.get(id);
    }


    public interface SavedState {
        public void onItemClick(int position, boolean is_saved);
    }

    public void setOnClickListener(DoubtAdapter.SavedState clickListener){
        mClickListener =clickListener;
    }





    public static int convertDpToPixel(int dp, Context context){
        return dp * ((int) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }



}
