package com.akshay.minglishmantra_beta.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.R;

public class DoubtHolder extends RecyclerView.ViewHolder {

    public TextView subjectOrPaperTv,dateAndTopicTv,text1Tv,answerBtnTv;
    public ImageView imageViewIv;




    public DoubtHolder(@NonNull View itemView) {
        super(itemView);


        subjectOrPaperTv =itemView.findViewById(R.id.doubt_single_doubt_subjectOrPaperTv);
        dateAndTopicTv =itemView.findViewById(R.id.doubt_single_doubt_dateAndTopicTv);
        text1Tv =itemView.findViewById(R.id.doubt_single_doubt_text1Tv);
        answerBtnTv =itemView.findViewById(R.id.doubt_single_doubt_answerBtnTv);

        imageViewIv =itemView.findViewById(R.id.doubt_single_doubt_imageviewIv);



    }
}
