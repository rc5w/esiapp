package com.emergencyskills.doe.aed.models;

/**
 * Created by Karan on 12/2/2015.
 */
import java.lang.*;
import java.util.ArrayList;
import java.util.ArrayList;
import com.emergencyskills.doe.aed.utils.CommonUtilities;

public class Schoolinfomodel {

    public String getContactphone() {
        return contactphone;
    }
    private String zip;
    private String state;
    private String city;
    private String schoolname;

    private String schoolnamefromsign;

    public String getSchoolnamefromsign() {
        return schoolnamefromsign;
    }

    public void setSchoolnamefromsign(String schoolnamefromsign) {
        this.schoolnamefromsign = schoolnamefromsign;
    }

    private String esrname;
    private String esrimage;

    private String esrnameser;
    private String esrimageser;
    private String esrnameinstall;
    private String esrimageinstall;



    public String getEsrname() {
        return esrname;
    }

    public String getEsrnameser() {
        return esrnameser;
    }

    public void setEsrnameser(String esrnameser) {
        this.esrnameser = esrnameser;
    }

    public String getEsrimageser() {
        return esrimageser;
    }

    public void setEsrimageser(String esrimageser) {
        this.esrimageser = esrimageser;
    }

    public String getEsrnameinstall() {
        return esrnameinstall;
    }

    public void setEsrnameinstall(String esrnameinstall) {
        this.esrnameinstall = esrnameinstall;
    }

    public String getEsrimageinstall() {
        return esrimageinstall;
    }

    public void setEsrimageinstall(String esrimageinstall) {
        this.esrimageinstall = esrimageinstall;
    }

    public void setEsrname(String esrname) {
        this.esrname = esrname;
    }

    public String getEsrimage() {
        return esrimage;
    }

    public void setEsrimage(String esrimage) {
        this.esrimage = esrimage;
    }

    public String drillsignimage;
    public String servicesignimage;
    public String newinstallsignimage;
    public String drilldate;
    public String drilltime;
    public String servicesdate;
    public String servicetime;
    public String installdate;

    public String servicename;
    public String installname;

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getInstallname() {
        return installname;
    }

    public void setInstallname(String installname) {
        this.installname = installname;
    }

    public String getInstalltime() {
        return installtime;
    }

    public void setInstalltime(String installtime) {
        this.installtime = installtime;
    }

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

    public String getDrillname() {
        return drillname;
    }

    public void setDrillname(String drillname) {
        this.drillname = drillname;
    }

    public String installtime;
    public String drillname;


    public String getSchoolname() {
        return schoolname;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public ArrayList<ExistingAedsmodel> getExistingAedsmodelArrayList() {
        return existingAedsmodelArrayList;
    }

    public void setExistingAedsmodelArrayList(ArrayList<ExistingAedsmodel> existingAedsmodelArrayList) {
        CommonUtilities.logMe( "setting list: " +  existingAedsmodelArrayList.size() );
        this.existingAedsmodelArrayList = existingAedsmodelArrayList;
    }

    ArrayList<ExistingAedsmodel> existingAedsmodelArrayList;
    ArrayList<OtherSchoolsinCampus> otherSchoolsinCampuses;

    private String schoolid;

    public ArrayList<OtherSchoolsinCampus> getOtherSchoolsinCampuses() {
        return otherSchoolsinCampuses;
    }

    public void setOtherSchoolsinCampuses(ArrayList<OtherSchoolsinCampus> otherSchoolsinCampuses) {
        this.otherSchoolsinCampuses = otherSchoolsinCampuses;
    }

    private String name;
    private int size=0;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private String address;
    private String phone;


    public String getname(){
        return name;
    }

    public void setname(String nam) {
        name = nam;
    }

    private String principal;
    private String princemail;
    private String aedcontact;
    private String aedcontactemail;
    private String schoolcode;

    private String drillid;
    private String servicecallid;
    private String contactphone;

    public String getNewinstallid() {
        return newinstallid;
    }

    public void setNewinstallid(String newinstallid) {
        this.newinstallid = newinstallid;
    }

    public void setNewinstallAedids(ArrayList<String> newinstallaedids) {
        this.newinstallaedids = newinstallaedids;
    }

    public ArrayList<String> getNewinstallAedids() {
        return newinstallaedids;
    }

    public String getDrillid() {
        return drillid;
    }

    public void setDrillid(String drillid) {
        this.drillid = drillid;
    }

    public String getServicecallid() {
        return servicecallid;
    }

    public void setServicecallid(String servicecallid) {
        this.servicecallid = servicecallid;
    }

    private String newinstallid;
    private ArrayList<String> newinstallaedids;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPrincemail() {
        return princemail;
    }

    public void setPrincemail(String princemail) {
        this.princemail = princemail;
    }

    public String getAedcontactemail() {
        return aedcontactemail;
    }

    public void setAedcontactemail(String aedcontactemail) {
        this.aedcontactemail = aedcontactemail;
    }

    public String getAedcontact() {
        return aedcontact;
    }

    public void setAedcontact(String aedcontact) {
        this.aedcontact = aedcontact;
    }

    public String getSchoolcode() {
        return schoolcode;
    }

    public void setSchoolcode(String schoolcode) {
        this.schoolcode = schoolcode;
    }

    private  String drilldata;
    private  String servicedata;
    private  String newinstalldata;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDrilldata() {
        return drilldata;
    }

    public void setDrilldata(String drilldata) {
        this.drilldata = drilldata;
    }

    public String getServicedata() {
        return servicedata;
    }

    public void setServicedata(String servicedata) {
        this.servicedata = servicedata;
    }

    public String getNewinstalldata() {
        return newinstalldata;
    }

    public void setNewinstalldata(String newinstalldata) {
        this.newinstalldata = newinstalldata;
    }





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

    private  String drillstatus;
    private  String servicestatus;
    private  String installstatus;

    private boolean includeindrill=true;

    public boolean isIncludeindrill() {
        return includeindrill;
    }

    public void setIncludeindrill(boolean includeindrill) {
        this.includeindrill = includeindrill;
    }

    public boolean isIncludeinservice() {
        return includeinservice;
    }

    public void setIncludeinservice(boolean includeinservice) {
        this.includeinservice = includeinservice;
    }

    public boolean isIncludeinnewinstall() {
        return includeinnewinstall;
    }

    public void setIncludeinnewinstall(boolean includeinnewinstall) {
        this.includeinnewinstall = includeinnewinstall;
    }

    private boolean includeinservice=true;
    private boolean includeinnewinstall=true;



}
