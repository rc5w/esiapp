package com.emergencyskills.doe.aed.utils;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.emergencyskills.doe.aed.models.PendingUploadModel;
import com.emergencyskills.doe.aed.models.Schoolinfomodel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Karan on 2/12/2016.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    Context context;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        this.context = context;
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.e("ok", "imhere");

        listops listops = new listops(context);

        ArrayList<Schoolinfomodel> drillist = listops.getdrilllist();
        ArrayList<Schoolinfomodel> serviceist = listops.getservicelist();
        ArrayList<Schoolinfomodel> installist = listops.getinstalllist();
        ArrayList<PendingUploadModel> pendinglist = listops.getpendinglist();
        ArrayList<PendingUploadModel> templist = pendinglist;
        if (pendinglist.size() > 0) {

            while (pendinglist.size() > 0) {

                for (int i = 0; i < templist.size(); i++) {


                    PendingUploadModel pendingUploadModel = templist.get(i);
                    String code = pendingUploadModel.getCode();
                    if (pendingUploadModel.getDrillstatus().equals("Pending")) {
                        int check = checklistcontainscodeandset(code, drillist);
                        if (check != -1) {

                            String drilltime = drillist.get(check).getDrilltime();
                            String drilldate = drillist.get(check).getDrilldate();
                            String drilldata = drillist.get(check).getDrilldata();
                            String sname = "";
//                            String drillesrname = drillist.get(check).getEsrname();
//                            String drillesrimage = drillist.get(check).getEsrimage();
//                           String drillsign = drillist.get(check).getDrillsignimage();
//                            String drillname = drillist.get(check).getDrillname();

                            // try {
                            //     sname = drillist.get(check).getSchoolnamefromsign();
                            //     Log.e("f", sname);

                            // } catch (Exception e) {
                            //         // this is always going to happen
                            //     sname = "";
                            // }

                            try {
                                
                                if(senddata(null, "uploadDrill", drilldata, drilldate, drilltime, null, "Sending" + " " + sname + " " + "Drill data", null, null, sname))
                                {
                                    drillist.get(check).setDrillstatus("Completed");
                                    listops.putdrilllist(drillist);
                                }
                            } catch (Exception e) {
                                Log.e("FS", e.getMessage());
                            }


                        }


                    }

                    if (pendingUploadModel.getServicestatus().equals("Pending")) {
                        int check = checklistcontainscodeandset(code, serviceist);
                        if (check != -1) {



                            String servicesign = serviceist.get(check).getServicesignimage();
                            String servicename = serviceist.get(check).getServicename();

                            String servicedate = serviceist.get(check).getServicesdate();

                            String servicetime = serviceist.get(check).getServicetime();
                            String servicedata = serviceist.get(check).getServicedata();

                            String serviceesrimage = serviceist.get(check).getEsrimageser();
                            String servicecesrname = serviceist.get(check).getEsrnameser();

                            String sname;

                            try {
                                sname = serviceist.get(check).getSchoolnamefromsign();
                                Log.e("f", sname);

                            } catch (Exception e) {
                                sname = "";
                            }

                            try {
                                if(!servicesign.isEmpty()) {

                                    if(senddata(servicesign, "uploadServiceCall", servicedata, servicedate, servicetime, servicename, "Sending" + " " + sname + " " + "Service Call data", servicecesrname, serviceesrimage, sname))
                                    {
                                        serviceist.get(check).setServicestatus("Completed");
                                        listops.putservicelist(serviceist);
                                    }


                                } } catch (Exception e) {
                                Log.e("FS", e.getMessage());
                            }

                        }


                    }
                    if (pendingUploadModel.getInstallstatus().equals("Pending")) {
                        int check = checklistcontainscodeandset(code, installist);
                        if (check != -1) {


                            String installsign = installist.get(check).getNewinstallsignimage();
                            String installdate = installist.get(check).getInstalldate();
                            String installtime = installist.get(check).getInstalltime();
                            String  installname = installist.get(check).getInstallname();
                            String installdata = installist.get(check).getNewinstalldata();
                            String installesrimage = installist.get(check).getEsrimageinstall();
                            String installesrname = installist.get(check).getEsrnameinstall();

                            String sname;

                            try {
                                sname = installist.get(check).getSchoolnamefromsign();
                                Log.e("f", sname);

                            } catch (Exception e) {
                                sname = "";
                            }
                            try {
                                if(!installsign.isEmpty())


                                if(senddata(installsign, "uploadNewInstall", installdata, installdate, installtime, installname, "Sending" + " " + sname + " " + "New Install data", installesrname, installesrimage, sname))
                                {
                                    installist.get(check).setInstallstatus("Completed");
                                    listops.putinstallllist(installist);
                                }
                            } catch (Exception e) {
                                Log.e("FS", e.getMessage());
                            }
                        }


                    }
                    pendinglist.remove(i);


                }
                listops.putpendinglist(pendinglist);


            }

        }
    }

    private int checklistcontainscodeandset(String code,ArrayList<Schoolinfomodel> data) {

        try {
            int i = -1;

            for (int pass = 0; pass < data.size(); pass++) {
                if (data.get(pass).getSchoolcode().equals(code)) {
                    i = pass;
                }
            }
            return i;
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    public boolean senddata(String url0,String url1,String url2,String url3,String url4,String url5,String url6,String url7,String mediaFileEsr,String url9)
    {
        CommonUtilities.logMe( "about to start syncadapter senddata" + url1 );
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize,bytesRead2, bytesAvailable2, bufferSize2;
        byte[] buffer,buffer2;
        int maxBufferSize = 1 * 1024 * 1024;
        File f = null,f2 = null;

        String line = null;
        listops listops = new listops(context);
        FileInputStream fileInputStream = null;
        FileInputStream fileInputStream2 = null;

        String floatMessage = "nodata";
        try {

            
            URL url = new URL("http://doe.emergencyskills.com/api/api.php");
                //URL url = new URL("http://workintelligent.com/TagFrame/webservice/testing");
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            if( url0 != null )
            {
                conn.setRequestProperty("media_file", url0);
            }
            if( mediaFileEsr != null )
            {
                conn.setRequestProperty("media_file_esr", mediaFileEsr);
            }

            
                //  conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            
            if( url0 != null )
            {
                    // Log.e("which",urls[1]);
                f = new File(url0);
                
                fileInputStream = new FileInputStream(f);
                f2 = new File(mediaFileEsr);

                fileInputStream2 = new FileInputStream(f2);

            }
//Adding Parameter name

                dos.writeBytes("Content-Disposition: form-data; name=\"apikey\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes("daMEs26rufAqasw2pUYU"); // mobile_no is String variable
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);



                //adding parameter description
                dos.writeBytes("Content-Disposition: form-data; name=\"method\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(url1); // mobile_no is String variable
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);


                    //adding parameter  event id
                dos.writeBytes("Content-Disposition: form-data; name=\"data\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(url2); // mobile_no is String variable
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);


                if( url9 != null )
                {
                        //adding parameter  event id
                    dos.writeBytes("Content-Disposition: form-data; name=\"schoolname\"" + lineEnd);
                //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(url9); // mobile_no is String variable
                    dos.writeBytes(lineEnd);
                    
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                }


                //adding parameter  event id
                dos.writeBytes("Content-Disposition: form-data; name=\"uploader\"" + lineEnd);
                //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(listops.getString("username")); // mobile_no is String variable
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);


                //adding parameter  event id
                dos.writeBytes("Content-Disposition: form-data; name=\"date\"" + lineEnd);
                //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(url3); // mobile_no is String variable
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);

                //adding parameter  event id
                dos.writeBytes("Content-Disposition: form-data; name=\"time\"" + lineEnd);
                //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(url4); // mobile_no is String variable
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);

                if( url5 != null )
                {
                        //adding parameter  event id
                    dos.writeBytes("Content-Disposition: form-data; name=\"name\"" + lineEnd);
                        //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                        //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);
                    
                    dos.writeBytes(url5); // mobile_no is String variable
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                        //adding parameter  event id
                }


                if( url7 != null )
                {
                        //adding parameter  event id
                    dos.writeBytes("Content-Disposition: form-data; name=\"esr_name\"" + lineEnd);
                        //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                        //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);
                    
                    dos.writeBytes(url7); // mobile_no is String variable
                    dos.writeBytes(lineEnd);
                    
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                        //adding parameter  event id
                }

                if( mediaFileEsr != null && fileInputStream2 != null)
                {
                    
                    dos.writeBytes("Content-Disposition: form-data; name=\"media_file_esr\";filename=\"" + mediaFileEsr + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    
                        // create a buffer of maximum size
                    bytesAvailable = fileInputStream2.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                        // read file and write it into form...
                    bytesRead = fileInputStream2.read(buffer, 0, bufferSize);
                    
                    while (bytesRead > 0) {
                        
                        
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream2.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream2.read(buffer, 0, bufferSize);
                        
                        
                    }
                    
                        // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + lineEnd) ;
                    fileInputStream2.close();;
                    
                    dos.writeBytes("Content-Disposition: form-data; name=\"media_file\";filename=\"" + url0 + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    
                }
                if( fileInputStream != null )
                {

                        // create a buffer of maximum size
                    bytesAvailable2 = fileInputStream.available();
                    bufferSize2 = Math.min(bytesAvailable2, maxBufferSize);
                    buffer2 = new byte[bufferSize2];
                        // read file and write it into form...
                    bytesRead2 = fileInputStream.read(buffer2, 0, bufferSize2);
                    
                    while (bytesRead2 > 0) {
                        
                        
                        dos.write(buffer2, 0, bufferSize2);
                        bytesAvailable2 = fileInputStream.available();
                        bufferSize2 = Math.min(bytesAvailable2, maxBufferSize);
                        bytesRead2 = fileInputStream.read(buffer2, 0, bufferSize2);
                        
                        
                    }
                    
                        // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    fileInputStream.close();
                }




                //conn.setRequestMethod("GET");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + '\n');
                }

                String jsonString = stringBuilder.toString();
                Log.e("jsonString", jsonString);
                JSONObject resJson = new JSONObject(jsonString);
                floatMessage = resJson.getString("status");
                Log.e("floatMessage", floatMessage);
                dos.flush();
                dos.close();



            try {

                if( f != null )
                {
                    if (f.exists()) {
                        f.delete();
                    }
                }
                if( f2 != null )
                {
                    if (f2.exists()) {
                        f2.delete();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return true;

        }
        catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }
}
