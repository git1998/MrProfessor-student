package com.akshay.minglishmantra_beta.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.akshay.minglishmantra_beta.Activity.SplashActivity;
import com.akshay.minglishmantra_beta.FileUtil;
import com.akshay.minglishmantra_beta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LoginProfileFragment extends Fragment {


    //firebase
    FirebaseUser user;


    //image vars
    ImageView profileIv;
    Uri imageUri= Uri.parse("x");
    String imageUrl="x";


    ProgressDialog pd;

    View mView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.login_profile_fragment, container, false);

        pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading Profile...");
        pd.setCancelable(false);


        user = FirebaseAuth.getInstance().getCurrentUser();
        String userName =user.getDisplayName();
        imageUrl = String.valueOf(user.getPhotoUrl());

        final EditText nameEt =mView.findViewById(R.id.login_profile_fragment_nameEt);
        profileIv =mView.findViewById(R.id.login_profile_fragment_imageIv);
        TextView nextTv =mView.findViewById(R.id.login_profile_fragment_nextTv);


        //update ui
        nameEt.setText(userName);
        if(!imageUrl.equals("x")){
            Picasso.get().load(imageUrl).placeholder(R.drawable.loading4).into(profileIv);
        }



        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPermission();
            }});



        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameStr =nameEt.getText().toString().trim();

                if (nameStr.isEmpty()) {

                    nameEt.setError("Please type your name");
                    return;
                }


                pd.show();
                uploadPhoto(nameStr);


            }});


        return mView;
    }





    private void uploadPhoto(final String nameStr) {


        if (!imageUri.toString().equals("x")) {

            StorageReference fileReferenceQuestion = FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));


            fileReferenceQuestion.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUrl = uri.toString();
                            updateProfileAndNext(nameStr);

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                    pd.dismiss();
                    Toast.makeText(getContext(),"ERROR:"+e.getMessage(),Toast.LENGTH_LONG).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                /*
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                Log.d("progress", "onProgress: "+progress);
                   mProgressDialog.setProgress((int) progress);

                 */

                }
            });


        }else {

            updateProfileAndNext(nameStr);

        }


    }



    public void updateProfileAndNext(String nameStr){


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nameStr)
                .setPhotoUri(Uri.parse(imageUrl))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        pd.dismiss();

                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            Intent intent =new Intent(getContext(), SplashActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });

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

                Picasso.get().load(compressedImage).placeholder(R.drawable.profile_placeholder).into(profileIv);
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



    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }






}
