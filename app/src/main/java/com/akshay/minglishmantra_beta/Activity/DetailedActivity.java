package com.akshay.minglishmantra_beta.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.widget.TextView;

import com.akshay.minglishmantra_beta.R;

public class DetailedActivity extends AppCompatActivity {


    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        navController = Navigation.findNavController(this, R.id.host_detailed);
        navController.popBackStack();

        TextView tittleTv =findViewById(R.id.activity_detailed_titleTv);

        String fragmentName = getIntent().getStringExtra("fragment");

        if(fragmentName.equals("profile")){

             navController.navigate(R.id.profileFragment);
             tittleTv.setText("Profile");

         }else if(fragmentName.equals("notification")){

             navController.navigate(R.id.notificationFragment);
             tittleTv.setText("Notification");
        }

    }


}
