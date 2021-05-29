package com.akshay.minglishmantra_beta.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Activity.LiveVideoActivity;
import com.akshay.minglishmantra_beta.Modal.LiveLecturesModal;
import com.akshay.minglishmantra_beta.R;

import com.akshay.minglishmantra_beta.ViewHolders.VideoListHolder;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RecentLiveAdapter extends RecyclerView.Adapter<VideoListHolder> {

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
    public RecentLiveAdapter(Context context, ArrayList<LiveLecturesModal> mData, DatabaseReference mRef, FirebaseAuth mAuth) {
        this.mInflater = LayoutInflater.from(context);
        mContex =context;
        this.mData =mData;

        //firebase
        this.mRef =mRef;
        this.mAuth =mAuth;

    }

    // inflates the row layout from xml when needed
    @Override
    public VideoListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.live_single_recentlectures2, parent, false);

        final VideoListHolder viewHolder = new VideoListHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListHolder holder, final int position) {

        final LiveLecturesModal modal = getItem(position);
        final String postId = modal.getPostId();
        final String course_name = modal.getCourse_name();


        if (modal.getType().equals("sub_topic")) {

            Glide.with(mContex)
                    .load(modal.getSender_image())
                    .placeholder(R.drawable.profile_placeholder)
                    .into(holder.senderImageIv);
           // Picasso.get().load(modal.getSender_image()).placeholder(R.drawable.profile_placeholder).into(holder.senderImageIv);

            holder.tittleTv.setText(modal.getSubject() + " | " + modal.getTopic() + " | " + modal.getSub_topic());
            holder.availableTv.setText("[Number] Joined");

            if(modal.getPdfUrl().equals("x")){
                holder.notesIv.setVisibility(View.GONE);
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

                    Gson gson = new Gson();
                    String myJson = gson.toJson(modal);

                    Intent intent = new Intent(mContex, LiveVideoActivity.class);
                    intent.putExtra("course_name", course_name);
                    intent.putExtra("liveId", postId);
                    intent.putExtra("myjson", myJson);
                    intent.putExtra("is_recent_lectures", "yes");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);

                }
            });


            holder.notesIv.setVisibility(View.GONE);

        }else{

            String subStr = modal.getDate();
            holder.tittleTv.setText(subStr);

            holder.playIv.setVisibility(View.GONE);
            holder.notesIv.setVisibility(View.GONE);
            holder.availableTv.setVisibility(View.GONE);
            holder.viewV.setVisibility(View.GONE);
            holder.chevRightIv.setVisibility(View.GONE);
            holder.imageCv.setVisibility(View.GONE);
            holder.chevRightIv.setVisibility(View.GONE);


        }

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

    public void setOnClickListener(RecentLiveAdapter.SavedState clickListener){
        mClickListener =clickListener;
    }






}
