package com.akshay.minglishmantra_beta.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;

import com.akshay.minglishmantra_beta.R;

public class DrawerActivity extends AppCompatActivity {


    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);


        navController = Navigation.findNavController(this, R.id.host_drawer);
        navController.popBackStack();

        String fragmentName = getIntent().getStringExtra("fragment");

        if(fragmentName.equals("browse")){
            navController.navigate(R.id.drawerPremiumFragment);


        }else if(fragmentName.equals("profile")){
            navController.navigate(R.id.profileFragment);


        }else if(fragmentName.equals("my_notes")){
            navController.navigate(R.id.drawerSavedFragment);


        }else if(fragmentName.equals("payment")){
            navController.navigate(R.id.drawerPaymentFragment);

        }else if(fragmentName.equals("aboutapp")){
            navController.navigate(R.id.drawerAboutappFragment);

        }



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



}
