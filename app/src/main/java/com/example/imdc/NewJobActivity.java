package com.example.imdc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewJobActivity extends AppCompatActivity {

    EditText pickUpAddressField,recipientAddressField;
    Button addressSubmitButton;
    JSONObject jsonObject;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);

        queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        pickUpAddressField = findViewById(R.id.pickUpAddressField);
        recipientAddressField = findViewById(R.id.recipientAddressField);
        addressSubmitButton = findViewById(R.id.addressSubmitButton);

        addressSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pickUpAddress = pickUpAddressField.getText().toString();
                String recipientAddress = recipientAddressField.getText().toString();
                String url = "https://parcel-route-planning.uc.r.appspot.com/addJob?rowID=682";
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("Customer Name","FG FAMILY GOLD");
                    jsonObject.put("Street","NO.113, JLN KESUMA 4B \\\\/ 1\\\\nBDR TASIK KESUMA 40 SELANGOR\\\\nBERANANG");
                    jsonObject.put("zip","43700");
                    jsonObject.put("City","BERANANG");
                    jsonObject.put("Act Dt",20200201);
                    jsonObject.put("Act Base","P");
                    jsonObject.put("Open","9:00");
                    jsonObject.put("Closed","12:00");
                    jsonObject.put("Prod Grp","WPX");
                    jsonObject.put("Prod Code","P");
                    jsonObject.put("Total Pcs",2.0);
                    jsonObject.put("Weight",1.78);

                } catch (JSONException e) {
                    Toast.makeText(NewJobActivity.this, "here", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(NewJobActivity.this, "Job Added", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(),MainPageActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewJobActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
//                queue.add(jsonObjectRequest);
                Intent i = new Intent(getApplicationContext(),MainPageActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
