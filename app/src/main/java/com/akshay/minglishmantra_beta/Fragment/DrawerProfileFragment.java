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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.akshay.minglishmantra_beta.ApplicationClass;
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

public class DrawerProfileFragment extends Fragment {


    //firebase
    FirebaseUser user;


    //image vars
    ImageView profileIv;
    Uri imageUri= Uri.parse("x");
    String imageUrl="x";

    LinearLayout yourCoursesLl;
    ViewGroup viewGroup;

     EditText fullnameEt , mobileNoEt;
     Button saveChangesBtn;

    View mView;
    ApplicationClass applicationClass;
    ProgressDialog pd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mView = inflater.inflate(R.layout.drawer_profile_fragment, container, false);

        pd = new ProgressDialog(getContext());
        pd.setMessage("Saving changes...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);

        user =FirebaseAuth.getInstance().getCurrentUser();


        viewGroup =container;
        applicationClass = (ApplicationClass) getActivity().getApplicationContext();


         profileIv =mView.findViewById(R.id.drawer_profile_fragment_imageviewIv);
         fullnameEt =mView.findViewById(R.id.drawer_profile_fragment_fullnameEt);
         mobileNoEt =mView.findViewById(R.id.drawer_profile_fragment_mobileNoEt);
         saveChangesBtn=mView.findViewById(R.id.drawer_profile_fragment_saveChangesTv);
        ImageView backIv =mView.findViewById(R.id.drawer_profile_fragment_backIv);


         //update UI
        imageUrl =applicationClass.getImageUrl();
        if(!applicationClass.getImageUrl().equals("x")){
            Picasso.get().load(applicationClass.getImageUrl()).placeholder(R.drawable.loading4).into(profileIv);
        }
        fullnameEt.setText(applicationClass.getUserName());
        mobileNoEt.setText(applicationClass.getMobileNumber());



        //select filter
        yourCoursesLl = mView.findViewById(R.id.main_prepare_fragment_addviewLl);
        for(int i=0 ; i<3 ; i++) {
            final View child = getLayoutInflater().inflate(R.layout.drawer_profile_single_yourcourses,viewGroup,false);
            child.setId(i);

            yourCoursesLl.addView(child);

        }

        fullnameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                saveChangesBtn.setVisibility(View.VISIBLE);

                if(fullnameEt.getText().toString().trim().isEmpty())

                    saveChangesBtn.setBackgroundTintList(getResources().getColorStateList(R.color.grey));

                    else
                        saveChangesBtn.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimaryGreen));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPermission();
            }});


        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameStr =fullnameEt.getText().toString().trim();

                if(nameStr.isEmpty()){
                    Toast.makeText(getContext(),"Full name cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                pd.show();
                uploadPhoto(nameStr);


            }});



        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();

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
                            uploadProfile(nameStr);

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                    pd.dismiss();
                    Toast.makeText(getContext(),"ERROR Photo upload:"+e.getMessage(),Toast.LENGTH_LONG).show();

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

            uploadProfile(nameStr);

        }


    }





    private void uploadProfile(final String nameStr) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nameStr)
                .setPhotoUri(Uri.parse(imageUrl))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            applicationClass.setUserName(nameStr);
                            applicationClass.setImageUrl(imageUrl);
                            Toast.makeText(getContext(),"Profile updated",Toast.LENGTH_SHORT).show();
                            saveChangesBtn.setVisibility(View.GONE);
                        }else{

                            Toast.makeText(getContext(),"ERROR:"+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }

                        pd.dismiss();
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
                saveChangesBtn.setVisibility(View.VISIBLE);
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
