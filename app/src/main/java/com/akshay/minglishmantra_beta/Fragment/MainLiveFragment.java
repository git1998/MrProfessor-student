package com.akshay.minglishmantra_beta.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Adapter.LiveAdapter;
import com.akshay.minglishmantra_beta.Adapter.RecentLiveAdapter;
import com.akshay.minglishmantra_beta.ApplicationClass;
import com.akshay.minglishmantra_beta.Modal.LiveLecturesModal;

import com.akshay.minglishmantra_beta.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainLiveFragment extends Fragment {


    ApplicationClass applicationClass;

    //
    RecyclerView liveSessionsRv,recentSessionsRv;
    LiveAdapter liveAdapter;
    RecentLiveAdapter recentLiveAdapter;

    TextView liveLoadingTv,recentLoadingTv;

    ArrayList<LiveLecturesModal> mData =new ArrayList<>();
    ArrayList<LiveLecturesModal> mDataRecent =new ArrayList<>();
    ArrayList<LiveLecturesModal> mDataRecentFinal =new ArrayList<>();
    ArrayList<String> arrayListDates =new ArrayList<>();


    private FirebaseAuth mAuth;
    private DatabaseReference mRef;


    //time
    String currentDateStr;
    Date today;

    //calender
    Calendar c ;
    int mYear;
    int mMonth ;
    int mDay ;
    SimpleDateFormat sdf,simpleDateFormat ;


    //progress dialog
    ProgressDialog pd;

    View mView;

    int count=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.main_live_fragment, container, false);


        mData.clear();
        mDataRecent.clear();
        mDataRecentFinal.clear();
        arrayListDates.clear();

        applicationClass = (ApplicationClass) getActivity().getApplicationContext();
        mAuth =FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().getRoot();



        //var init
        liveSessionsRv =mView.findViewById(R.id.main_prepare_fragment_liveSessionsRv);
        recentSessionsRv =mView.findViewById(R.id.main_prepare_fragment_RecentSessionsRv);
        liveLoadingTv =mView.findViewById(R.id.main_live_fragment_todayloadingTv);
        recentLoadingTv =mView.findViewById(R.id.main_live_fragment_recentloadingTv);



        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(false);


        //time date
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        today = cal.getTime();


        //getting time/date
        sdf = new SimpleDateFormat("HH:mm");
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        currentDateStr =c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);

        //setting adapters

        // set up the LIVE RecyclerView
        liveSessionsRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        liveAdapter = new LiveAdapter(getContext().getApplicationContext(),mData,mRef,mAuth);
        liveSessionsRv.setAdapter(liveAdapter);

        // set up the RECENT LIVE RecyclerView
        recentSessionsRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recentLiveAdapter = new RecentLiveAdapter(getContext().getApplicationContext(),mDataRecent,mRef,mAuth);
        recentSessionsRv.setAdapter(recentLiveAdapter);

        int count=0;


        for(final String courseName : applicationClass.getEnrolledCourseValid()){


            final int finalCount = count;

            mRef.child(courseName).child("live").orderByChild("date_time").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    LiveLecturesModal modal =dataSnapshot.getValue(LiveLecturesModal.class);
                    modal.setPostId(dataSnapshot.getKey());
                    modal.setCourse_name(courseName);
                    Date testDate = null;
                    try {
                         testDate = simpleDateFormat.parse(modal.getDate());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(today.after(testDate)) {   //recent sessions

                        Log.d("sasa","recent:"+modal.getDate_time());

                        String date = modal.getDate();
                        if(! arrayListDates.contains(date)){

                            arrayListDates.add(date);

                            Calendar cal =Calendar.getInstance();
                            String[] dateArr =modal.getDate().split("/");

                            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArr[0]));
                            cal.set(Calendar.MONTH, Integer.parseInt(dateArr[1]));
                            cal.set(Calendar.YEAR, Integer.parseInt(dateArr[2]));

                            cal.set(Calendar.HOUR_OF_DAY, 23);
                            cal.set(Calendar.MINUTE,50);
                            cal.set(Calendar.SECOND,0);
                            cal.set(Calendar.MILLISECOND,0);

                            LiveLecturesModal modal1 =new LiveLecturesModal("x","x","x","x","x",date,"23:50"
                                    ,"x","x","x","x","x","x",cal.getTimeInMillis(),"x","x","x");
                            modal.setPostId("x");
                            modal.setCourse_name("x");

                            int insertIndex = getInsertIndexDataRecent(modal1);
                            mDataRecent.add(insertIndex,modal1);
                            recentLiveAdapter.notifyItemInserted(insertIndex);
                        }


                        int insertIndex = getInsertIndexDataRecent(modal);
                        mDataRecent.add(insertIndex, modal);
                        recentLiveAdapter.notifyItemInserted(insertIndex);





                    }else { //upcoming tests


                        if(today.equals(testDate)) { //today

                            Log.d("sasa","today");

                            int insertIndex = getInsertIndexDate(modal);
                            mData.add(insertIndex, modal);
                            liveAdapter.notifyItemInserted(insertIndex);

                        }
                    }



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

                        if(mData.isEmpty())
                            liveLoadingTv.setText("No live session today");
                        else
                            liveLoadingTv.setVisibility(View.GONE);


                        if(mDataRecent.isEmpty())
                            recentLoadingTv.setText("No recent sessions found");
                        else
                            recentLoadingTv.setVisibility(View.GONE);


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }});



              count++;


        }








        return mView;

    }

    private int getInsertIndexDate(LiveLecturesModal modal) {


        Long timestamp =modal.getTimestamp();


        int tempCount=0;
        for (LiveLecturesModal tempModal : mData){  // 12 ,10 , 8 , 6 , 9


            Long innerTimestamp =tempModal.getTimestamp();


            if(timestamp > innerTimestamp){

                return tempCount;
            }


            tempCount++;
        }




        return tempCount;

    }





    private int getInsertIndexDataRecent(LiveLecturesModal modal) {


        Long timestamp =modal.getTimestamp();


        int tempCount=0;
        for (LiveLecturesModal tempModal : mDataRecent){  // 00:00 ,1:00 , 5:00 , 14:00


                Long innerTimestamp = tempModal.getTimestamp();


                if (timestamp > innerTimestamp) {

                    return tempCount;
                }


                tempCount++;

            }



        return tempCount;

    }








    @Override
    public void onStart() {
        super.onStart();


    }


    //Important methods

    public int indexOfMData(String postId){

        int i=0;
        for(LiveLecturesModal modal : mData){

            if(modal.postId.equals(postId)){

                return i;

            }

            i++;

        }


        return -1;

    }

    private void varInitForDataRecent() {

        /*

        for(LiveLecturesModal k : mDataRecent){

            Log.d("nanaBefore", k.getDate_time());

        }


        Collections.sort(mDataRecent, new Comparator<LiveLecturesModal>() {
            @Override
            public int compare(LiveLecturesModal lhs, LiveLecturesModal rhs) {

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                String lhsString =format.format(Date.parse(lhs.getDate()+" "+lhs.getStart_time()));
                String rhsString =format.format(Date.parse(rhs.getDate()+" "+lhs.getStart_time()));

                try {
                    Date date =format.parse(lhsString);
                    Long ttLhs =date.getTime();


                    try {
                        Date rhsDate =format.parse(rhsString);
                        Long ttRhs =rhsDate.getTime();


                        return ttLhs.compareTo(ttRhs);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;

            }
        });

        Collections.reverse(mDataRecent);
        String date = mDataRecent.get(0).getDate();
        LiveLecturesModal modal1 =new LiveLecturesModal("x","x","x","x","x",date,"x","x","x","x","x","x","x","x","x",1);
        mDataRecentFinal.add(modal1);
        int tempCount=0;

        for(LiveLecturesModal modal : mDataRecent){

            String tempDate =modal.getDate();


            if(tempDate.equals(date)){


            }else{

                date =tempDate;
                LiveLecturesModal modal2 =new LiveLecturesModal("x","x","x","x","x",date,"x","x","x","x","x","x","x","x","x",1);
                mDataRecentFinal.add(modal2);



            }

            mDataRecentFinal.add(mDataRecent.get(tempCount));


            tempCount++;

        }


        for(LiveLecturesModal k : mDataRecentFinal){

            Log.d("hashas", k.getDate_time());

        }



        // set up the RECENT LIVE RecyclerView
        recentSessionsRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        RecentLiveAdapter recentLiveAdapter = new RecentLiveAdapter(getContext().getApplicationContext(),mDataRecentFinal,mRef,mAuth);
        recentSessionsRv.setAdapter(recentLiveAdapter);


         */

    }

    private void varInitForData() {


        /*

        Log.d("puss",""+count);
        Collections.sort(mData, new Comparator<LiveLecturesModal>() {
            @Override
            public int compare(LiveLecturesModal lhs, LiveLecturesModal rhs) {

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                String lhsString =format.format(Date.parse(lhs.getDate()+" "+lhs.getStart_time()));
                String rhsString =format.format(Date.parse(rhs.getDate()+" "+lhs.getStart_time()));

                try {
                    Date date =format.parse(lhsString);
                    Long ttLhs =date.getTime();

                    try {
                        Date rhsDate =format.parse(rhsString);
                        Long ttRhs =rhsDate.getTime();


                        return ttLhs.compareTo(ttRhs);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;

            }
        });



        // set up the LIVE RecyclerView
        liveSessionsRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        LiveAdapter liveAdapter = new LiveAdapter(getContext().getApplicationContext(),mData,mRef,mAuth);
        liveSessionsRv.setAdapter(liveAdapter);



         */

    }




}
