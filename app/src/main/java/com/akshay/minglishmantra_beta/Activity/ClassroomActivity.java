package com.akshay.minglishmantra_beta.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akshay.minglishmantra_beta.Adapter.ClassRoomTabAdapter;
import com.akshay.minglishmantra_beta.Fragment.ClassroomClasstestsFragment;
import com.akshay.minglishmantra_beta.Fragment.ClassroomMaterialFragment;

import com.akshay.minglishmantra_beta.R;
import com.google.android.material.tabs.TabLayout;

public class ClassroomActivity extends AppCompatActivity {

    private ClassRoomTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    int previousPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);


        String tittleStr =getIntent().getStringExtra("course_name");

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        TextView tittleTv =findViewById(R.id.activity_classroom_tittleTv);
        ImageView backIv =findViewById(R.id.activity_classroom_backIv);


        tittleTv.setText(tittleStr);


        adapter = new ClassRoomTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new ClassroomMaterialFragment(), "");
        adapter.addFragment(new ClassroomClasstestsFragment(), "");
       // adapter.addFragment(new ClassroomMaterialFragment(), "");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);




        TextView tab1 = (TextView) LayoutInflater.from(this).inflate(R.layout.classroom_custom_tab, null);
        tab1.setText("MATERIAL");
        tab1.setTextColor(getResources().getColor(R.color.colorPrimaryGreen));
        tabLayout.getTabAt(0).setCustomView(tab1);

        TextView tab2 = (TextView) LayoutInflater.from(this).inflate(R.layout.classroom_custom_tab, null);
        tab2.setText("TEST/WORKSHEET");
        tabLayout.getTabAt(1).setCustomView(tab2);

        /*

        TextView tab3 = (TextView) LayoutInflater.from(this).inflate(R.layout.classroom_custom_tab, null);
        tab3.setText("HOMEWORK");
        tabLayout.getTabAt(2).setCustomView(tab3);

         */



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {




            }

            @Override
            public void onPageSelected(int position) {

                if((previousPosition - position) == 0){



                }else if( (previousPosition - position) > 0 ){

                    previousPosition =position;


                    TabLayout.Tab tab =tabLayout.getTabAt(position);
                    TextView textView =tab.getCustomView().findViewById(R.id.tab);
                    textView.setTextColor(getColor(R.color.colorPrimaryGreen));


                    TabLayout.Tab preTab =tabLayout.getTabAt(position+1);
                    TextView preTextView =preTab.getCustomView().findViewById(R.id.tab);
                    preTextView.setTextColor(getColor(R.color.darkGrey));



                }else if( (previousPosition - position) < 0 ) {

                    previousPosition =position;

                    TabLayout.Tab tab =tabLayout.getTabAt(position);
                    TextView textView =tab.getCustomView().findViewById(R.id.tab);
                    textView.setTextColor(getColor(R.color.colorPrimaryGreen));


                    TabLayout.Tab preTab =tabLayout.getTabAt(position-1);
                    TextView preTextView =preTab.getCustomView().findViewById(R.id.tab);
                    preTextView.setTextColor(getColor(R.color.darkGrey));

                }




            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }});



        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               finish();

            }});


    }
}
