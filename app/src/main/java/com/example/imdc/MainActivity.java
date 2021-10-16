package com.example.imdc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.MoreObjects;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //This is the loginPage

    TextView registerLink;
    EditText email,password;
    String currentEmail,role;
    Button loginButton;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;

    @Override
    public void onStart() {
        sharedPreferences = getSharedPreferences("mypreferences",MODE_PRIVATE);
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            String userID = currentUser.getUid();
            //get role from firebase collection based on user email
            getRole(userID);
        }
    }

    //retrieve id from firestore based on uid
    private void getRole(String userID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                         role= document.get("role").toString();
                        if(role.equals("user")){
                            Intent mainPage = new Intent(getApplicationContext(),MainPageActivity.class);
                            startActivity(mainPage);
                            finish();
                        }else if(role.equals("driver")){
                            Intent driverPage = new Intent(getApplicationContext(),DriverActivity.class);
                            startActivity(driverPage);
                            finish();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "firebase fail on document", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "firebase fail on task", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        registerLink=findViewById(R.id.registerLinkTextView);
        email = findViewById(R.id.emailLoginField);
        password = findViewById(R.id.passwordLoginField);
        loginButton = findViewById(R.id.loginButton);
        firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                if(TextUtils.isEmpty(userEmail)||TextUtils.isEmpty(userPassword)){
                    Toast.makeText(MainActivity.this, "Please complete the fields!", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                String uID = currentUser.getUid();
                                getRole(uID);
                            }else{
                                Toast.makeText(MainActivity.this, "Incorrect Email/Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerPage = new Intent(getApplicationContext(),RegisterActivity.class);
                registerPage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(registerPage);
                finish();
            }
        });

}
}
