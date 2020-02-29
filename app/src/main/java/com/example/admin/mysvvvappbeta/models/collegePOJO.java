package com.example.admin.mysvvvappbeta.models;

import java.io.Serializable;

/**
 * Created by Asad Mirza on 24-01-2018.
 */

public class collegePOJO implements Serializable {
    public String college_name;
    public String college_logo;
    public String cid;
    public String topicName;
    public String website;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public collegePOJO() {
    }

    @Override
    public String toString() {
        return college_name;
    }

    public String getCollege_name() {
        return college_name;
    }

    public void setCollege_name(String college_name) {
        this.college_name = college_name;
    }

    public String getCollege_logo() {

        return college_logo;
    }

    public void setCollege_logo(String college_logo) {
        this.college_logo = college_logo;
    }
}
