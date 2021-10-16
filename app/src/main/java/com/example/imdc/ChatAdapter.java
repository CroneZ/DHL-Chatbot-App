package com.example.imdc;

import android.content.Context;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.alicebot.ab.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter{
    private static final char MESSAGE_SENT = '1';
    private static final char MESSAGE_RECEIVED = '2';

    private Context context;
    private List<ChatMessage> chatMessageList;

    public ChatAdapter(Context context, List<ChatMessage> chatMessageList){
        this.context = context;
        this.chatMessageList = chatMessageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatsend,parent,false);
            return new SendMessageHolder(view);
        }else if(viewType==MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatreceived,parent,false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = (ChatMessage) chatMessageList.get(position);

        switch(holder.getItemViewType()) {
            case MESSAGE_SENT:
                ((SendMessageHolder)holder).bind(message);
                break;
            case MESSAGE_RECEIVED:
                ((ReceivedMessageHolder)holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    @Override
    public int getItemViewType(int position){

        ChatMessage message = (ChatMessage) chatMessageList.get(position);
        if(message.getMessageType()=="S"){
            return MESSAGE_SENT;
        }else{
            return MESSAGE_RECEIVED;
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder{

        TextView messageText,timeText;


        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }
        void bind(ChatMessage message){
            messageText.setText(message.getMessage());
            timeText.setText(message.getTimestamp());
        }
    }
    private class SendMessageHolder extends RecyclerView.ViewHolder{

        TextView messageText,timeText;

        public SendMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }
        void bind(ChatMessage message){
            messageText.setText(message.getMessage());
            timeText.setText(message.getTimestamp());
        }
    }
}
