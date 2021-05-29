package com.akshay.minglishmantra_beta.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.R;

public class PostHolder extends RecyclerView.ViewHolder {


    public TextView date_posttypeTv;
    public TextView text1Tv;
    public ImageView imageIv,moreVertIv;
    public LinearLayout quizLl;
    public TextView attemptUpvotesCommentTv;
    public TextView upvoteTv,commentTv,shareTv;

    public ImageView senderImageIv;
    public TextView senderNameTv;


    //quiz
    public TextView startQuizTv,quizTittleTv,quizQueTimeTv;
    public ImageView quizImageviewIv;

    public PostHolder(@NonNull View itemView) {
        super(itemView);

        //common
        date_posttypeTv =itemView.findViewById(R.id.home_single_post_date_posttype_tv);
        text1Tv =itemView.findViewById(R.id.home_single_post_text1Tv);
        imageIv =itemView.findViewById(R.id.home_single_post_imageIv);
        quizLl =itemView.findViewById(R.id.home_single_post_quizLl);
        attemptUpvotesCommentTv =itemView.findViewById(R.id.home_single_post_attempt_upvote_commentTv);
        moreVertIv =itemView.findViewById(R.id.home_single_post_moreVertIv);
        upvoteTv =itemView.findViewById(R.id.home_single_post_upvoteTv);
        commentTv =itemView.findViewById(R.id.home_single_post_commentTv);
        shareTv =itemView.findViewById(R.id.home_single_post_shareTv);

        senderImageIv =itemView.findViewById(R.id.post_single_post_senderImageIv);
        senderNameTv =itemView.findViewById(R.id.home_single_post_postByTv);


        //quiz
        startQuizTv =itemView.findViewById(R.id.home_single_post_startquizTv);
        quizTittleTv =itemView.findViewById(R.id.home_single_post_quizLl_tittleTv);
        quizQueTimeTv =itemView.findViewById(R.id.home_single_post_quizLl_questions_time_tv);
        quizImageviewIv =itemView.findViewById(R.id.home_single_post_quizLl_imageviewIv);




        //common mClicklisteners

        moreVertIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,getAdapterPosition());
            }});

        upvoteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,getAdapterPosition());
            }});

        commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,getAdapterPosition());
            }});

        shareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,getAdapterPosition());
            }});




    }




    private PostHolder.ClickListener mClickListener;

    public interface ClickListener {
        public void onItemClick(View view,int position);
    }

    public void setOnClickListener(PostHolder.ClickListener clickListener){
        mClickListener =clickListener;
    }


}
