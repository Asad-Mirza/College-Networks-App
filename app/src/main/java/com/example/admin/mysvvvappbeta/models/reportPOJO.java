package com.example.admin.mysvvvappbeta.models;

/**
 * Created by Asad Mirza on 28-01-2018.
 */

public class reportPOJO {
    public String address,id,senderName,senderUID;
    public Object time;

    public reportPOJO() {
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public String getSenderUID() {

        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public String getSenderName() {

        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
