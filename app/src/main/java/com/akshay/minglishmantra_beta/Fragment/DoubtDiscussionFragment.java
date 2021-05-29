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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.ApplicationClass;
import com.akshay.minglishmantra_beta.Modal.CommentModal;

import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.ViewHolders.CommentHolder;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DoubtDiscussionFragment extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    CommentModal commentModal;
    String postId,commentId,course_name;

    RecyclerView recyclerViewRv;
    EditText commentEt;
    TextView sendTv;
    ProgressDialog mProgressDialog;

    private FirebaseRecyclerAdapter<CommentModal, CommentHolder> firebaseRecyclerAdapter = null;

    private ApplicationClass applicationClass;
    View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.doubt_discussion_fragment, container, false);
        mView =view;

        applicationClass = (ApplicationClass) getActivity().getApplication();

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("uploading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);


        Bundle args =getArguments();
        Gson gson = new Gson();

        commentModal = gson.fromJson(args.getString("myjson"), CommentModal.class);
        commentId =args.getString("comment_id");
        postId =args.getString("post_id");
        course_name =args.getString("course_name");


        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().getRoot();



        //vars

        ImageView senderImageIv =view.findViewById(R.id.doubt_discussion_fragment_senderImageIv);
        TextView senderTv =view.findViewById(R.id.doubt_discussion_fragment_senderTv);
        TextView timeTv =view.findViewById(R.id.doubt_discussion_fragment_timeTv);
        TextView textTv =view.findViewById(R.id.doubt_discussion_fragment_textTv);
        ImageView commentImageIv=view.findViewById(R.id.doubt_discussion_fragment_ImageIv);
        ImageView backIv =view.findViewById(R.id.doubt_discussion_fragment_backIv);

        recyclerViewRv =view.findViewById(R.id.doubt_discusion_fragment_recyclerviewRv);
        commentEt =view.findViewById(R.id.doubt_discussion_fragment_commentEt);
        sendTv =view.findViewById(R.id.doubt_discussion_fragment_sendTv);


        //update ui
        Picasso.get().load(commentModal.getSender_image()).placeholder(R.drawable.profile_placeholder).into(senderImageIv);
        senderTv.setText(commentModal.getSender());
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String date =format1.format(Date.parse(commentModal.getTime()));
        timeTv.setText(date);
        textTv.setText(commentModal.getDoubtText());

        if(commentModal.getImageUrl().equals("x"))
           commentImageIv.setVisibility(View.GONE);

        else {
            Glide.with(getActivity())
                    .load(commentModal.getImageUrl())
                //    .placeholder(R.drawable.loading4)
                    .into(commentImageIv);
          //  Picasso.get().load(commentModal.getImageUrl()).into(commentImageIv);
        }



        //init recent searches rv
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(false);
        recyclerViewRv.setLayoutManager(manager);

        attachFirebaseRecycler();



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

                    mProgressDialog.show();
                    uploadChat(doubtStr);

                }

            }
        });



        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(mView).popBackStack();

            }});





        return view;
    }



    private void uploadChat(String doubtStr) {

        String pushId =mRef.push().getKey();
        final Calendar c = Calendar.getInstance();

        Map hashMap =new HashMap();

        hashMap.put("doubtText",doubtStr);
        hashMap.put("sender",applicationClass.getUserName());
        hashMap.put("sender_image",applicationClass.getImageUrl());
        hashMap.put("time",""+c.getTime());



        mRef.child(course_name).child("discussion_comment").child(commentId).child(pushId)
                .setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    commentEt.setText("");
                    sendTv.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.jast_grey))));

                }else{


                }

                mProgressDialog.dismiss();

            }});


    }




    private void attachFirebaseRecycler() {


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CommentModal, CommentHolder>(

                CommentModal.class,
                R.layout.doubt_discussion_single_discussion,
                CommentHolder.class,
                mRef.child(course_name).child("discussion_comment").child(commentId)

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
        recyclerViewRv.setAdapter(firebaseRecyclerAdapter);



    }



}
