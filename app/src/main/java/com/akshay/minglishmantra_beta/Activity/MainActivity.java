package com.akshay.minglishmantra_beta.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.akshay.minglishmantra_beta.ApplicationClass;

import com.akshay.minglishmantra_beta.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    ImageView draverIv;
    DrawerLayout drawer;
    NavController navController;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    TextView tittleTextTv;


    private FirebaseAuth mAuth;
    private DatabaseReference mRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mAuth =FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().getRoot();


        bottomNavigationView = findViewById(R.id.bottomNavigation);
        navController = Navigation.findNavController(this,R.id.host_main);
        draverIv =findViewById(R.id.activity_main_drawerIv);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        tittleTextTv =findViewById(R.id.activity_main_titleTv);





    }


    @Override
    protected void onStart() {
        super.onStart();


        ApplicationClass applicationClass = (ApplicationClass)getApplicationContext();

        if(applicationClass.getEnrolledCourse().isEmpty()){

            showDialog();

        }


        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                if(destination.getId() == R.id.mainPostFragment){

                    tittleTextTv.setText("Doubts");


                }else if(destination.getId() == R.id.mainLiveFragment){

                    tittleTextTv.setText("Live");


                }else if(destination.getId() == R.id.mainTestFragment){

                    tittleTextTv.setText("Test");


                }else if(destination.getId() == R.id.mainPrepareFragment){

                    tittleTextTv.setText("Prepare");



                }else if(destination.getId() == R.id.mainHomeFragment) {

                    tittleTextTv.setText("Home");
                }


            }


        });



        draverIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer.openDrawer(GravityCompat.START);
            }});


        ImageView searchIv =findViewById(R.id.activity_main_searchIv);
        ImageView browseIv =findViewById(R.id.activity_main_browseIv);


        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(getApplicationContext(), MainDetailedActivity.class);
                intent.putExtra("fragment","search");
                startActivity(intent);
            }});



        //drawer init
        TextView nameTv =findViewById(R.id.drawer_header_nameTv);
        ImageView imageViewIv =findViewById(R.id.drawer_header_imageviewIv);
        TextView mobileNoTv =findViewById(R.id.drawer_header_mobileNoTv);
        RelativeLayout viewProfileRl =findViewById(R.id.top_profile_layout2);

        TableRow moreCoursesTr =findViewById(R.id.drawer_header_moreCoursesTr);
        TableRow savedNotesTr =findViewById(R.id.drawer_header_savedNotesTr);
        TableRow myPaymentTr =findViewById(R.id.drawer_header_myPaymentTr);

        TableRow aboutAppTr =findViewById(R.id.drawer_header_aboutAppTr);

        nameTv.setText(applicationClass.getUserName());
        if(!applicationClass.getImageUrl().equals("x")){
            Picasso.get().load(applicationClass.getImageUrl()).placeholder(R.drawable.loading4).into(imageViewIv);
        }
       // mobileNoTv.setText(applicationClass.getMobileNumber());


        viewProfileRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(getApplicationContext(), DrawerActivity.class);
                intent.putExtra("fragment","profile");
                startActivity(intent);

            }});


        moreCoursesTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(getApplicationContext(), DrawerActivity.class);
                intent.putExtra("fragment","browse");
                startActivity(intent);

            }});


        savedNotesTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(getApplicationContext(), DrawerActivity.class);
                intent.putExtra("fragment","my_notes");
                startActivity(intent);

            }});

        myPaymentTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(getApplicationContext(), DrawerActivity.class);
                intent.putExtra("fragment","payment");
                startActivity(intent);

            }});




        aboutAppTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(getApplicationContext(), DrawerActivity.class);
                intent.putExtra("fragment","aboutapp");
                startActivity(intent);

            }});



    }

    @Override
    public void onBackPressed() {


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }



    }


    private void showDialog() {

        final Dialog dialog = new BottomSheetDialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.main_dialog_addcourse);

        Window window = dialog.getWindow();
        // window.setGravity(Gravity.CENTER);

        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        dialog.setTitle(null);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        TextView goToCourseTv =dialog.findViewById(R.id.main_dialog_addcourse_goToCoursesTv);

        goToCourseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(), DrawerActivity.class);
                intent.putExtra("fragment","browse");
                startActivity(intent);
                dialog.dismiss();

            }});



        dialog.show();
    }


}
