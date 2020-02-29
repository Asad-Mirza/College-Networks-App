package com.example.admin.mysvvvappbeta.models;

import java.io.Serializable;

/**
 * Created by Asad Mirza on 27-01-2018.
 */

public class sectionPOJO implements Serializable {
    public String course,branch,year,section,key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public sectionPOJO() {
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSection() {

        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCourse() {

        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBranch() {

        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
