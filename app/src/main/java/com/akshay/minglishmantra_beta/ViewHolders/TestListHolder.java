package com.akshay.minglishmantra_beta.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.R;

public class TestListHolder extends RecyclerView.ViewHolder {

    public TextView testNameTv ,availableDateTv,noOfQueTv,marksTv,timeTv,startNowTv;

    //classtests_single
    public TextView recentTestNameTv,recentNoOfQueTv,recentDurationTv,recentMarksTv;
    public TextView recentStartNow,recetDateTv;


    public TestListHolder(@NonNull View itemView) {
        super(itemView);

        testNameTv =itemView.findViewById(R.id.test_single_upcomingtest_testNameTv);
        availableDateTv =itemView.findViewById(R.id.test_single_upcomingtest_availableDateTv);
        noOfQueTv =itemView.findViewById(R.id.test_single_upcomingtest_questionsTv);
        marksTv =itemView.findViewById(R.id.test_single_upcomingtest_marksTv);
        timeTv =itemView.findViewById(R.id.test_single_upcomingtest_timeTv);
        startNowTv =itemView.findViewById(R.id.test_single_upcomingtest_startNowTv);



        //classtests_single
        recentTestNameTv =itemView.findViewById(R.id.classroom_classtests_single_test_tittleTv);

        recentNoOfQueTv =itemView.findViewById(R.id.classroom_classtests_single_test_noOfQueTv);
        recentMarksTv =itemView.findViewById(R.id.classroom_classtests_single_test_marksTv);
        recentDurationTv =itemView.findViewById(R.id.classroom_classtests_single_test_timeTv);

        recentStartNow =itemView.findViewById(R.id.classroom_classtests_single_test_attemptNow);
        recetDateTv =itemView.findViewById(R.id.classroom_classtests_single_test_dateTv);

    }
}
