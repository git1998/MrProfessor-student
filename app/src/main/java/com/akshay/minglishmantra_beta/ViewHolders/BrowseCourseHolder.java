package com.akshay.minglishmantra_beta.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.R;

public class BrowseCourseHolder extends RecyclerView.ViewHolder {



    public TextView freeTrialTv,buyCourseTv,courseNameTv,freeTrialEndsInTv,daysTv,hrsTv;
    public LinearLayout freeTrialLl;


    //prepare
    public TextView prepareTittleTv, prepareBuyCourseTv,prepareStatusTv;
    public ImageView prepareMoreVertIv;

    public BrowseCourseHolder(@NonNull View itemView) {
        super(itemView);


        freeTrialTv =itemView.findViewById(R.id.browse_single_course_freeTrialTv);
        buyCourseTv =itemView.findViewById(R.id.browse_single_course_buyCourseTv);
        courseNameTv =itemView.findViewById(R.id.browse_single_course_courseNameTv);
        freeTrialLl =itemView.findViewById(R.id.browse_single_course_freeTrialLl);
        freeTrialEndsInTv =itemView.findViewById(R.id.browse_single_course_freeTrialEndsInTv);
        daysTv =itemView.findViewById(R.id.browse_single_course_daysTv);
        hrsTv =itemView.findViewById(R.id.browse_single_course_hrsTv);


        //prepare
        prepareMoreVertIv =itemView.findViewById(R.id.prepare_single_course_moreVertIv);
        prepareTittleTv =itemView.findViewById(R.id.prepare_single_course_tittleTv);
        prepareBuyCourseTv =itemView.findViewById(R.id.prepare_single_course_buyNowTv);
        prepareStatusTv =itemView.findViewById(R.id.prepare_single_course_statusTv);


    }
}
