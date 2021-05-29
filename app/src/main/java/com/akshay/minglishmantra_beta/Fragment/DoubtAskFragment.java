package com.akshay.minglishmantra_beta.Fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.akshay.minglishmantra_beta.FileUtil;
import com.akshay.minglishmantra_beta.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import id.zelory.compressor.Compressor;

public class DoubtAskFragment extends Fragment {

    private DatabaseReference ref;

    RelativeLayout imageRelativeRl;
    ImageView uploadImageIv;

    Uri imageUri= Uri.parse("x");
    String imageUrl="x";



    //new
    int i=0;


    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mView = inflater.inflate(R.layout.doubt_ask_fragment, container, false);


        //firebase
        ref = FirebaseDatabase.getInstance().getReference();



        //initialzing layout var--------------------------------------------------------------------


        LinearLayout addImageLl =mView.findViewById(R.id.main_doubt_deep0_fragment_addImageLl);
        uploadImageIv =mView.findViewById(R.id.main_upload_fragment_uploadImageIv);
        imageRelativeRl =mView.findViewById(R.id.main_upload_fragment_imageRelativeRl);
        final EditText questionTextEt =mView.findViewById(R.id.dialog_postask_questionTextEt);
        TextView uploadQuestionTv =mView.findViewById(R.id.dialog_postask_uploadQuestionTv);
        ImageView backIv =mView.findViewById(R.id.doubt_ask_fragment_backIv);



        //setOnClickListeners()-----------------------------------------------------------

        addImageLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPermission();
            }});



        uploadQuestionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text1=questionTextEt.getText().toString();

                if(text1.isEmpty() && imageUri.toString().equals("x")){

                    Toast.makeText(getContext(),"Upload image or write something , cannot upload blank post",Toast.LENGTH_SHORT).show();
                    return;
                }


                Bundle args =new Bundle();

                args.putString("text",text1);
                args.putString("uri", String.valueOf(imageUri));

                Navigation.findNavController(mView).navigate(R.id.mainDoubtDeep1Fragment,args);

                //uploadPhoto(text1);


            }});



        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();

            }});




        //post mcq



        return mView;
    }






    private void cleanNupPage() {

        imageUri = Uri.parse("x");




    }









    private void checkPermission() {

        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1888);


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("abhi", "resultCode=" + resultCode);

        if (resultCode == -1) {

            try {
                Log.d("abhi", "Compressed=" + getFolderSizeLabel(FileUtil.from(getContext(), data.getData())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                File compressedImage = new Compressor(getContext())
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(FileUtil.from(getContext(), data.getData()));

                imageRelativeRl.setVisibility(View.VISIBLE);
                Picasso.get().load(compressedImage).placeholder(R.drawable.loading4).into(uploadImageIv);
                imageUri = Uri.fromFile(compressedImage);
                Log.d("abhi", "Compressed=" + getFolderSizeLabel(compressedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }


            //    Picasso.get().load(uri).placeholder(R.drawable.loading4).into(noticeImageIv);


        }


    }



    public static String getFolderSizeLabel(File file) {
        long size = getFolderSize(file) / 1024; // Get size and convert bytes into Kb.
        if (size >= 1024) {
            return (size / 1024) + " Mb";
        } else {
            return size + " Kb";
        }
    }

    public static long getFolderSize(File file) {
        long size = 0;
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                size += getFolderSize(child);
            }
        } else {
            size = file.length();
        }
        return size;
    }



}
