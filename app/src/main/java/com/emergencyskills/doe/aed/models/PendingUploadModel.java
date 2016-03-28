package com.emergencyskills.doe.aed.models;

import java.io.Serializable;

/**
 * Created by BWT on 9/30/15.
 */
public class PendingUploadModel implements Serializable {

    public String drillsignimage;
    public String servicesignimage;
    public String newinstallsignimage;
    public String drilldate;
    public String drilltime;
    public String servicesdate;
    public String servicetime;
    public String installdate;
    public String installtime;
    public String drillname;
    public String schoolname;

    public String getDrillsignimage() {
        return drillsignimage;
    }

    public void setDrillsignimage(String drillsignimage) {
        this.drillsignimage = drillsignimage;
    }

    public String getServicesignimage() {
        return servicesignimage;
    }

    public void setServicesignimage(String servicesignimage) {
        this.servicesignimage = servicesignimage;
    }

    public String getNewinstallsignimage() {
        return newinstallsignimage;
    }

    public void setNewinstallsignimage(String newinstallsignimage) {
        this.newinstallsignimage = newinstallsignimage;
    }

    public String getDrilldate() {
        return drilldate;
    }

    public void setDrilldate(String drilldate) {
        this.drilldate = drilldate;
    }

    public String getDrilltime() {
        return drilltime;
    }

    public void setDrilltime(String drilltime) {
        this.drilltime = drilltime;
    }

    public String getServicesdate() {
        return servicesdate;
    }

    public void setServicesdate(String servicesdate) {
        this.servicesdate = servicesdate;
    }

    public String getServicetime() {
        return servicetime;
    }

    public void setServicetime(String servicetime) {
        this.servicetime = servicetime;
    }

    public String getInstalldate() {
        return installdate;
    }

    public void setInstalldate(String installdate) {
        this.installdate = installdate;
    }

    public String getInstalltime() {
        return installtime;
    }

    public void setInstalltime(String installtime) {
        this.installtime = installtime;
    }

    public String getDrillname() {
        return drillname;
    }

    public void setDrillname(String drillname) {
        this.drillname = drillname;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

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

    private String SchoolID;
    private String SchoolName;

    public String getServicedata() {
        return servicedata;
    }

    public void setServicedata(String servicedata) {
        this.servicedata = servicedata;
    }

    public String getDrilldata() {
        return drilldata;
    }

    public void setDrilldata(String drilldata) {
        this.drilldata = drilldata;
    }

    public String getNewinstalldata() {
        return newinstalldata;
    }

    public void setNewinstalldata(String newinstalldata) {
        this.newinstalldata = newinstalldata;
    }

    private  String drilldata;
    private  String servicedata;
    private  String newinstalldata;



    public String getDrillstatus() {
        return drillstatus;
    }

    public void setDrillstatus(String drillstatus) {
        this.drillstatus = drillstatus;
    }

    public String getServicestatus() {
        return servicestatus;
    }

    public void setServicestatus(String servicestatus) {
        this.servicestatus = servicestatus;
    }

    public String getInstallstatus() {
        return installstatus;
    }

    public void setInstallstatus(String installstatus) {
        this.installstatus = installstatus;
    }

    private  String drillstatus="i";
    private  String servicestatus="k";
    private  String installstatus="j";


    private String address;
    private String principal;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDrillid() {
        return drillid;
    }

    public void setDrillid(String drillid) {
        this.drillid = drillid;
    }

    private String contact;
    private String code;
    private String drillid;



}
