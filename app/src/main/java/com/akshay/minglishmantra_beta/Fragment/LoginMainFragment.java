package com.akshay.minglishmantra_beta.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.akshay.minglishmantra_beta.Adapter.StudyIntroViewPagerAdapter;

import com.akshay.minglishmantra_beta.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginMainFragment extends Fragment {


    //
    NavController navController;
    View mView;


    //
    EditText numberEt;

    String privacyUrlStr = "https://mr-professor-2b15c.web.app";

    //viewpager
    private ViewPager screenPager;
    StudyIntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    int position = 0;
    Animation btnAnim;


    //authentication

    private FirebaseAuth mAuth;
    private DatabaseReference mRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.login_main_fragment, container, false);


        showPrivacyDialog();



        mAuth =FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().getRoot();

        navController = Navigation.findNavController(getActivity(),R.id.login_host);


        //



         /*
        //viewpager
        tabIndicator = mView.findViewById(R.id.tab_indicator);
        screenPager = mView.findViewById(R.id.screen_viewpager);


        // fill list screen

        final List<StudyIntroScreenItem> mList = new ArrayList<>();
        mList.add(new StudyIntroScreenItem("", "","", R.drawable.classplash1));
        mList.add(new StudyIntroScreenItem("", "","", R.drawable.classplash2));
        mList.add(new StudyIntroScreenItem("", "","", R.drawable.classplash3));

        // setup viewpager

        introViewPagerAdapter = new StudyIntroViewPagerAdapter(getContext(), mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // setup tablayout with viewpager

        tabIndicator.setupWithViewPager(screenPager);

        // tablayout add change listener

        tabIndicator.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.transparent));


        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {


                tabIndicator.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.transparent));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // int tabIconColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
                //tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }});


         */

        final EditText numberEt =mView.findViewById(R.id.login_main_fragment_mobileNoEt);
        TextView continueTv =mView.findViewById(R.id.login_main_fragment_continueTv);
        TextView termsPolicyTv =mView.findViewById(R.id.login_main_fragment_termsPolicyTv);


        termsPolicyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrlStr));
                startActivity(browserIntent);

            }});



        continueTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mobileStr =numberEt.getText().toString();

                if (mobileStr.isEmpty()){

                    numberEt.setError("You must enter mobile number");
                    return;

                }else if(mobileStr.length()!=10){

                    numberEt.setError("Please enter valid mobile number");
                    return;
                }

                    Bundle args =new Bundle();
                    args.putString("mobile",mobileStr);
                    navController.navigate(R.id.loginOtpFragment,args);


            }});










        return mView;
    }

    private void showPrivacyDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_privacy);


        Window window = dialog.getWindow();
        // window.setGravity(Gravity.CENTER);

        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setTitle(null);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final TextView seePrivacyTv =dialog.findViewById(R.id.dialog_privacy_seePrivacyTv);
        final TextView exitTv =dialog.findViewById(R.id.dialog_privacy_exitTv);
        final TextView iAgreeTv =dialog.findViewById(R.id.dialog_privacy_iAgreeTv);



        seePrivacyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrlStr));
                startActivity(browserIntent);

            }});


        exitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                getActivity().finish();

            }});


        iAgreeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }});



        dialog.show();
    }


}
