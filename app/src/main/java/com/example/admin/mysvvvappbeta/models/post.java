package com.example.admin.mysvvvappbeta.models;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Asad Mirza on 24-12-2017.
 */

public class post implements Serializable{
    public Object time;
    public String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String sender_uid;
    public String image_uri;
    public String link1;

    public String dis;
    public String title;
    public String sender_name;
    public String sender_dp;




    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String p_id;


    public post(){}

    public String getSender_dp() {
        return sender_dp;
    }

    public void setSender_dp(String sender_dp) {

        this.sender_dp = sender_dp;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    post(String title, String dis, String image_uri, String sender_uid, String sender_name, String sender_dp, String link1, String link2, String link3, Object time){
    this.title=title;
    this.dis=dis;
    this.image_uri =image_uri;
    this.sender_uid= sender_uid;

    this.link1 =link1;

        this.sender_name=sender_name;
        this.sender_dp=sender_dp;
        this.time=time;



    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getSender_uid() {

        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }




    public String getLink1() {

        return link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public String getImage_uri() {

        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public post(String image_uri) {

        this.image_uri = image_uri;
    }

    public String getDis() {

        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }
}
