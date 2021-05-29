package com.akshay.minglishmantra_beta.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.R;

public class CommentHolder extends RecyclerView.ViewHolder {

    //doubt
    public ImageView doubtSenderImageIv,doubtMainImageIv;
    public TextView doubtTextTv,doubtSenderTv,doubtTimeTv,doubtDiscussTv,doubtThanksTv;


    //post
    public ImageView postSenderImageIv;
    public TextView postTextTv,postSenderTv,postTimeTv;


    public CommentHolder(@NonNull View itemView) {
        super(itemView);


        //prepare
        doubtSenderImageIv =itemView.findViewById(R.id.doubt_comment_single_comment_imageIv);
        doubtSenderTv =itemView.findViewById(R.id.doubt_comment_single_comment_nameTv);
        doubtTimeTv =itemView.findViewById(R.id.doubt_comment_single_comment_dateTv);
        doubtTextTv =itemView.findViewById(R.id.doubt_comment_single_comment_textTv);
        doubtMainImageIv=itemView.findViewById(R.id.doubt_comment_fragment_mainImgIv);
        doubtDiscussTv =itemView.findViewById(R.id.doubt_comment_single_comment_discussTv);
        doubtThanksTv =itemView.findViewById(R.id.doubt_comment_single_comment_thanksTv);


        //search
        postSenderImageIv =itemView.findViewById(R.id.doubt_discussion_single_discussion_senderImageIV);
        postSenderTv =itemView.findViewById(R.id.doubt_discussion_single_discussion_nameTv);
        postTimeTv =itemView.findViewById(R.id.doubt_discussion_single_discussion_dateTv);
        postTextTv =itemView.findViewById(R.id.doubt_discussion_single_discussion_textTv);



    }
}
