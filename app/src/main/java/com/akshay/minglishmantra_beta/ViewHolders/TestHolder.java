package com.akshay.minglishmantra_beta.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.R;

public class TestHolder extends RecyclerView.ViewHolder {



    public LinearLayout prepareAddViewLl;

    public LinearLayout testAddviewLl;


    //search
    public ImageView imageViewIv;




    public TestHolder(@NonNull View itemView) {
        super(itemView);


        //prepare
        prepareAddViewLl =itemView.findViewById(R.id.main_prepare_single_recentlectures_addviewLl);
        testAddviewLl =itemView.findViewById(R.id.activity_test_addviewLl);

        //search
        imageViewIv =itemView.findViewById(R.id.search_fragment_single_imageIv);



    }
}
