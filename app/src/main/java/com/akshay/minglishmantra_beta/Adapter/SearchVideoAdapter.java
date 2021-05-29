package com.akshay.minglishmantra_beta.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Activity.LiveVideoActivity;
import com.akshay.minglishmantra_beta.Modal.LiveLecturesModal;
import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.ViewHolders.VideoListHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SearchVideoAdapter extends RecyclerView.Adapter<VideoListHolder> {

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
    public SearchVideoAdapter(Context context, ArrayList<LiveLecturesModal> mData, DatabaseReference mRef, FirebaseAuth mAuth) {
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
        final View view = mInflater.inflate(R.layout.live_single_recentlectures, parent, false);

        final VideoListHolder viewHolder = new VideoListHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListHolder holder, final int position) {

        final LiveLecturesModal modal = getItem(position);
        final String postId = modal.getPostId();
        final String course_name = modal.getCourse_name();


        holder.availableTv.setText("[Number] watched");
        holder.tittleTv.setText(modal.getTopic() +" | "+modal.getSub_topic());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new Gson();
                String myJson = gson.toJson(modal);

                Intent intent = new Intent(mContex, LiveVideoActivity.class);
                intent.putExtra("course_name", course_name);
                intent.putExtra("liveId", postId);
                intent.putExtra("myjson", myJson);
                intent.putExtra("is_recent_lectures", "yes");
                mContex.startActivity(intent);

            }});



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

    public void setOnClickListener(SearchVideoAdapter.SavedState clickListener){
        mClickListener =clickListener;
    }






}
