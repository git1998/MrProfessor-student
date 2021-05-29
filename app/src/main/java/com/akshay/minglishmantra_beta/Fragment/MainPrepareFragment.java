package com.akshay.minglishmantra_beta.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.Activity.ClassroomActivity;
import com.akshay.minglishmantra_beta.Modal.PrepareModal;
import com.akshay.minglishmantra_beta.Modal.TestListModal;

import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.ViewHolders.BrowseCourseHolder;
import com.akshay.minglishmantra_beta.ViewHolders.TestHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainPrepareFragment extends Fragment {

    View mView;

    //
    Dialog dialog;
    TextView errorTv;


    RecyclerView selectUnitRv,recentWatchedRv;

    private FirebaseRecyclerAdapter<TestListModal, TestHolder> firebaseRecyclerAdapterRecentWatched = null;
    private FirebaseRecyclerAdapter<PrepareModal, BrowseCourseHolder> firebaseRecyclerAdapter = null;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;


    //
    String userId;
    HashMap courseHashmap =new HashMap();
    HashMap daysHashmap =new HashMap();
    HashMap buyDaysHashmap =new HashMap();

    //calender
    Calendar c ;
    int mYear;
    int mMonth ;
    int mDay ;
    int hour;
    int minute;

    //progress dialog
    ProgressDialog pd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.main_prepare_fragment, container, false);


        mAuth =FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().getRoot();
        userId =mAuth.getCurrentUser().getUid();


        pd = new ProgressDialog(getContext());
        pd.setMessage("Loading...");
        pd.setCancelable(false);


        //time date
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);



        mRef.child("student").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                courseHashmap.clear();
                daysHashmap.clear();
                buyDaysHashmap.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){


                    String endDateStr=snapshot.child("buycourse_enddate").getValue().toString();
                    if(! endDateStr.equals("x")){

                        String[] strings = endDateStr.split("/");

                        int day = Integer.parseInt(strings[0]);
                        int month = Integer.parseInt(strings[1]);
                        int year = Integer.parseInt(strings[2]);



                        Calendar testCalender =Calendar.getInstance();
                        testCalender.set(year,month-1,day);

                        long msDiff =  testCalender.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
                        int daysDiff = (int) TimeUnit.MILLISECONDS.toDays(msDiff);


                        //  Log.d("cat","days:"+(daysDiff+1));

                        buyDaysHashmap.put(snapshot.getKey(),""+(daysDiff));
                    }


                    String endDateStr1=snapshot.child("freetrial_enddate").getValue().toString();
                    if(! endDateStr1.equals("x")){

                        String[] strings = endDateStr1.split("/");

                        int day = Integer.parseInt(strings[0]);
                        int month = Integer.parseInt(strings[1]);
                        int year = Integer.parseInt(strings[2]);


                        Calendar testCalender =Calendar.getInstance();
                        testCalender.set(year,month-1,day);

                        long msDiff =  testCalender.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
                        int daysDiff = (int) TimeUnit.MILLISECONDS.toDays(msDiff);


                        //  Log.d("cat","days:"+(daysDiff+1));

                        daysHashmap.put(snapshot.getKey(),""+(daysDiff));

                    }


                    courseHashmap.put(snapshot.getKey(),snapshot.child("is_freetrial").getValue().toString());

                }




                recentWatchedRv =mView.findViewById(R.id.main_prepare_fragment_recentWatchedRv);
                //setting up recyclerview
                //  recyclerView.setHasFixedSize(true);
                LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
                manager.setStackFromEnd(true);
                recentWatchedRv.setLayoutManager(manager);
                attachFirebaseRecyclerRecentWatched();




                selectUnitRv =mView.findViewById(R.id.browse_choosecourse_fragment_courseRv);
                //setting up recyclerview
                //  recyclerView.setHasFixedSize(true);
                LinearLayoutManager manager1 =  new LinearLayoutManager(getContext());
                manager1.setReverseLayout(false);
                selectUnitRv.setLayoutManager(manager1);

                attachFirebaseRecycler();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});





        return mView;
    }

    private void attachFirebaseRecyclerRecentWatched() {

        firebaseRecyclerAdapterRecentWatched = new FirebaseRecyclerAdapter<TestListModal, TestHolder>(

                TestListModal.class,
                R.layout.prepare_single_recentwatched,
                TestHolder.class,
                mRef.child("test").limitToFirst(10)

        ) {
            @Override
            protected void populateViewHolder(final TestHolder holder, TestListModal modal, final int position) {




            }
        };


        // firebaseRecyclerAdapter.
        Log.d("abhi", "2");
        recentWatchedRv.setAdapter(firebaseRecyclerAdapterRecentWatched);


    }


    private void attachFirebaseRecycler() {

        Log.d("abhi","0");

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PrepareModal, BrowseCourseHolder>(

                PrepareModal.class,
                R.layout.prepare_single_course,
                BrowseCourseHolder.class,
                mRef.child("student").child(userId)

        ) {

            @Override
            protected void populateViewHolder(final BrowseCourseHolder holder, final PrepareModal modal, final int position) {

                final String course_name =getRef(position).getKey();
                holder.prepareTittleTv.setText(course_name);


                     String courseStatusStr ="x";
                     String courseStatusSpecificStr = "x";

                    if(courseHashmap.get(course_name).toString().equals("yes")){

                        holder.prepareStatusTv.setTextColor(getResources().getColor(R.color.primaryRed));
                        courseStatusStr="BUY NOW";
                        holder.prepareBuyCourseTv.setText("BUY NOW");

                        int daysNo = Integer.parseInt(daysHashmap.get(course_name).toString());

                        if(daysNo <= 0){

                            holder.prepareStatusTv.setText("Free trial ended");
                            courseStatusSpecificStr ="free_trial_ended";

                        }else {
                            holder.prepareStatusTv.setText("Free trial ends in "+(daysNo)+" days");
                            courseStatusSpecificStr ="free_trial";


                        }



                    }else{

                        holder.prepareStatusTv.setTextColor(getResources().getColor(R.color.colorPrimaryGreen));

                        int daysNo = Integer.parseInt(buyDaysHashmap.get(course_name).toString());

                        if(daysNo <= 0){

                            holder.prepareStatusTv.setText("Purchase ended");
                            holder.prepareBuyCourseTv.setText("PURCHASE AGAIN");
                            courseStatusStr="PURCHASE AGAIN";
                            holder.prepareBuyCourseTv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryYellow)));


                        }else {

                            holder.prepareStatusTv.setText("Course validity left "+(daysNo)+" days");
                            holder.prepareBuyCourseTv.setText("PURCHASED");
                            courseStatusStr="PURCHASED";
                            holder.prepareBuyCourseTv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

                        }


                    }



                    final String tempSpecificStr =courseStatusSpecificStr;
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(tempSpecificStr.equals("free_trial_ended")){

                                Toast.makeText(getContext(),"Please buy now course",Toast.LENGTH_LONG).show();
                                return;
                            }

                            Intent intent =new Intent(getActivity(), ClassroomActivity.class);
                            intent.putExtra("course_name",course_name);
                            startActivity(intent);


                        }});




                    final String tempStr =courseStatusStr;
                    holder.prepareMoreVertIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Creating the instance of PopupMenu
                            PopupMenu popup = new PopupMenu(getContext(), holder.prepareMoreVertIv);
                            //Inflating the Popup using xml file
                            popup.getMenuInflater()
                                    .inflate(R.menu.popup_menu, popup.getMenu());

                           MenuItem menuItem = popup.getMenu().findItem(R.id.popup_buy);
                           menuItem.setTitle(tempStr);
                           if(tempStr.equals("PURCHASED")){

                               menuItem.setEnabled(false);
                           }


                            //registering popup with OnMenuItemClickListener
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {

                                    if(item.getItemId() == R.id.popup_buy){

                                        showDialog(course_name);


                                    }


                                    return true;
                                }
                            });

                            popup.show(); //showing popup menu
                        }
                    }); //closing the setOnClickListener method





            }};

        // firebaseRecyclerAdapter.
        selectUnitRv.setAdapter(firebaseRecyclerAdapter);

    }



    private void showDialog(final String courseName) {

        dialog = new BottomSheetDialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.drawer_premium_dilog_buycourse);

        Window window = dialog.getWindow();
        // window.setGravity(Gravity.CENTER);

        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setTitle(null);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);


        final TextView tittleTv =dialog.findViewById(R.id.browse_dialog_buycourse_tittleTv);
        final LinearLayout addViewInstallmentll =dialog.findViewById(R.id.browse_dialog_buycourse_addViewInstallmentsTv);

        final LinearLayout courseInstallmentLl =dialog.findViewById(R.id.browse_dialog_buycourse_courseInstallmentLl);
        final LinearLayout redeemNowLl =dialog.findViewById(R.id.browse_dialog_buycourse_redeemNowLl);
        final LinearLayout contactLl =dialog.findViewById(R.id.browse_dialog_buycourse_contactRl);

        TextView getCouponTv =dialog.findViewById(R.id.browse_dialog_buycourse_getCouponTv);

        final EditText editTextEt =dialog.findViewById(R.id.browse_dialog_buycourse_edittextEt);
        final TextView applynowTv =dialog.findViewById(R.id.browse_dialog_buycourse_applynowTv);
        errorTv =dialog.findViewById(R.id.browse_dialog_buycourse_errorTv);

        //installment card vars
        final TextView parentMonthTv = dialog.findViewById(R.id.browse_dialog_single_installments_monthTv);
        final TextView parentOfferTv = dialog.findViewById(R.id.browse_dialog_single_installments_OfferTv);
        final TextView parentSavedMoneyTv = dialog.findViewById(R.id.browse_dialog_single_installments_savedMoneyTv);
        final TextView parentAmountsPerMo = dialog.findViewById(R.id.browse_dialog_single_installments_AmountPerMonthTv);
        final TextView parentTotalFeesTv = dialog.findViewById(R.id.browse_dialog_single_installments_totalFeesTv);




        //update ui
        tittleTv.setText("Buy course - "+courseName);



        //addview installments
        /*
        mRef.child(courseName).child("installments").orderByChild("month").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                addViewInstallmentll.removeAllViewsInLayout();

                int i=0;

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    final InstallmentModal modal =snapshot.getValue(InstallmentModal.class);

                    final View child = getLayoutInflater().inflate(R.layout.drawer_premium_dilog_single_installments, null);
                    child.setId(i);

                    final RelativeLayout parentRl =child.findViewById(R.id.browse_dialog_single_installment_parentRl);
                    TextView monthTv = child.findViewById(R.id.browse_dialog_single_installments_monthTv);
                    TextView offerTv = child.findViewById(R.id.browse_dialog_single_installments_OfferTv);
                    TextView savedMoneyTv = child.findViewById(R.id.browse_dialog_single_installments_savedMoneyTv);
                    TextView amountsPerMo = child.findViewById(R.id.browse_dialog_single_installments_AmountPerMonthTv);
                    TextView totalFeesTv = child.findViewById(R.id.browse_dialog_single_installments_totalFeesTv);

                    monthTv.setText(modal.getMonth()+" Month");
                    offerTv.setText(modal.getOffer() +"% OFF");
                    savedMoneyTv.setText("SAVE Rs."+modal.getSavedAmount());
                    amountsPerMo.setText("Rs."+modal.getAmountPerMonth()+" / MONTH");
                    totalFeesTv.setText("TOTAL FEES : "+modal.getTotalFees());


                    child.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            parentMonthTv.setText(modal.getMonth()+" Month");
                            parentOfferTv.setText(modal.getOffer() +"% OFF");
                            parentSavedMoneyTv.setText("SAVE Rs."+modal.getSavedAmount());
                            parentAmountsPerMo.setText("Rs."+modal.getAmountPerMonth()+" / MONTH");
                            parentTotalFeesTv.setText("TOTAL FEES : "+modal.getTotalFees());

                            tittleTv.setText("Redeem coupon");
                            tittleTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_chevron_left_black_24dp,0,0,0);

                            editTextEt.setFocusable(true);
                            courseInstallmentLl.setVisibility(View.GONE);
                            redeemNowLl.setVisibility(View.VISIBLE);



                            int p = child.getId();
                            parentRl.setBackground(getResources().getDrawable(R.drawable.box_cornered_whitefilled_green_jada));

                            for (int k = 0; k <= installmentCount; k++ ) {

                                if(k != p){

                                    RelativeLayout relativeLayout = addViewInstallmentll.getChildAt(k ).findViewById(R.id.browse_dialog_single_installment_parentRl);
                                    relativeLayout.setBackground(getResources().getDrawable(R.drawable.box_cornered_whitefilled_greystroke));

                                }


                            }



                        }});


                    i++;
                    addViewInstallmentll.addView(child);





                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});


         */



        /*

        tittleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tiitleStr =tittleTv.getText().toString();

                tittleTv.setText("Buy course - MPSC Pre 12 Months");
                tittleTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_black_24dp,0,0,0);

                if(tiitleStr.equals("Purchase coupon")){

                    courseInstallmentLl.setVisibility(View.VISIBLE);
                    contactLl.setVisibility(View.GONE);


                }else if(tiitleStr.equals("Redeem coupon")){

                    courseInstallmentLl.setVisibility(View.VISIBLE);
                    redeemNowLl.setVisibility(View.GONE);

                }else {


                    dialog.dismiss();
                }

            }});



         */

        /*
        getCouponTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tittleTv.setText("Purchase coupon");
                tittleTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_chevron_left_black_24dp,0,0,0);


                courseInstallmentLl.setVisibility(View.GONE);
                contactLl.setVisibility(View.VISIBLE);


            }});



         */


        editTextEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                applynowTv.setBackgroundTintList(getActivity().getColorStateList(R.color.colorPrimaryGreen));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }});


        applynowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editTextEt.clearFocus();

                if(editTextEt.getText().toString().trim().isEmpty()){
                    //   Toast.makeText(getContext(),"please type"+textViewTv.getText()+" before proceding",Toast.LENGTH_SHORT).show();
                    editTextEt.setError("You must fill required info");

                }else{

                    pd.show();
                   // String installmentName =parentMonthTv.getText().toString();
                    checkCouponOnCloud(editTextEt.getText().toString().trim(),courseName);


                }

            }});



        dialog.show();
    }


    private void checkCouponOnCloud(final String couponCodeStr, final String courseName) {

        mRef.child(courseName).child("coupons").orderByChild("name")
                .equalTo(couponCodeStr).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.getChildrenCount()==0){

                    errorTv.setText("Invalid coupon code");
                    errorTv.setTextColor(getResources().getColor(R.color.primaryRed));
                    pd.dismiss();
                }

                else {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String keyId = snapshot.getKey();
                        String is_used = snapshot.child("is_used").getValue().toString();

                        if (is_used.equals("yes")) {

                            errorTv.setText("Applied coupon is used alreday");
                            errorTv.setTextColor(getResources().getColor(R.color.primaryRed));

                        } else {


                            Map updatedUserData = new HashMap();
                            Calendar calendar =Calendar.getInstance();
                            calendar.add(Calendar.MONTH,2);

                            Map hashMap1 = new HashMap();
                            hashMap1.put("name", couponCodeStr);
                            hashMap1.put("is_used", "yes");

                            String singleRowPath = courseName+"/"+"coupons" + "/" + keyId;
                            updatedUserData.put(singleRowPath, hashMap1);

                            final Calendar myCalender = Calendar.getInstance();
                            int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                            int minute = myCalender.get(Calendar.MINUTE);

                            Map hashMap2 = new HashMap();
                            hashMap2.put("is_freetrial", "no");
                            hashMap2.put("freetrial_enddate", "x");
                            hashMap2.put("buycourse_enddate", (calendar.get(Calendar.DAY_OF_MONTH))+"/"+(calendar.get(Calendar.MONTH)+4)+"/"+calendar.get(Calendar.YEAR));
                            String singleRowPath2 = "student" + "/" + mAuth.getCurrentUser().getUid() + "/" + courseName;
                            updatedUserData.put(singleRowPath2, hashMap2);


                        /*
                        mRef.child("coupon").child(keyId).child("is_used")
                                .setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){


                                    mRef.child("student").child(mAuth.getCurrentUser().getUid()).child(courseName).child("is_freetrial")
                                            .setValue("no").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {



                                        }});



                                    Toast.makeText(getApplicationContext(),"Coupon applied successfully",Toast.LENGTH_SHORT);
                                    dialog.dismiss();

                                }

                            }});



                         */


                            mRef.updateChildren(updatedUserData).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {


                                    Toast.makeText(getContext(), "Coupon applied successfully", Toast.LENGTH_SHORT);
                                    dialog.dismiss();
                                    pd.dismiss();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    errorTv.setText("ERROR:"+e.getMessage());
                                    errorTv.setTextColor(getResources().getColor(R.color.primaryRed));
                                    pd.dismiss();
                                }
                            });


                            pd.dismiss();
                        }
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});

    }



}