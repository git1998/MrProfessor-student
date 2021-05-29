package com.akshay.minglishmantra_beta.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Activity.LiveVideoActivity;
import com.akshay.minglishmantra_beta.Activity.PractiseTestActivity;
import com.akshay.minglishmantra_beta.Modal.LiveLecturesModal;
import com.akshay.minglishmantra_beta.R;

import com.akshay.minglishmantra_beta.ViewHolders.LiveLecturesHolder;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LiveAdapter extends RecyclerView.Adapter<LiveLecturesHolder> {

    private SavedState mClickListener;

    public ArrayList<LiveLecturesModal> mData =new ArrayList<>();
    private LayoutInflater mInflater;
    public Context mContex;

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    Calendar c =Calendar.getInstance();

    //firebase
    DatabaseReference mRef;
    FirebaseAuth mAuth;



    // data is passed into the constructor
    public LiveAdapter(Context context, ArrayList<LiveLecturesModal> mData, DatabaseReference mRef, FirebaseAuth mAuth) {
        this.mInflater = LayoutInflater.from(context);
        mContex =context;
        this.mData =mData;

        //firebase
        this.mRef =mRef;
        this.mAuth =mAuth;

    }

    // inflates the row layout from xml when needed
    @Override
    public LiveLecturesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.live_single_livelecturesnew, parent, false);

        final LiveLecturesHolder viewHolder = new LiveLecturesHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LiveLecturesHolder holder, final int position) {

        final LiveLecturesModal modal =getItem(position);
        final String postId =modal.getPostId();
        final String course_name =modal.getCourse_name();
        final String status ;


        //update ui

        Glide.with(mContex)
                .load(modal.getSender_image())
                .placeholder(R.drawable.profile_placeholder)
                .into(holder.senderImageIv);
       // Picasso.get().load(modal.getSender_image()).placeholder(R.drawable.profile_placeholder).into(holder.senderImageIv);

        if(!modal.getQuiz().equals("x")){
            holder.quizParentRl.setVisibility(View.VISIBLE); }


        if(modal.getPdfUrl().equals("x")){
            holder.notesIv.setVisibility(View.GONE);
        }

        String currentTimeStr =sdf.format(c.getTime());

        holder.tittleTv.setText(modal.getSubject() + " | " + modal.getTopic() + " | " + modal.getSub_topic());


        if (currentTimeStr.compareTo(modal.getStart_time()) < 0) {  //live not started yet
            holder.availableAtTv.setText("Video Available at " + modal.getStart_time());
            status ="wait";}


        else{

            holder.lockIv.setVisibility(View.GONE);

            if (currentTimeStr.compareTo(modal.getEnd_time()) < 0) {   // live started

                holder.availableAtTv.setText("Lecture is LIVE");
                holder.availableAtTv.setTextColor(mContex.getResources().getColor(R.color.primaryRed));
                status="live";}


            else{

                holder.quizLockIv.setVisibility(View.GONE);
                holder.availableAtTv.setTextColor(mContex.getResources().getColor(R.color.colorPrimaryGreen));
                holder.availableAtTv.setText("Video available");
                status ="avaiable";}


        }


        holder.notesIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(modal.getPdfUrl()), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Intent newIntent = Intent.createChooser(intent, "Open File");
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContex.startActivity(newIntent);

            }});



        holder.playIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(status.equals("wait")){


                    Toast.makeText(mContex,"Lecture not started yet !",Toast.LENGTH_SHORT).show(); }

                else{

                    Gson gson = new Gson();
                    String myJson = gson.toJson(modal);

                    Intent intent = new Intent(mContex, LiveVideoActivity.class);
                    intent.putExtra("course_name", course_name);
                    intent.putExtra("liveId", postId);
                    intent.putExtra("myjson", myJson);
                    intent.putExtra("is_recent_lectures", "no");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent); }
            }});



        holder.quizParentRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(status.equals("wait")){


                    Toast.makeText(mContex,"quiz will avaiable after lecture !",Toast.LENGTH_SHORT).show(); }

                else{


                    Intent intent =new Intent(mContex, PractiseTestActivity.class);
                    intent.putExtra("course_name", course_name);
                    intent.putExtra("node", "live_quiz_questions");
                    intent.putExtra("id",postId);
                    mContex.startActivity(intent); }
            }});



        holder.notesIv.setVisibility(View.GONE);


    }



    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }



    // convenience method for getting data at click position
    LiveLecturesModal getItem(int id) {
        return mData.get(id);
    }


    public interface SavedState {
        public void onItemClick(int position, boolean is_saved);
    }

    public void setOnClickListener(LiveAdapter.SavedState clickListener){
        mClickListener =clickListener;
    }






}
