package com.example.bartalap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //Create global veriable for phone number and code
    private EditText mPhoneNumber, mCode;

    //Create global variable for Send button
    private Button mSend;

    private FirebaseAuth mAuth;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        userIsLoggedIn();

        //Select the id of the phone number to the mPhoneNumber variable
        mPhoneNumber = findViewById(R.id.phone_number);

        //Select the id of the code to the mcoder variable
        mCode = findViewById(R.id.code);

        //Select the id of the Send button to the mSend variable
        mSend = findViewById(R.id.send);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mVerificationId != null)
                    verifyPhoneNumberWithCode();
                else
                    startPhoneNumberVerification();
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {}

            //This method is called whenever the code is send to the user
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                //super.onCodeSent(verificationId, forceResendingToken);

                mVerificationId = verificationId;
                mSend.setText("Verify Code");
            }
        };


    }

    //@param code is the 6 digit verification code sent to user phone via msg
    private void verifyPhoneNumberWithCode() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, mCode.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    userIsLoggedIn();
            }
        });
    }

    private void userIsLoggedIn() {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
            finish();
            return;
        }

    }

    private void startPhoneNumberVerification() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mPhoneNumber.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
           }
}