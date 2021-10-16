package com.example.imdc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.TabStopSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextView loginLink;
    FirebaseAuth firebaseAuth;
    EditText email, password,passwordConfirm;
    Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginLink = findViewById(R.id.loginLinkTextView);
        email =  findViewById(R.id.emailRegisterField);
        password = findViewById(R.id.passworldRegisterField);
        passwordConfirm = findViewById(R.id.passworldConfirmRegisterField);
        register = findViewById(R.id.registerButton);
        firebaseAuth = FirebaseAuth.getInstance();

        //register new user process start
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userEmail = email.getText().toString().trim();
                final String userPassword = password.getText().toString().trim();
                String userPasswordConfirm = passwordConfirm.getText().toString().trim();
                //validation for fields
                if(TextUtils.isEmpty(userEmail)||TextUtils.isEmpty(userPassword)||TextUtils.isEmpty(userPasswordConfirm)){
                    Toast.makeText(RegisterActivity.this, "Please Complete the fields!", Toast.LENGTH_SHORT).show();
                }else if(!userPassword.equals(userPasswordConfirm)){
                    Toast.makeText(RegisterActivity.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                }else{
                    //validation is successful
                    firebaseAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //user creation is successful
                                // Sign in success, get current userID from the signed in user;
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String uID = user.getUid();
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                //Generate map to store value for the current user into firestore
                                Map<String, Object> temp = new HashMap<>();
                                temp.put("role","user");

                                //Inserting map into the firestore
                                db.collection("users").document(uID).set(temp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //On success
                                        Toast.makeText(RegisterActivity.this, "DocumentSnapshot created", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //On failure
                                        Toast.makeText(RegisterActivity.this, "Failed creation", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();

                                //Sign in the user and move them into user.
                                firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            //Signed in successfully
                                            Intent login = new Intent(getApplicationContext(),MainPageActivity.class);
                                            startActivity(login);
                                            finish();
                                        }else{
                                            //Failed to sign in
                                            Toast.makeText(RegisterActivity.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegisterActivity.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginPage = new Intent(getApplicationContext(),MainActivity.class);
                loginPage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(loginPage);
                finish();
            }
        });

    }
}
