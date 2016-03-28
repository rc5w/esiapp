package com.emergencyskills.doe.aed.models;

import java.io.Serializable;

/**
 * Created by BWT on 9/30/15.
 */
public class PickSchoolModel implements Serializable {

    public String getSchoolID() {
        return SchoolID;
    }

    public void setSchoolID(String schoolID) {
        SchoolID = schoolID;
    }

    public String getSchoolName() {
        return SchoolName;
    }

    public void setSchoolName(String schoolName) {
        SchoolName = schoolName;
    }



    public String getSchoolZipCode() {
        return SchoolZipCode;
    }

    public void setSchoolZipCode(String schoolZipCode) {
        SchoolZipCode = schoolZipCode;
    }

    public String getSchoolCode() {
        return SchoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        SchoolCode = schoolCode;
    }

    public String getSchoolCampusID() {
        return SchoolCampusID;
    }

    public void setSchoolCampusID(String schoolCampusID) {
        SchoolCampusID = schoolCampusID;
    }

    public String getSchoolCampusName() {
        return SchoolCampusName;
    }

    public void setSchoolCampusName(String schoolCampusName) {
        SchoolCampusName = schoolCampusName;
    }

    public String getSchoolAddress() {
        return SchoolAddress;
    }

    public void setSchoolAddress(String address) {
        SchoolAddress = address;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String p_key) {
        key = p_key;
    }
    public String getSchoolCity() {
        return SchoolCity;
    }

    public void setSchoolCity(String city) {
        SchoolCity = city;
    }

    private String SchoolID;
    private String key;
    private String SchoolName;
    private String SchoolCode;
    private String SchoolZipCode;
    private String SchoolCity;
    private String SchoolAddress;
    private String SchoolCampusID;
    private String SchoolCampusName;
}
