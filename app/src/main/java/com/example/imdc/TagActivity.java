package com.example.imdc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TagActivity extends AppCompatActivity {

    TextView trackingNumberDisplay;
    EditText tagNameField;
    Button saveButton;
    String trackingID;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        Intent i = getIntent();

        trackingID = i.getStringExtra("tracking_ID");
        trackingNumberDisplay = findViewById(R.id.trackingNumberDisplay);
        trackingNumberDisplay.setText(trackingID);
        tagNameField = findViewById(R.id.tagNameField);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tagNameField.getText().toString();
                Intent i =new Intent(getApplicationContext(), MainPageActivity.class);
                i.putExtra("name",name);
                Toast.makeText(TagActivity.this, trackingID, Toast.LENGTH_SHORT).show();
                i.putExtra("tracking_id",trackingID);
                i.putExtra("status","Delivering");
                startActivity(i);
                finish();
            }
        });
    }
}
