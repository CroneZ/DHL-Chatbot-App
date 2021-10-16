package com.example.imdc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DriverActivity extends AppCompatActivity {

    RequestQueue queue;
    List<DeliveryJob> deliveryJobList;
    RecyclerView deliveryView;
    JSONArray latArray,lgtdArray,addressArray;
    DeliveryJob temp;
    DeliveryAdapter deliveryAdapter;
    Button lgoutButton;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        deliveryJobList = new ArrayList<DeliveryJob>();
        deliveryView = findViewById(R.id.deliveryView);
        deliveryAdapter = new DeliveryAdapter(this,deliveryJobList);
        deliveryView.setAdapter(deliveryAdapter);
        deliveryView.setLayoutManager(new LinearLayoutManager(this));
        final String url = "https://parcel-route-planning.uc.r.appspot.com/getGeo?rowID=3";

        firebaseAuth = FirebaseAuth.getInstance();
        lgoutButton = findViewById(R.id.driverLogOutButton);
        lgoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                sharedPreferences = getSharedPreferences("mypreferences",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("role");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        JsonObjectRequest driverRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(DriverActivity.this, "json success", Toast.LENGTH_SHORT).show();
                    JSONObject location = response.getJSONObject("location");
                    latArray = location.getJSONArray("lat");
                    lgtdArray = location.getJSONArray("lgtd");
                    addressArray = response.getJSONArray("address");
                    for(int i =1;i<latArray.length()-1;i++){
                            temp = new DeliveryJob(latArray.getDouble(i),lgtdArray.getDouble(i),addressArray.getString(i));
                            deliveryJobList.add(temp);
                    }
                    deliveryAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DriverActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(driverRequest);

    }
}
