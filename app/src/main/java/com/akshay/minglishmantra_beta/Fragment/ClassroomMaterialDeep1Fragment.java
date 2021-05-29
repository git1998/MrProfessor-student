package com.akshay.minglishmantra_beta.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Modal.LiveLecturesModal;
import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.ViewHolders.VideoListHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClassroomMaterialDeep1Fragment extends Fragment {


    String selectedSubject;
    RecyclerView recentSessionsRv;

    private FirebaseRecyclerAdapter<LiveLecturesModal, VideoListHolder> firebaseRecyclerAdapterRecentSessions = null;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    ViewGroup mContainer;

    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.enrolled_fragment_material_deep1, container, false);

        mContainer =container;


        mAuth =FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().getRoot();


        //getArguments
        Bundle args = getArguments();
        selectedSubject =args.getString("subject");
        // topicName = args.getString("topic_name");

        recentSessionsRv =view.findViewById(R.id.main_prepare_fragment_RecentSessionsRv);
        //setting up recyclerview
        //  recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext());
        manager2.setReverseLayout(false);
        recentSessionsRv.setLayoutManager(manager2);

        attachFirebaseRecyclerRecentSessions();


        return view;
    }


    private void attachFirebaseRecyclerRecentSessions() {

        Log.d("abhi","0");

        firebaseRecyclerAdapterRecentSessions = new FirebaseRecyclerAdapter<LiveLecturesModal, VideoListHolder>(

                LiveLecturesModal.class,
                R.layout.live_single_recentlectures,
                VideoListHolder.class,
                mRef.child("videos").child(selectedSubject)

        ) {
            @Override
            protected void populateViewHolder(final VideoListHolder holder, LiveLecturesModal modal, int position) {


                if(modal.getType().equals("sub_topic")){

                    holder.tittleTv.setText(modal.getSub_topic());
                    holder.availableTv.setText("64 Watched");


                }else{

                    holder.tittleTv.setText(modal.getTopic());


                    holder.playIv.setVisibility(View.GONE);
                    holder.notesIv.setVisibility(View.GONE);
                    holder.availableTv.setVisibility(View.GONE);
                    holder.viewV.setVisibility(View.GONE);
                    holder.chevRightIv.setVisibility(View.GONE);
                    holder.imageCv.setVisibility(View.GONE);
                    holder.chevRightIv.setVisibility(View.GONE);

                }







                /*

                testHolder.prepareAddViewLl.removeAllViews();

                for(int i=0;i<4;i++) {
                    final View child = getLayoutInflater().inflate(R.layout.enrolled_material_deep1_single_content_in, mContainer, false);
                    // child.setId(i);
                    child.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //  Intent intent =new Intent(getContext(), LiveActivity.class);
                            //  startActivity(intent);

                        }});

                    testHolder.prepareAddViewLl.addView(child);

                }



                 */



            }};


        // firebaseRecyclerAdapter.
        Log.d("abhi","2");
        recentSessionsRv.setAdapter(firebaseRecyclerAdapterRecentSessions);

    }







}
