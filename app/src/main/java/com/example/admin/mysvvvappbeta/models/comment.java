package com.example.admin.mysvvvappbeta.models;

import java.util.Map;

/**
 * Created by admin on 26-12-2017.
 */


public class comment {

    public String sender;

    public  String senderImageURL;

    public  Object time;

    public  String commenttext;

    public  String sender_UID;

    public  String getSender_UID() {
        return sender_UID;
    }

    public void setSender_UID(String sender_UID) {
        this.sender_UID = sender_UID;
    }

    public comment() {
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String value) {
        this.sender = value;
    }

    public String getSenderImageURL() {
        return this.senderImageURL;
    }

    public void setSenderImageURL(String value) {
        this.senderImageURL = value;
    }

    public Object getTime() {
        return this.time;
    }

    public void setTime(Object value) {
        this.time = value;
    }

    public String getCommenttext() {
        return this.commenttext;
    }

    public void setCommenttext(String value) {
        this.commenttext = value;
    }
}
