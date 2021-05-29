package com.akshay.minglishmantra_beta.Fragment;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.ApplicationClass;
import com.akshay.minglishmantra_beta.Modal.CommentModal;
import com.akshay.minglishmantra_beta.Modal.PostModal;
import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.ViewHolders.CommentHolder;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostCommentFragment extends Fragment {


    PostModal postModal;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;


    RecyclerView recyclerViewRv;

    private FirebaseRecyclerAdapter<CommentModal, CommentHolder> firebaseRecyclerAdapterHomeComment = null;


    EditText commentEt;
    TextView sendTv;

    String id;
    View mView;
    ProgressDialog mProgressDialog;
    private ApplicationClass applicationClass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mView = inflater.inflate(R.layout.post_comment_fragment, container, false);


        Bundle args =getArguments();
        id =args.getString("id");
        String courseName =args.getString("course_name");
        Gson gson = new Gson();
        postModal = gson.fromJson(args.getString("myjson"), PostModal.class);


        applicationClass = (ApplicationClass) getActivity().getApplication();
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child(courseName);

        //
        ImageView senderImageIv =mView.findViewById(R.id.post_comment_fragment_senderImgIv);
        TextView senderNameTv =mView.findViewById(R.id.post_comment_fragment_senderNameTv);
        TextView dateTimeTv =mView.findViewById(R.id.post_comment_fragment_dateTimeTv);
        TextView textTv =mView.findViewById(R.id.post_comment_fragment_textTv);
        ImageView postImageIv =mView.findViewById(R.id.post_comment_fragment_postImageIv);
        ImageView backIv =mView.findViewById(R.id.post_comment_fragment_backIv);


        //update ui
        Picasso.get().load(postModal.getSender_image()).into(senderImageIv);
        senderNameTv.setText(postModal.getSender_name());
        dateTimeTv.setText(postModal.getDate()+" "+postModal.getDuration());
        textTv.setText(postModal.getText());

        Glide.with(getActivity())
                .load(postModal.getImageUrl())
                .placeholder(R.drawable.loading4)
                .into(postImageIv);
       // Picasso.get().load(postModal.getImageUrl()).into(postImageIv);

        recyclerViewRv =mView.findViewById(R.id.doubt_comment_fragmenr_recyclerviewRv);
        commentEt =mView.findViewById(R.id.doubt_comment_fragmenr_startAnsweringEt);
        sendTv =mView.findViewById(R.id.doubt_comment_fragmenr_sendTv);

        //init recent searches rv
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(false);
        recyclerViewRv.setLayoutManager(manager);

        attachFirebaseRecyclerHomeComment();




        commentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(commentEt.getText().toString().trim().isEmpty())
                    sendTv.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.grey))));
                else
                sendTv.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.colorPrimaryGreen))));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String doubtStr = commentEt.getText().toString().trim();

                if (doubtStr.isEmpty()) {

                    commentEt.setError("Fill the text field");

                } else {


                    uploadChat(doubtStr);

                }

            }
        });



        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();

            }});



        return mView;
    }

    private void uploadChat(final String doubtStr) {

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("uploading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        final String pushId =mRef.push().getKey();
        final Calendar c = Calendar.getInstance();


        final Map updateMap =new HashMap();




        mRef.child("posts").child(id).child("comments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int count = Integer.parseInt(dataSnapshot.getValue().toString());
                    count++;
                    String path1 = "posts/" + id + "/comments";
                    updateMap.put(path1, "" + count);


                    Map hashMap = new HashMap();

                    hashMap.put("doubtText", doubtStr);
                    hashMap.put("sender", applicationClass.getUserName());
                    hashMap.put("sender_image",applicationClass.getImageUrl());
                    hashMap.put("time", "" + c.getTime());

                    String path2 = "post_comment/" + id + "/" + pushId;
                    updateMap.put(path2, hashMap);


                    mRef.updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (task.isSuccessful()) {

                                mProgressDialog.dismiss();
                                commentEt.setText("");
                                sendTv.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.jast_grey))));
                            }

                        }
                    });

                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});



    }





    private void attachFirebaseRecyclerHomeComment() {


        firebaseRecyclerAdapterHomeComment = new FirebaseRecyclerAdapter<CommentModal, CommentHolder>(

                CommentModal.class,
                R.layout.doubt_discussion_single_discussion,
                CommentHolder.class,
                mRef.child("post_comment").child(id)

        ) {
            @Override
            protected void populateViewHolder(final CommentHolder holder, CommentModal modal, final int position) {

                SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                String date =format1.format(Date.parse(modal.getTime()));


                Picasso.get().load(modal.getSender_image()).placeholder(R.drawable.profile_placeholder).into(holder.postSenderImageIv);
                holder.postTextTv.setText(modal.getDoubtText());
                holder.postTimeTv.setText(date);
                holder.postSenderTv.setText(modal.getSender());




            }};


        // firebaseRecyclerAdapter.
        recyclerViewRv.setAdapter(firebaseRecyclerAdapterHomeComment);



    }



}
