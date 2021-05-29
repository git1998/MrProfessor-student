package com.akshay.minglishmantra_beta.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.akshay.minglishmantra_beta.ApplicationClass;

import com.akshay.minglishmantra_beta.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {


    ProgressBar progressBar;
    ApplicationClass applicationClass;

    private FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    private HashMap<String, Object> firebaseDefaultMap;
    public static final String VERSION_CODE_KEY = "latest_app_version";


    private FirebaseAuth mAuth;
    private DatabaseReference mRef;



    HashMap courseHashmap =new HashMap();
    HashMap daysHashmap =new HashMap();
    HashMap buyDaysHashmap =new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mAuth =FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().getRoot();


        if(mAuth.getCurrentUser() == null){

            Intent intent =new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
            return;

        }




        checkForUpdate();


    }




    private void checkCourses(){



        FirebaseUser user = mAuth.getCurrentUser();
        applicationClass = (ApplicationClass) getApplicationContext();
        applicationClass.setUserName(user.getDisplayName());
        applicationClass.setImageUrl(String.valueOf(user.getPhotoUrl()));
        applicationClass.setMobileNumber(user.getPhoneNumber());

        if(user.getDisplayName().trim().isEmpty()){

        }

        mRef.child("student").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount() !=0) {

                    applicationClass.getEnrolledCourse().clear();
                    applicationClass.getEnrolledCourseValid().clear();


                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String course_name =snapshot.getKey();
                        applicationClass.getEnrolledCourse().add(course_name);


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

                            buyDaysHashmap.put(snapshot.getKey(),""+(daysDiff+1));
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

                            daysHashmap.put(snapshot.getKey(),""+(daysDiff+1));

                        }


                        courseHashmap.put(snapshot.getKey(),snapshot.child("is_freetrial").getValue().toString());




                        //validate data




                        if(courseHashmap.get(course_name).toString().equals("yes")){


                            int daysNo = Integer.parseInt(daysHashmap.get(course_name).toString());

                            if(daysNo <= 0){

                                //not to include in final application hashmap

                            }else {


                                applicationClass.getEnrolledCourseValid().add(course_name);


                            }



                        }else{


                            int daysNo = Integer.parseInt(buyDaysHashmap.get(course_name).toString());

                            if(daysNo <= 0){

                              //not to include in application hashmap


                            }else {

                                applicationClass.getEnrolledCourseValid().add(course_name);

                            }


                        }





                    }













                }

                Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }});

    }



    private void checkForUpdate() {
        firebaseDefaultMap = new HashMap<>();
        firebaseDefaultMap.put(VERSION_CODE_KEY, getCurrentVersionCode());
        mFirebaseRemoteConfig.setDefaults(firebaseDefaultMap);

        mFirebaseRemoteConfig.setConfigSettings(
                new FirebaseRemoteConfigSettings.Builder()
                        .build());
        mFirebaseRemoteConfig.fetch().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mFirebaseRemoteConfig.activateFetched();
                    //Log.d(TAG, "Fetched value: " + mFirebaseRemoteConfig.getString(VERSION_CODE_KEY));
                    //calling function to check if new version is available or not
                    checkForUpdateAvailability();
                } else {

                    checkCourses();

                }
            }
        });

        // Log.d(TAG, "Default value: " + mFirebaseRemoteConfig.getString(VERSION_CODE_KEY));
    }



    private void checkForUpdateAvailability() {
        int latestAppVersion = (int) mFirebaseRemoteConfig.getDouble(VERSION_CODE_KEY);
        if (latestAppVersion > getCurrentVersionCode()) {
            new AlertDialog.Builder(this).setTitle("Please Update the App")
                    .setMessage("A new version of this app is available. Please update it").setPositiveButton(
                    "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=com.nexm.lucidity")));

                        }
                    }).
                    setNegativeButton("Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            finish();

                        }
                    }).setCancelable(false).show();
        } else {
            // Toast.makeText(this,"This app is already up to date", Toast.LENGTH_SHORT).show();

            checkCourses();


        }
    }








    private int getCurrentVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }


}
