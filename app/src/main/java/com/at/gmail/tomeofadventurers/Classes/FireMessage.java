package com.at.gmail.tomeofadventurers.Classes;

public class FireMessage {

    public String message;
    public String senderUID;
    public String senderName;
    public String receiverUID;


    public FireMessage(String message, String senderUID, String senderName, String receiverUID){

        this.message = message;
        this.senderUID = senderUID;
        this.senderName = senderName;
        this.receiverUID = receiverUID;

    }

    public  FireMessage(){
        message = "";
        senderUID = "";
        senderName = "";
        receiverUID = "";
    }

    public String getMessage() {
        return message;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverUID(){
        return receiverUID;
    }


}
