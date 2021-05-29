package com.akshay.minglishmantra_beta.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.akshay.minglishmantra_beta.R;

public class MainDetailedActivity extends AppCompatActivity {

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detailed);


        navController = Navigation.findNavController(this, R.id.host_main_detailed);

        String fragmentName = getIntent().getStringExtra("fragment");

      if(fragmentName.equals("doubt")){

            navController.navigate(R.id.mainDoubtDeep0Fragment);

        }else if(fragmentName.equals("search")){

          navController.navigate(R.id.mainSearchFragment);


      }else if(fragmentName.equals("comment_doubt")){

          Bundle args =new Bundle();
          args.putString("post_id",getIntent().getStringExtra("post_id"));
          args.putString("comment_id",getIntent().getStringExtra("comment_id"));
          args.putString("course_name",getIntent().getStringExtra("course_name"));
          args.putString("myjson",getIntent().getStringExtra("myjson"));
          navController.navigate(R.id.doubtCommentFragment,args);


      }else if(fragmentName.equals("comment_post")){
          Bundle args =new Bundle();
          args.putString("id",getIntent().getStringExtra("id"));
          args.putString("course_name",getIntent().getStringExtra("course_name"));
          args.putString("myjson",getIntent().getStringExtra("myjson"));
          navController.navigate(R.id.postCommentFragment,args);


      }else if(fragmentName.equals("discussion")){

          navController.navigate(R.id.doubtCommentFragment);
      }





    }
}
