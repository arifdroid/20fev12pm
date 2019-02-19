package com.example.dell.a19febnoon;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Button buttonSignIn;
    private EditText editTextPhone,editTextCode;
    private TextView messagee;

    private String codeReceivedFromFB;

    //
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSignIn=findViewById(R.id.buttonSignIn);
        editTextPhone=findViewById(R.id.editTextPhone);
        editTextCode=findViewById(R.id.editTextCode);

        messagee=findViewById(R.id.messageCode);

        FirebaseApp.initializeApp(this);

        userIsLoggedIn();

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(codeReceivedFromFB!=null){


                    verifyPhoneWithCode(codeReceivedFromFB,editTextCode.getText().toString());

                }else {

                    startPhoneVerification();

                }
            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                messagee.setText("logging in..");

                signInWithPhoneCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                messagee.setText("failed to login");

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                messagee.setText("enter code");
                codeReceivedFromFB=s;

                buttonSignIn.setText("Enter Code Now");
            }
        };


    }

    private void verifyPhoneWithCode(String codeReceivedFromFB, String s) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeReceivedFromFB,s);
        signInWithPhoneCredential(credential);

    }

    private void signInWithPhoneCredential(PhoneAuthCredential credential) {


        messagee.setText("verifying..");

        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                messagee.setText("successful..");


                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(task.isSuccessful()){

                   // userIsLoggedIn();


                    final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());

                    mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){
                                Map<String,Object> userMap = new HashMap<>();

                                userMap.put("phone",user.getPhoneNumber());
                                userMap.put("name",user.getDisplayName());
                                userMap.put("test",user.getUid());

                                mUserDB.updateChildren(userMap);


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    userIsLoggedIn();
                }
            }
        });
    }

    private void userIsLoggedIn() {

        messagee.setText("logged in now.!");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        if(user!=null){

            Intent intent = new Intent(MainActivity.this,MainPageActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void startPhoneVerification(){

        messagee.setVisibility(View.VISIBLE);
        messagee.setText("start verifying..");

        PhoneAuthProvider.getInstance().verifyPhoneNumber(editTextPhone.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);

    }
}
