package com.example.imdc;

public class ChatMessage {

    private String message;
    private String timestamp;
    private String messageType;

    public ChatMessage(String message, String timestamp,String messageType){
        this.message = message;
        this.timestamp = timestamp;
        this.messageType = messageType;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public String getMessage() {
        return this.message;
    }
    public String getMessageType() {
        return this.messageType;
    }
    public String getTimestamp() {
        return this.timestamp;
    }
}
