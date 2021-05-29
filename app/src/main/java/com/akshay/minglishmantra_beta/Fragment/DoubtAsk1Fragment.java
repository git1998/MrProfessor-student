package com.akshay.minglishmantra_beta.Fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshay.minglishmantra_beta.ApplicationClass;
import com.akshay.minglishmantra_beta.Modal.TestModal;
import com.akshay.minglishmantra_beta.R;
import com.akshay.minglishmantra_beta.ViewHolders.CommonHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DoubtAsk1Fragment extends Fragment {

    TextView tittleText,textViewTv;
    RecyclerView recyclerView;

    private FirebaseRecyclerAdapter<TestModal, CommonHolder> firebaseRecyclerAdapter = null;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    Boolean isCourse=true,isPaper=false,isSubject=false;
    String selectedCourse,selectedPaper,selectedSubject;
    NavController navController;


    ProgressDialog mProgressDialog;
    String textStr;
    Uri imageUri;
    String imageURL;


    ApplicationClass applicationClass;

    View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.doubt_ask1_fragment, container, false);
        applicationClass = (ApplicationClass) getActivity().getApplicationContext();

        mAuth =FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().getRoot();

        tittleText =mView.findViewById(R.id.main_doubt_deep1_fragment_titleTv);
        textViewTv =mView.findViewById(R.id.main_doubt_deep1_fragment_textviewTv);
        ImageView backIv =mView.findViewById(R.id.doubt_ask1_fragment_backIv);

        recyclerView =mView.findViewById(R.id.rmain_doubt_deep1_fragment_recyclervireRv);
        //setting up recyclerview
        //  recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        String path ="student"+"/"+mAuth.getCurrentUser().getUid() ;
        attachFirebaseRecycler(path);


        Bundle args =getArguments();
        textStr =args.getString("text");
        imageUri = Uri.parse(args.getString("uri"));


        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if(isSubject){
                    tittleText.setText("Choose paper");
                    isPaper = true;
                    isSubject =false;
                    attachFirebaseRecycler("paper" + "/" + selectedCourse);


                }else if(isPaper){
                    tittleText.setText("Choose class");
                    isCourse = true;
                    isPaper =false;
                    String path ="student"+"/"+mAuth.getCurrentUser().getUid() ;
                    attachFirebaseRecycler(path);
                }else {

                    Navigation.findNavController(mView).popBackStack();
                }



            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()



        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSubject){
                    tittleText.setText("Choose paper");
                    isPaper = true;
                    isSubject =false;
                    attachFirebaseRecycler("paper" + "/" + selectedCourse);


                }else if(isPaper){
                    tittleText.setText("Choose class");
                    isCourse = true;
                    isPaper =false;
                    String path ="student"+"/"+mAuth.getCurrentUser().getUid() ;
                    attachFirebaseRecycler(path);
                }else {

                    Navigation.findNavController(mView).popBackStack();
                }

            }});



        return mView;
    }

    private void attachFirebaseRecycler(String s) {

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TestModal, CommonHolder>(

                TestModal.class,
                R.layout.doubt_ask1_single_text,
                CommonHolder.class,
                mRef.child(s)

        ) {
            @Override
            protected void populateViewHolder(final CommonHolder holder, final TestModal modal, final int position) {

                if(isCourse){

                    holder.tittleTv.setText(getRef(position).getKey());

                }else{
                    holder.tittleTv.setText(modal.getName());
                }



                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            if(isCourse) {
                                tittleText.setText("Choose paper");
                                isPaper = true;
                                isCourse =false;
                                selectedCourse =getRef(position).getKey();
                                attachFirebaseRecycler(selectedCourse +"/"+ "paper" );

                            }else if(isPaper){
                                tittleText.setText("Choose subject");
                                isSubject = true;
                                isPaper=false;
                                selectedPaper =modal.getName();
                                attachFirebaseRecycler(selectedCourse   +"/"+  "subject" + "/" + modal.getName());

                            }else if(isSubject){

                                selectedSubject =modal.getName();
                                uploadPhoto();

                            }


                        }});




            }};



        recyclerView.setAdapter(firebaseRecyclerAdapter);



    }









    private void uploadPhoto() {

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
                            uploadFinalData();

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
            uploadFinalData();

        }


    }



    private void uploadFinalData(){

        if(textStr.trim().isEmpty()){
            textStr ="x";
        }



        String pushId =mRef.push().getKey();

        Map updatedUserData = new HashMap();

        Map newPost = new HashMap();
        Calendar cal =Calendar.getInstance();

        newPost.put("course", selectedCourse);
        newPost.put("paper", selectedPaper);
        newPost.put("subject",selectedSubject);
        newPost.put("text1",textStr);
        newPost.put("imageUrl",imageURL);
        newPost.put("date",cal.getTime().toString());

        newPost.put("senderName",applicationClass.getUserName());
        newPost.put("senderProfileUrl",applicationClass.getImageUrl());


        String path = selectedCourse +"/"+ "doubts"+ "/" + pushId;

        updatedUserData.put(path, newPost);


        mRef.updateChildren(updatedUserData).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                mProgressDialog.dismiss();
                getActivity().finish();
              //  cleanNupPage();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"ERROR:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                getActivity();
               // cleanNupPage();
            }
        });
    }




    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }








}
