package com.sp.mm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;


import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private Button btnLog;
    EditText Name,Email,Pass,Cpass;
    Button btnRegister;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;

    String userID;

    String profileID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        Name = findViewById(R.id.NameReg);

        Email = findViewById(R.id.EmailReg);
        Pass = findViewById(R.id.PasswordReg);
        Cpass = findViewById(R.id.ConPasswordReg);

        btnRegister = findViewById(R.id.btnRegister);

        btnLog = findViewById(R.id.btnLog);
        btnLog.setOnClickListener(onLog);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        //Keep user logged in
        if(fAuth.getCurrentUser() != null ){
            startActivity(new Intent (Registration.this,Main.class));
            finish();
        }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = Email.getText().toString().trim();
                String password = Pass.getText().toString().trim();
                String name = Name.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Email.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Pass.setError("Enter your password");
                    return;
                }
                if(password.length()< 6){
                    Pass.setError("Minimum character length for password is 6");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //Register the user
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Registration.this, "Registration Successful!", Toast.LENGTH_LONG).show();

                            userID = fAuth.getCurrentUser().getUid();

                            //DocumentReference documentReference = fstore.collection("users").document("userID").collection("profile").document(profileID);
                            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                            DocumentReference documentReference = rootRef
                                    .collection("users").document(userID)
                                    .collection("profile").document("profileID");

                            Map<String,Object> user = new HashMap<>();
                            user.put("Name",name);      //Add into firestore
                            user.put("Email",email);    //Add into firestore
                            user.put("Password",password);    //Add into firestore
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","onSuccess: user Profile is created for"+ userID);
                                }
                            });

                            Intent add;
                            add = new Intent(Registration.this, Main.class);
                            startActivity(add);
                            finish();


                        } else {
                            Toast.makeText(Registration.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

                /*if (Pass == Cpass) {

                } else {
                    Toast.makeText(Registration.this, "Please check that your password is keyed in correctly", Toast.LENGTH_LONG).show();

                }*/



            }

        });
    }

    private View.OnClickListener onLog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent add;
            add = new Intent(Registration.this, Login.class);
            startActivity(add);
        }
    };


}

//editText.getText().clear();
//android:background="@drawable/round"