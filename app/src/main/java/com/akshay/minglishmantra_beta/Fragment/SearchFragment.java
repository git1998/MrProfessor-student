package com.akshay.minglishmantra_beta.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Adapter.SearchVideoAdapter;
import com.akshay.minglishmantra_beta.ApplicationClass;
import com.akshay.minglishmantra_beta.Modal.LiveLecturesModal;
import com.akshay.minglishmantra_beta.Modal.TestModal;
import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.ViewHolders.TestHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {


    ApplicationClass applicationClass;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;


    RecyclerView recentSearchesRv,trendingRv,videosRv;

    private FirebaseRecyclerAdapter<TestModal, TestHolder> firebaseRecyclerAdapterRecentSearchesAdpater = null;
    private FirebaseRecyclerAdapter<TestModal, TestHolder> firebaseRecyclerAdapterTrendingAdapter = null;
    SearchVideoAdapter searchVideoAdapter;



    private ArrayList<LiveLecturesModal> mData =new ArrayList<>();

    private EditText searchEt;
    private TextView videosNoRsultTv;
    private RelativeLayout parentRecentSearchesRl,parentTrendingRl;
    private LinearLayout parentVideosRl;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.search_fragment, container, false);



        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().getRoot();
        applicationClass = (ApplicationClass) getActivity().getApplicationContext();


        recentSearchesRv =view.findViewById(R.id.search_fragment_recentSearchesRv);
        trendingRv =view.findViewById(R.id.search_fragment_trendingRv);
        videosRv =view.findViewById(R.id.search_fragment_videosRv);

        parentRecentSearchesRl =view.findViewById(R.id.search_fragment_parentRecentSearchRl);
        parentTrendingRl =view.findViewById(R.id.search_fragment_parentTrendingRl);
        parentVideosRl =view.findViewById(R.id.search_fragment_parentVideosRl);

        videosNoRsultTv =view.findViewById(R.id.search_fragment_videosNoResultTv);
        searchEt =view.findViewById(R.id.search_fragment_searchEt);



        //init recent searches rv
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(false);
        recentSearchesRv.setLayoutManager(manager);

        attachFirebaseRecyclerRecentSearches();

        //init trending rv
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        manager1.setReverseLayout(false);
        trendingRv.setLayoutManager(manager1);

        attachFirebaseRecyclerTrending();






        //init videos rv
        videosRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        searchVideoAdapter = new SearchVideoAdapter(getContext().getApplicationContext(),mData,mRef,mAuth);
        videosRv.setAdapter(searchVideoAdapter);



        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String searchStr =searchEt.getText().toString().trim();

                if(searchStr.isEmpty()){

                    parentRecentSearchesRl.setVisibility(View.GONE);
                    parentVideosRl.setVisibility(View.GONE);
                    videosNoRsultTv.setVisibility(View.VISIBLE);

                }else{

                    parentRecentSearchesRl.setVisibility(View.GONE);
                    parentTrendingRl.setVisibility(View.GONE);
                    parentVideosRl.setVisibility(View.VISIBLE);
                    videosNoRsultTv.setVisibility(View.GONE);

                    attachSearchVideosAdapter(searchStr);

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }});



        return view;
    }

    private void attachSearchVideosAdapter(String searchStr) {

        int count =0;
        mData.clear();
        videosRv.removeAllViews();

        for(final String courseName : applicationClass.getEnrolledCourse()){

            final int finalCount =count;

            mRef.child(courseName).child("live").orderByChild("topic").startAt(searchStr).endAt(searchStr+"\uf88f").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    LiveLecturesModal modal =dataSnapshot.getValue(LiveLecturesModal.class);
                    modal.setPostId(dataSnapshot.getKey());
                    modal.setCourse_name(courseName);

                    int insertIndex = mData.size();
                    mData.add(insertIndex, modal);
                    searchVideoAdapter.notifyItemInserted(insertIndex);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            mRef.child(courseName).child("live").addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(finalCount ==applicationClass.getEnrolledCourse().size()-1){

                        if(mData.isEmpty()) {
                            videosNoRsultTv.setVisibility(View.VISIBLE);
                            videosNoRsultTv.setText("No videos found ):");
                        }
                        else
                            videosNoRsultTv.setVisibility(View.GONE);




                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }});



            count++;

        }


    }


    private void attachFirebaseRecyclerRecentSearches() {


        firebaseRecyclerAdapterRecentSearchesAdpater = new FirebaseRecyclerAdapter<TestModal, TestHolder>(

                TestModal.class,
                R.layout.search_fragment_single_searches,
                TestHolder.class,
                mRef.child("test").limitToFirst(3)

        ) {
            @Override
            protected void populateViewHolder(final TestHolder holder, TestModal modal, final int position) {






            }};


        // firebaseRecyclerAdapter.
        Log.d("abhi","2");
        recentSearchesRv.setAdapter(firebaseRecyclerAdapterRecentSearchesAdpater);



    }




    private void attachFirebaseRecyclerTrending() {



        firebaseRecyclerAdapterTrendingAdapter = new FirebaseRecyclerAdapter<TestModal, TestHolder>(

                TestModal.class,
                R.layout.search_fragment_single_searches,
                TestHolder.class,
                mRef.child("test").limitToFirst(15)

        ) {
            @Override
            protected void populateViewHolder(final TestHolder holder, TestModal modal, final int position) {



                    holder.imageViewIv.setImageResource(R.drawable.ic_trending_up_black_24dp);


            }};


        // firebaseRecyclerAdapter.
        Log.d("abhi","2");
        trendingRv.setAdapter(firebaseRecyclerAdapterTrendingAdapter);



    }









}
