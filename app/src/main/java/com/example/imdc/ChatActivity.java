package com.example.imdc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.alicebot.ab.Chat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    RecyclerView chatlist;
    ChatAdapter chatAdapter;
    Button sendButton;
    TextView messageField;
    Date date;
    DateFormat dateFormat;
    List<ChatMessage> chatMessageList;
    RequestQueue queue;
    JSONObject jsonObject;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        ChatMessage msg = new ChatMessage("Hello There","10:10","S");
//        ChatMessage msg2 = new ChatMessage("Hello to you too!","10:12","R");

        //get data from database of messages between user and chat bot
        chatMessageList = new ArrayList<ChatMessage>();
//        chatMessageList.add(msg);
//        chatMessageList.add(msg2);
        date = new Date();
        String stringDateFormat = "hh:mm a";
        dateFormat = new SimpleDateFormat(stringDateFormat);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        final String url = "https://rasa-chatbot-328310.uc.r.appspot.com/webhooks/rest/webhook";

        chatlist = findViewById(R.id.chatListView);
        chatAdapter = new ChatAdapter(this,chatMessageList);
        chatlist.setAdapter(chatAdapter);
        chatlist.setLayoutManager(new LinearLayoutManager(this));

        messageField = findViewById(R.id.messageField);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new Date();
                ChatMessage temp = new ChatMessage(messageField.getText().toString(), dateFormat.format(date), "S");
                chatMessageList.add(temp);
                chatAdapter.notifyDataSetChanged();
                jsonObject = new JSONObject();
                String email = user.getEmail();
                try {
                    jsonObject.put("sender",email);
                    jsonObject.put("message",messageField.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CustomRequest jsonObjectRequest = new CustomRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject temp = response.getJSONObject(0);
                            date = new Date();
                            ChatMessage tempMessage = new ChatMessage(temp.getString("text"),dateFormat.format(date),"R");
                            Toast.makeText(ChatActivity.this, "success", Toast.LENGTH_SHORT).show();
                            chatMessageList.add(tempMessage);
                            chatAdapter.notifyDataSetChanged();
                            messageField.setText("");
                            if(temp.getString("text").equals("Redirecting you to forms . . .")){
                               //Intent to direct to add new Job page
                                Intent i = new Intent(getApplicationContext(),NewJobActivity.class);
                                startActivity(i);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(jsonObjectRequest);


            }
        });
    }
}
