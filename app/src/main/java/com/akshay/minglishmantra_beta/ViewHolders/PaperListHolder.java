package com.akshay.minglishmantra_beta.ViewHolders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.R;

public class PaperListHolder extends RecyclerView.ViewHolder {


    public LinearLayout addviewLl;
    public RelativeLayout parentRl;
    public TextView tittleTv;
    public ProgressBar progressBarPb;

    public PaperListHolder(@NonNull View itemView) {
        super(itemView);


        addviewLl =itemView.findViewById(R.id.enrolled_material_single_content_addViewLl);
        parentRl =itemView.findViewById(R.id.enrolled_material_single_content_parentRl);
        tittleTv =itemView.findViewById(R.id.enrolled_material_single_content_tittleTv);
        progressBarPb =itemView.findViewById(R.id.classroom_material_single_content_progressbarPb);

    }
}
