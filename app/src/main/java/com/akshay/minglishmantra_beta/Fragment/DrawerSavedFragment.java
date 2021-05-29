package com.akshay.minglishmantra_beta.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.akshay.minglishmantra_beta.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class DrawerSavedFragment extends Fragment {


    public DrawerSavedFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.drawer_saved_fragment, container, false);


        final LinearLayout selectedSubjectLl =view.findViewById(R.id.fragment_question_addSubjectRootLinear);
        final RelativeLayout selectedTypeLl =view.findViewById(R.id.bookmarks_a_main_fragment_selectTypeLl);


        selectedTypeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchSelectTypeDialog();

            }});


        /*

        //addview() the single subject layout
        for(int i=0 ; i<5 ;i++) {

            final View child = getLayoutInflater().inflate(R.layout.doubt_single_subjectfilter, container, false);
            child.setId(i);
            selectedSubjectLl.addView(child);


            LinearLayout rootLl =child.findViewById(R.id.single_selectfiltersubject_root);
            final CardView subjectCv =child.findViewById(R.id.single_selectfiltersubject_cv);
            final ImageView subjectIcon =child.findViewById(R.id.single_selectfiltersubject_iv);
            TextView subjectText =child.findViewById(R.id.single_selectfiltersubject_tv);

            if(i==0){

                subjectCv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryYellow)));
                subjectIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardColor)));
                subjectText.setTextColor(getResources().getColor(R.color.black));
                subjectIcon.setImageResource(R.drawable.ic_star_white_24dp);
                subjectText.setText("All");

            }else if(i==1){

                subjectIcon.setImageResource(R.drawable.vibes_physics);
                subjectText.setText("Minglish Mantra - MPSC");

            }else if(i==2){

                subjectIcon.setImageResource(R.drawable.vibes_chem);
                subjectText.setText("Minglish Mantra - UPSC");

            }else if(i==3){

                subjectIcon.setImageResource(R.drawable.vibes_math);
                subjectText.setText("Minglish Mantra - पोलीस भरती");

            }else if(i==4){

                subjectIcon.setImageResource(R.drawable.vibes_biotech);
                subjectText.setText("Dehankar Classes - 12Th");
            }


            rootLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //  int id =  child.getId();

                    for(int k=0 ; k<5 ;k++) {

                        final CardView subjectCvTemp =selectedSubjectLl.getChildAt(k).findViewById(R.id.single_selectfiltersubject_cv);
                        final ImageView subjectIconTemp =selectedSubjectLl.getChildAt(k).findViewById(R.id.single_selectfiltersubject_iv);
                        final TextView subjectTextTemp =selectedSubjectLl.getChildAt(k).findViewById(R.id.single_selectfiltersubject_tv);

                        if(k==child.getId()){
                            subjectCvTemp.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryYellow)));
                            subjectIconTemp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardColor)));
                            subjectTextTemp.setTextColor(getResources().getColor(R.color.black));
                        }else {

                            subjectCvTemp.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.cardColor)));
                            subjectIconTemp.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.jastest_grey)));
                            subjectTextTemp.setTextColor(getResources().getColor(R.color.jast_grey));
                        }

                    }

                }});


        }

         */



        return view;

    }

    private void launchSelectTypeDialog() {

        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.drawer_saved_dialog_selecttype);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setTitle(null);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);


        dialog.show();



    }
}
