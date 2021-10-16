package com.example.imdc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPageActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    Button button,chatbtn,addIDButton;
    EditText trackingIdField;
    List<TrackingItem> trackingItemList;
    TrackingAdapter trackingAdapter;
    RecyclerView trackingItemListView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        firebaseAuth = FirebaseAuth.getInstance();
        trackingItemList = new ArrayList<TrackingItem>();
        //setup Recycler View
        trackingItemListView = findViewById(R.id.trackingRecyclerView);
        trackingAdapter = new TrackingAdapter(this,trackingItemList);
        trackingItemListView.setAdapter(trackingAdapter);
        trackingItemListView.setLayoutManager(new LinearLayoutManager(this));
        //initialize list from firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        CollectionReference collectionReference =db.collection("users").document(user.getUid()).collection("trackingItem");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                        QuerySnapshot querySnapshot = task.getResult();
                        if(querySnapshot.isEmpty()){
                        }else{
                            //document exist in collection, move it into local list
                            List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                            Document tempD;
                            for(int i =0;i<documents.size();i++){
                                if(documents.get(i).exists()){
                                    //get field from ds
                                    if(trackingItemList.size()==0){
                                        TrackingItem temp = new TrackingItem();
                                        temp.setTrackingID(documents.get(i).get("TrackingID").toString());
                                        temp.setStatus(documents.get(i).get("Status").toString());
                                        temp.setTag(documents.get(i).get("TagName").toString());
                                        trackingItemList.add(temp);
                                    }
                                }else {
                                    //nothing happens
                                }
                            trackingAdapter.notifyDataSetChanged();
                            }//end of for documentSnapshot
                        }//end of if else querySnapshot
                }else{
                    Toast.makeText(MainPageActivity.this, "collection failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        CollectionReference collection = db.collection("users").document(user.getUid()).collection("list");
//        if(collection.get() == null){
//            //empty collection
//            Toast.makeText(this, "empty Collection", Toast.LENGTH_SHORT).show();
//        }
        //Initialize list with results

        Intent a = getIntent();
        if(a.getStringExtra("name")!=null){
            //there is update to add to local list
            TrackingItem tempItem = new TrackingItem();
            tempItem.setTrackingID(a.getStringExtra("tracking_id"));
            tempItem.setTag(a.getStringExtra("name"));
            tempItem.setStatus(a.getStringExtra("status"));
            trackingItemList.add(tempItem);
            trackingAdapter.notifyDataSetChanged();
            //Local change done
            //Update Firebase
            //set up map for firebase insertion
            Map<String, Object> tempM = new HashMap<>();
            tempM.put("TrackingID",a.getStringExtra("tracking_id"));
            tempM.put("TagName",a.getStringExtra("name"));
            tempM.put("Status",a.getStringExtra("status"));

            db.collection("users").document(user.getUid()).collection("trackingItem").document(tempItem.getTrackingID()).set(tempM).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Insertion into the firebase
                    Toast.makeText(MainPageActivity.this, "tag success", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainPageActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        button = findViewById(R.id.button);
        chatbtn = findViewById(R.id.button2);
        addIDButton = findViewById(R.id.addIDButton);
        trackingIdField = findViewById(R.id.trackingIdField);
        sharedPreferences = getSharedPreferences("mypreferences",MODE_PRIVATE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("role");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ChatActivity.class);
                startActivity(i);
                finish();
            }
        });
        //setup button call to move to tag page
        addIDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TagActivity.class);
                i.putExtra("tracking_ID",trackingIdField.getText().toString());
                startActivity(i);
                finish();
            }
        });

    }
}
