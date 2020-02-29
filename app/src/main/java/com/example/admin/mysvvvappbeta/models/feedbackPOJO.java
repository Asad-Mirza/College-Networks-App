package com.example.admin.mysvvvappbeta.models;

/**
 * Created by Asad Mirza on 01-02-2018.
 */

public class feedbackPOJO {
    public String text,sender_name,sender_UID,device_info,network_type;
    public Object time;

    public String getNetwork_type() {
        return network_type;
    }

    public void setNetwork_type(String network_type) {
        this.network_type = network_type;
    }

    public Object getTime() {

        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender_UID() {

        return sender_UID;
    }

    public void setSender_UID(String sender_UID) {
        this.sender_UID = sender_UID;
    }

    public String getSender_name() {

        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }



    public String getDevice_info() {

        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }
}
