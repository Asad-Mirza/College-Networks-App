package com.example.admin.mysvvvappbeta.models;

import java.io.Serializable;

/**
 * Created by Asad Mirza on 18-12-2017.
 */

public class user implements Serializable{
    public String name;
    public String branch;
    public  String token;
    public  String college;
    public  String course;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String cid;
    public String year,section;
    public String photoId;
    public String email;
    public String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBranch() {
        return branch;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPhotoId() {

        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBranch(String branch) {

        this.branch = branch;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }


    public user() {

    }

    public user(String name, String branch, String year, String photoId, String token,String section,String college) {
        this.branch = branch;
        this.name = name;
        this.year = year;
        this.token = token;
        this.photoId = photoId;

        this.section =section;

       this.college  =college;
    }


}
