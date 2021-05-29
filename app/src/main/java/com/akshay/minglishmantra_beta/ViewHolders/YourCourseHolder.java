package com.akshay.minglishmantra_beta.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.R;

public class YourCourseHolder extends RecyclerView.ViewHolder {



    public TextView freeTrialTv,buyCourseTv,courseNameTv,daysTv;

    public YourCourseHolder(@NonNull View itemView) {
        super(itemView);


        freeTrialTv =itemView.findViewById(R.id.main_single_yourcourses_endsInTv);
        buyCourseTv =itemView.findViewById(R.id.main_single_yourcourses_buyCourseTv);
        courseNameTv =itemView.findViewById(R.id.main_single_yourcourses_courseNameTv);
        daysTv =itemView.findViewById(R.id.main_single_yourcourses_daysTv);

    }
}
