package com.akshay.minglishmantra_beta.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.akshay.minglishmantra_beta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LoginOtpFragment extends Fragment {

    NavController navController;

    String mobileNo;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    View mView;
    ProgressDialog pd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.login_otp_fragment, container, false);

        navController = Navigation.findNavController(getActivity(),R.id.login_host);

        pd = new ProgressDialog(getContext());
        pd.setMessage("Sending Otp...");
        pd.setCancelable(false);
        pd.show();

        Bundle args =getArguments();
        mobileNo =args.getString("mobile");

        final String phoneNumber = "+91" +mobileNo;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);


        final EditText otpNoEt =mView.findViewById(R.id.login_otp_fragment_otpEt);
        TextView numberInfoTv =mView.findViewById(R.id.login_otp_fragment_numberInfoTv);
        TextView nextTv =mView.findViewById(R.id.login_otp_fragment_nextTv);
        ImageView backIv =mView.findViewById(R.id.login_otp_fragment_backIv);

        numberInfoTv.setText( "Please enter the OTP sent to (+91) "+ mobileNo);

        nextTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (otpNoEt.getText().toString().trim().isEmpty()) {

                    otpNoEt.setError("Please enter OTP number");
                    return;
                }


                    pd.setMessage("Verifying OTP ...");
                    pd.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpNoEt.getText().toString().trim());
                    signInWithPhoneAuthCredential(credential);



            }});



        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(mView).popBackStack();

            }});




        return mView;
    }




    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + credential);

            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            // Show a message and update the UI
            // ...
            pd.dismiss();
            Toast.makeText(getContext(),"ERROR:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            navController.popBackStack();
            navController.navigate(R.id.loginMainFragment);

        }

        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);
            Toast.makeText(getContext(),"OTP sent",Toast.LENGTH_LONG);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            //   mResendToken = token;
            pd.dismiss();

            // ...
        }};

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, mobileNoEt.getText().toString());
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            pd.dismiss();
                            navController.navigate(R.id.loginProfileFragment);



                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            pd.dismiss();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });



    }





}
