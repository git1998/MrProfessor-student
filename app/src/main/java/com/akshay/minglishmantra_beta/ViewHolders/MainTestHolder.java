package com.akshay.minglishmantra_beta.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.RecursiveRadioGroup;

public class MainTestHolder extends RecyclerView.ViewHolder {


    //class test
    public TextView classQuestionNoTv,classText1Tv,classText2Tv;
    public ImageView classImageIv;

    public RecursiveRadioGroup classRrg;
    public RadioButton classOptionA,classOptionB,classOptionC,classOptionD;


    //practise test
    public TextView practiseQuestionNoTv,practiseText1Tv,practiseText2Tv;
    public ImageView practiseImageIv;

    public RecursiveRadioGroup practiseRrg;
    public RadioButton practiseOptionA,practiseOptionB,practiseOptionC,practiseOptionD;


    public MainTestHolder(@NonNull View itemView) {
        super(itemView);


        //class test

        classQuestionNoTv =itemView.findViewById(R.id.classtest_single_question_queNoTv);
        classText1Tv =itemView.findViewById(R.id.classtest_single_question_text1Tv);
        classText2Tv =itemView.findViewById(R.id.classtest_single_question_text2Tv);
        classImageIv =itemView.findViewById(R.id.classtest_single_question_imageviewIv);

        classRrg =itemView.findViewById(R.id.classtest_question_single_rrg);
        classOptionA =itemView.findViewById(R.id.classtest_question_single_option1);
        classOptionB =itemView.findViewById(R.id.classtest_question_single_option2);
        classOptionC =itemView.findViewById(R.id.classtest_question_single_option3);
        classOptionD =itemView.findViewById(R.id.classtest_question_single_option4);






        //practise test

        practiseQuestionNoTv =itemView.findViewById(R.id.practise_question_single_queNoTv);
        practiseText1Tv =itemView.findViewById(R.id.practise_question_single_text1);
        practiseText2Tv =itemView.findViewById(R.id.practise_question_single_text2);
        practiseImageIv =itemView.findViewById(R.id.practise_question_single_questionImage);

        practiseRrg =itemView.findViewById(R.id.practisetest_question_single_rrg);
        practiseOptionA =itemView.findViewById(R.id.practisetest_question_single_optionA);
        practiseOptionB =itemView.findViewById(R.id.practisetest_question_single_optionB);
        practiseOptionC =itemView.findViewById(R.id.practisetest_question_single_optionC);
        practiseOptionD =itemView.findViewById(R.id.practisetest_question_single_optionD);


    }
}
