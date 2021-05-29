package com.akshay.minglishmantra_beta.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.ApplicationClass;
import com.akshay.minglishmantra_beta.FileUtil;
import com.akshay.minglishmantra_beta.Modal.CommentModal;
import com.akshay.minglishmantra_beta.Modal.DoubtModal;

import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.ViewHolders.CommentHolder;
import id.zelory.compressor.Compressor;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoubtCommentFragment extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private DoubtModal doubtModal;



    //comment camera cars

    Uri imageUri= Uri.parse("x");
    String imageUrl="x";
    String imageURL;

    private RelativeLayout captureRelativelayoutRl;
    private ImageView captureImageIv;
    private ImageButton captureDeleteImageIb;

    RecyclerView recyclerViewRv;

    private FirebaseRecyclerAdapter<CommentModal, CommentHolder> firebaseRecyclerAdapter = null;


    EditText commentEt;
    TextView sendTv;

    ProgressDialog mProgressDialog;

    View mView;
    private ApplicationClass applicationClass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mView = inflater.inflate(R.layout.doubt_comment_fragment, container, false);



        applicationClass = (ApplicationClass) getActivity().getApplication();
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().getRoot();

        Bundle args =getArguments();
        Gson gson = new Gson();
        doubtModal = gson.fromJson(args.getString("myjson"), DoubtModal.class);



        //pre vars
        TextView subjectOrPaperTv =mView.findViewById(R.id.doubt_comment_fragment_paperOrSubjectTv);
        TextView dateAndTopicTv =mView.findViewById(R.id.doubt_comment_fragment_dateAndTopicTv);
        TextView text1Tv =mView.findViewById(R.id.doubt_comment_fragment_text1Tv);
        ImageView imageViewIv =mView.findViewById(R.id.doubt_comment_fragment_imageviewIv);
        ImageView imageBtnIv =mView.findViewById(R.id.doubt_comment_fragment_commentsImgBtnIv);
        ImageView backIv =mView.findViewById(R.id.doubt_comment_fragment_backIv);


        captureRelativelayoutRl=mView.findViewById(R.id.doubt_comment_fragment_commentImgLayoutRl);
        captureImageIv=mView.findViewById(R.id.doubt_comment_fragment_commentsImgIv);
        captureDeleteImageIb=mView.findViewById(R.id.doubt_comment_fragment_commentsImgDeleteIb);


        recyclerViewRv =mView.findViewById(R.id.doubt_comment_fragmenr_recyclerviewRv);
        commentEt =mView.findViewById(R.id.doubt_comment_fragmenr_startAnsweringEt);
        sendTv =mView.findViewById(R.id.doubt_comment_fragmenr_sendTv);


        //update ui

        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        String date =format1.format(Date.parse(doubtModal.getDate()));

        //update ui
        subjectOrPaperTv.setText(doubtModal.getPaper());
        dateAndTopicTv.setText(date + "   ."+doubtModal.getSubject());

        if(doubtModal.getText1().equals("x"))
            text1Tv.setVisibility(View.GONE);
        else
            text1Tv.setText(doubtModal.getText1());


        if(doubtModal.getImageUrl().equals("x"))
            imageViewIv.setVisibility(View.GONE);

        else {
            Glide.with(getActivity())
                    .load(doubtModal.getImageUrl())
                  //  .placeholder(R.drawable.loading4)
                    .into(imageViewIv);
           // Picasso.get().load(doubtModal.getImageUrl()).into(imageViewIv);

        }



        //init recent searches rv
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(false);
        recyclerViewRv.setLayoutManager(manager);

        attachFirebaseRecycler();



        commentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(commentEt.getText().toString().trim().isEmpty())
                    sendTv.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.grey))));
                else
                sendTv.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.colorPrimaryGreen))));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String doubtStr = commentEt.getText().toString().trim();

                if (doubtStr.isEmpty() && imageUri.toString().equals("x")) {

                    commentEt.setError("cannot upload blank comment");

                } else {

                    uploadPhoto(doubtStr);

                }

            }
        });


        imageBtnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkPermission();

            }});

        captureDeleteImageIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageUri = Uri.parse("x");
                captureRelativelayoutRl.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Image Removed",Toast.LENGTH_SHORT).show();

            }});



        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().finish();

            }});



        return mView;
    }



    private void uploadPhoto(final String doubtStr) {

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("uploading...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


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
                            imageURL = uri.toString();
                            uploadChat(doubtStr);

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

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

            imageURL="x";
            uploadChat(doubtStr);

        }


    }


    private void uploadChat(String doubtStr) {

        String pushId =mRef.push().getKey();
        final Calendar c = Calendar.getInstance();

        Map hashMap =new HashMap();

        hashMap.put("doubtText",doubtStr);
        hashMap.put("sender",applicationClass.getUserName());
        hashMap.put("sender_image",applicationClass.getImageUrl());
        hashMap.put("imageUrl",imageURL);
        hashMap.put("time",""+c.getTime());



        mRef.child(doubtModal.getCourse()).child("doubt_comment").child(doubtModal.getPostId()).child(pushId)
                .setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                mProgressDialog.dismiss();


                if(task.isSuccessful()){

                    commentEt.setText("");
                    captureRelativelayoutRl.setVisibility(View.GONE);
                    imageUri=Uri.parse("x");
                    imageURL="x";
                    sendTv.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.jast_grey))));

                }else{


                }

            }});


    }


    private void attachFirebaseRecycler() {


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CommentModal, CommentHolder>(

                CommentModal.class,
                R.layout.doubt_comment_single_comment,
                CommentHolder.class,
                mRef.child(doubtModal.getCourse()).child("doubt_comment").child(doubtModal.getPostId())

        ) {
            @Override
            protected void populateViewHolder(final CommentHolder holder, final CommentModal modal, final int position) {

                final String commentPostId =getRef(position).getKey();

                SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                String date =format1.format(Date.parse(modal.getTime()));



                holder.doubtTextTv.setText(modal.getDoubtText());
                holder.doubtTimeTv.setText(date);
                holder.doubtSenderTv.setText(modal.getSender());
                Picasso.get().load(modal.getSender_image()).placeholder(R.drawable.profile_placeholder).into(holder.doubtSenderImageIv);


                if(modal.getDoubtText().equals("x"))
                    holder.doubtTextTv.setVisibility(View.GONE);
                else
                    holder.doubtTextTv.setText(modal.getDoubtText());


                if(modal.getImageUrl().equals("x"))
                    holder.doubtMainImageIv.setVisibility(View.GONE);

                else
                    Picasso.get().load(modal.getImageUrl()).into(holder.doubtMainImageIv);



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Gson gson = new Gson();
                        String myJson = gson.toJson(modal);

                        Bundle args =new Bundle();
                        args.putString("post_id",doubtModal.getPostId() );
                        args.putString("comment_id",commentPostId );
                        args.putString("course_name",doubtModal.getCourse() );
                        args.putString("myjson",myJson);

                        Navigation.findNavController(mView).navigate(R.id.doubtDiscussionFragment,args);

                    }});



            }};


        // firebaseRecyclerAdapter.
        recyclerViewRv.setAdapter(firebaseRecyclerAdapter);



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

                captureRelativelayoutRl.setVisibility(View.VISIBLE);
                Picasso.get().load(compressedImage).placeholder(R.drawable.loading4).into(captureImageIv);
                imageUri = Uri.fromFile(compressedImage);
                Log.d("abhi", "Compressed=" + getFolderSizeLabel(compressedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }


            //    Picasso.get().load(uri).placeholder(R.drawable.loading4).into(noticeImageIv);


        }


    }



    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
