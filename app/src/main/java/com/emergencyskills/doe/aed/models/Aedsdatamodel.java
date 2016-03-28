package com.emergencyskills.doe.aed.models;

/**
 * Created by Karan on 1/27/2016.
 */
public class Aedsdatamodel {

    private String serialnum="";
    private boolean statusindicator=false;
    private boolean unitunavailable=false;
    private boolean error=false;
    private boolean hasnewserialnumber=false;
    private boolean Statusindicator=false;

    private boolean datacardstatus=false;

    private String physicallocation="";
    private String padaexpdate="";
    private String padanewdate="";
    private String padalotnumber="";
    private String padbexpdate="";
    private String padbnewdate="";
    private String padblotnumber="";

    private String padpedexpdate="";
    private String padpednewdate="";
    private String Sparedate="";
    private  String newsparedate="";
    private  String errorinfo="";

    public String getErrorinfo() {
        return errorinfo;
    }
    private boolean sendsparebattery=false;

    public boolean isSendsparebattery() {
        return sendsparebattery;
    }

    public void setSendsparebattery(boolean sendsparebattery) {
        this.sendsparebattery = sendsparebattery;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }

    public String getNewsparedate() {
        return newsparedate;
    }

    public void setNewsparedate(String newsparedate) {
        this.newsparedate = newsparedate;
    }

    private boolean isoutwithcoach=false;

    public boolean isOutWithCoach() {
        return isoutwithcoach;
    }

    public void setIsOutWithCoach(boolean isoutwithcoach) {
        this.isoutwithcoach = isoutwithcoach;
    }

    private String no="";

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    private String aedid="";

    public String getAedid() {
        return aedid;
    }

    public void setAedid(String aedid) {
        this.aedid = aedid;
    }

    public String getSparedate() {
        return Sparedate;
    }

    public void setSparedate(String sparedate) {
        Sparedate = sparedate;
    }

    public String getSerialnum() {
        return serialnum;
    }

    public void setSerialnum(String serialnum) {
        this.serialnum = serialnum;
    }

    public boolean isStatusindicator() {
        return statusindicator;
    }

    public void setStatusindicator(boolean statusindicator) {
        this.statusindicator = statusindicator;
    }

    public boolean isDatacardstatus() {
        return datacardstatus;
    }

    public void setDatacardstatus(boolean datacardstatus) {
        this.datacardstatus = datacardstatus;
    }

    public String getPhysicallocation() {
        return physicallocation;
    }

    public void setPhysicallocation(String physicallocation) {
        this.physicallocation = physicallocation;
    }

    public String getPadaexpdate() {
        return padaexpdate;
    }

    public void setPadaexpdate(String padaexpdate) {
        this.padaexpdate = padaexpdate;
    }

    public String getPadanewdate() {
        return padanewdate;
    }

    public void setPadanewdate(String padanewdate) {
        this.padanewdate = padanewdate;
    }

    public String getPadalotnumber() {
        return padalotnumber;
    }

    public void setPadalotnumber(String padalotnumber) {
        this.padalotnumber = padalotnumber;
    }

    public String getPadbexpdate() {
        return padbexpdate;
    }

    public void setPadbexpdate(String padbexpdate) {
        this.padbexpdate = padbexpdate;
    }

    public String getPadbnewdate() {
        return padbnewdate;
    }

    public void setPadbnewdate(String padbnewdate) {
        this.padbnewdate = padbnewdate;
    }

    public String getPadblotnumber() {
        return padblotnumber;
    }

    public void setPadblotnumber(String padblotnumber) {
        this.padblotnumber = padblotnumber;
    }

    public String getPadpedexpdate() {
        return padpedexpdate;
    }

    public void setPadpedexpdate(String padpedexpdate) {
        this.padpedexpdate = padpedexpdate;
    }

    public String getPadpednewdate() {
        return padpednewdate;
    }

    public void setPadpednewdate(String padpednewdate) {
        this.padpednewdate = padpednewdate;
    }

    public String getPadpedlotnumber() {
        return padpedlotnumber;
    }

    public void setPadpedlotnumber(String padpedlotnumber) {
        this.padpedlotnumber = padpedlotnumber;
    }

    public boolean isHasfrx() {
        return hasfrx;
    }

    public void setHasfrx(boolean hasfrx) {
        this.hasfrx = hasfrx;
    }

    public boolean isRequestpediatrickey() {
        return requestpediatrickey;
    }

    public void setRequestpediatrickey(boolean requestpediatrickey) {
        this.requestpediatrickey = requestpediatrickey;
    }

    public boolean isRequestdoesendfastresponsekit() {
        return requestdoesendfastresponsekit;
    }

    public void setRequestdoesendfastresponsekit(boolean requestdoesendfastresponsekit) {
        this.requestdoesendfastresponsekit = requestdoesendfastresponsekit;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isUnitunavailable() {
        return unitunavailable;
    }

    public void setUnitunavailable(boolean p_unitunavailable) {
        this.unitunavailable = p_unitunavailable;
    }

    public boolean isErrorInUnit() {
        return error;
    }

    public void setErrorinunit(boolean error) {
        this.error = error;
    }

    public boolean isHasnewserialnumber() {
        return hasnewserialnumber;
    }

    public void setHasnewserialnumber(boolean hasnewserialnumber) {
        this.hasnewserialnumber = hasnewserialnumber;
    }

    private String padpedlotnumber="";

    private boolean hasfrx=false;

    private boolean requestpediatrickey=false;

    private boolean requestdoesendfastresponsekit=false;

    private String comments="";




}
