package com.emergencyskills.doe.aed.UI.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emergencyskills.doe.aed.R;
import com.emergencyskills.doe.aed.UI.activity.TabsActivity;
import com.emergencyskills.doe.aed.utils.Constants;
import com.emergencyskills.doe.aed.utils.MyToast;
import com.emergencyskills.doe.aed.utils.Networkstate;
import com.emergencyskills.doe.aed.utils.listops;
import com.emergencyskills.doe.aed.adapters.PendingUploadAdapter;
import com.emergencyskills.doe.aed.models.PendingUploadModel;
import com.emergencyskills.doe.aed.models.PickSchoolModel;
import com.emergencyskills.doe.aed.models.Schoolinfomodel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by naveen on 11/18/2015.
 */
public class PendingUploadsFragment extends Fragment implements OnClickListener {

    private ListView lvPendingUploads;
    public static final String ARG_SECTION_NUMBER = "section_number";
    private Button upload;
    ArrayList<Schoolinfomodel> drilllist,servicelist,installlist;
    ArrayList<PendingUploadModel> pendingUploadlist;
    ListAdapter adapter;
    JSONObject pending=new JSONObject();
    String drillsign="no",servicesign="no",installsign="no";
    String drilltime,drillname,drilldate,servicetime,servicedate,servicename,installtime,installdate,installname,drilldata,servicedata,installdata;
   String drillesrimage,drillesrname,serviceesrimage,servicecesrname,installesrimage,installesrname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pending_uploads, container, false);
        // Toast.makeText(getActivity(), "pending upload", Toast.LENGTH_LONG).show();
        lvPendingUploads = (ListView) rootView.findViewById(R.id.lvSPendingUploads);
        //upload = (Button) rootView.findViewById(R.id.upload);
       // upload.setOnClickListener(this);


        listops listops=new listops(getActivity());
        lvPendingUploads.setAdapter(new PendingUploadAdapter(getActivity(),listops.getpendinglist()));








         adapter=lvPendingUploads.getAdapter();

        if (adapter != null) {


            ViewGroup vg = lvPendingUploads;
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, vg);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams par = lvPendingUploads.getLayoutParams();
            par.height = totalHeight + (lvPendingUploads.getDividerHeight() * (adapter.getCount() - 1));
            lvPendingUploads.setLayoutParams(par);
            lvPendingUploads.requestLayout();

        }
        return rootView;
    }


    public void removeduplicates(ArrayList<PendingUploadModel> arrayList) {
        for (int i = 0; i < arrayList.size() - 1; i++) {
            if (arrayList.size() > 2) {
                PendingUploadModel p = arrayList.get(i);
                PendingUploadModel p2 = arrayList.get(i + 1);
                String id1 = p.getSchoolID();
                String id2 = p2.getSchoolID();
                if (id1.equals(id2)) {
                    arrayList.remove(i + 1);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
       /* if (v.getId() == R.id.upload) {

            Networkstate ns=new Networkstate(getActivity());
            if(ns.haveNetworkConnection()) {


                listops listops = new listops(getActivity());
                ArrayList<Schoolinfomodel> drillist = listops.getdrilllist();
                ArrayList<Schoolinfomodel> serviceist = listops.getservicelist();
                ArrayList<Schoolinfomodel> installist = listops.getinstalllist();
                ArrayList<PendingUploadModel> pendinglist = listops.getpendinglist();
                if (pendinglist.size() > 0) {
                    pending = new JSONObject();
                    for (int i = 0; i < pendinglist.size(); i++) {


                            PendingUploadModel pendingUploadModel = pendinglist.get(i);
                            String code = pendingUploadModel.getCode();
                            if (pendingUploadModel.getDrillstatus().equals("Pending")) {
                                int check = checklistcontainscodeandset(code, drillist);
                                if (check != -1) {
                                    drillist.get(check).setDrillstatus("Completed");
                                    listops.putdrilllist(drillist);

                                    drillsign = drillist.get(check).getDrillsignimage();
                                    drilltime = drillist.get(check).getDrilltime();
                                    drillname = drillist.get(check).getDrillname();
                                    drilldate = drillist.get(check).getDrilldate();
                                    drilldata=drillist.get(check).getDrilldata();
                                    drillesrname=drillist.get(check).getEsrname();
                                    drillesrimage=drillist.get(check).getEsrimage();

                                    String sname=serviceist.get(check).getSchoolname();
                                    try {
                                        new Senddata().execute(drillsign,"uploadDrill",drilldata,drilldate,drilltime,drillname,"Sending"+" "+sname+" "+"Drill data",drillesrname,drillesrimage).get();
                                    }
                                    catch (Exception e)
                                    {
                                        Log.e("FS",e.getMessage());
                                    }


                                }


                            }

                            if (pendingUploadModel.getServicestatus().equals("Pending")) {
                                int check = checklistcontainscodeandset(code, serviceist);
                                if (check != -1) {
                                    serviceist.get(check).setServicestatus("Completed");
                                    listops.putservicelist(serviceist);


                                    servicesign = serviceist.get(check).getServicesignimage();
                                    servicename = serviceist.get(check).getServicename();

                                    servicedate = serviceist.get(check).getServicesdate();

                                    servicetime = serviceist.get(check).getServicetime();
                                    servicedata=serviceist.get(check).getServicedata();

                                    serviceesrimage = serviceist.get(check).getEsrimageser();
                                    servicecesrname=serviceist.get(check).getEsrnameser();

                                    String sname=serviceist.get(check).getSchoolname();
                                    try {
                                        new Senddata().execute(servicesign,"uploadServiceCall",servicedata,servicedate,servicetime,servicename,"Sending"+" "+sname+" "+"Service Call data",servicecesrname,serviceesrimage).get();
                                    }
                                    catch (Exception e)
                                    {
                                        Log.e("FS",e.getMessage());
                                    }

                                }


                            }
                            if (pendingUploadModel.getInstallstatus().equals("Pending")) {
                                int check = checklistcontainscodeandset(code, installist);
                                if (check != -1) {
                                    installist.get(check).setInstallstatus("Completed");
                                    listops.putinstallllist(installist);
                                    JSONObject drilldata = new JSONObject();

                                    installsign = installist.get(check).getServicesignimage();
                                    installsign = installist.get(check).getInstalldate();
                                    installsign = installist.get(check).getInstalltime();
                                    installsign = installist.get(check).getInstallname();
                                    installsign = installist.get(check).getNewinstalldata();
                                    installesrimage = installist.get(check).getEsrimageinstall();
                                    installesrname = installist.get(check).getEsrnameinstall();

                                    String sname=serviceist.get(check).getSchoolname();
                                    try {
                                        new Senddata().execute(installsign,"uploadNewInstall",installdata,installdate,installtime,installname,"Sending"+" "+sname+" "+"New Install data",installesrname,installesrimage).get();
                                    }
                                    catch (Exception e)
                                    {
                                        Log.e("FS",e.getMessage());
                                    }
                                }


                            }
                            pendinglist.remove(i);
                            listops.putpendinglist(pendinglist);





                    }

                    //new Senddata().execute();



                    ((RefreshList)getActivity()).onlistdatachanged();
                    MyToast.popmessage("Upload Successful",getActivity());

                }
                else
                {
                    MyToast.popmessage("No pending uploads",getActivity());
                }
            }
            else
            {
                MyToast.popmessage("No internet connection",getActivity());
            }



        }*/

    }

    private int checklistcontainscodeandset(String code,ArrayList<Schoolinfomodel> data) {
                   int i=-1;

                    for(int pass=0;pass<data.size();pass++)
                    {
                        if(data.get(pass).getSchoolcode().equals(code))
                        {
                            i=pass;
                        }
                    }
                    return  i;

    }

    public interface RefreshList
    {
        public void onlistdatachanged();
    }




    class Senddata extends AsyncTask<String, Integer, String> {

        ProgressDialog dialog;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize,bytesRead2, bytesAvailable2, bufferSize2;
        byte[] buffer,buffer2;
        int maxBufferSize = 1 * 1024 * 1024;

        String line = null;

        String floatMessage = "nodata";


        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(getActivity());
            dialog.show();
            dialog.setMessage("Sending Data");
            dialog.setCancelable(false);

            //create a file to write bitmap data

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {




                if(!urls[0].equals("no")) {


                    File f = new File(urls[0]);

                    FileInputStream fileInputStream = new FileInputStream(f);
                    File f2 = new File(urls[8]);

                    FileInputStream fileInputStream2 = new FileInputStream(f2);


                    //URL url = new URL("http://doe.emergencyskills.com/api/api.php");
                    URL url = new URL("http://workintelligent.com/TagFrame/webservice/testing");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    //  conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("media_file", urls[0]);


                    dos = new DataOutputStream(conn.getOutputStream());


                    dos.writeBytes(twoHyphens + boundary + lineEnd);

//Adding Parameter name


                    dos.writeBytes("Content-Disposition: form-data; name=\"apikey\"" + lineEnd);
                    //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                    //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes("daMEs26rufAqasw2pUYU"); // mobile_no is String variable
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);


                    //adding parameter description
                    dos.writeBytes("Content-Disposition: form-data; name=\"method\"" + lineEnd);
                    //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                    //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(urls[1]); // mobile_no is String variable
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);

                    //adding parameter  event id
                    dos.writeBytes("Content-Disposition: form-data; name=\"data\"" + lineEnd);
                    //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                    //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(urls[2]); // mobile_no is String variable
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);


                    //adding parameter  event id
                    dos.writeBytes("Content-Disposition: form-data; name=\"date\"" + lineEnd);
                    //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                    //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(urls[3]); // mobile_no is String variable
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);

                    //adding parameter  event id
                    dos.writeBytes("Content-Disposition: form-data; name=\"time\"" + lineEnd);
                    //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                    //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(urls[4]); // mobile_no is String variable
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);

                    //adding parameter  event id
                    dos.writeBytes("Content-Disposition: form-data; name=\"name\"" + lineEnd);
                    //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                    //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(urls[5]); // mobile_no is String variable
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    //adding parameter  event id

                    //adding parameter  event id
                    dos.writeBytes("Content-Disposition: form-data; name=\"esr_name\"" + lineEnd);
                    //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                    //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(urls[7]); // mobile_no is String variable
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    //adding parameter  event id


                    dos.writeBytes("Content-Disposition: form-data; name=\"media_file\";filename=\"" + urls[0] + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);


                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];
                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {


                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);


                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                    fileInputStream.close();


                    dos.writeBytes("Content-Disposition: form-data; name=\"media_file_esr\";filename=\"" + urls[8] + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);


                    // create a buffer of maximum size
                    bytesAvailable2 = fileInputStream2.available();
                    bufferSize2 = Math.min(bytesAvailable2, maxBufferSize);
                    buffer2 = new byte[bufferSize2];
                    // read file and write it into form...
                    bytesRead2 = fileInputStream2.read(buffer2, 0, bufferSize2);

                    while (bytesRead2 > 0) {


                        dos.write(buffer2, 0, bufferSize2);
                        bytesAvailable2 = fileInputStream2.available();
                        bufferSize2 = Math.min(bytesAvailable2, maxBufferSize);
                        bytesRead2 = fileInputStream2.read(buffer2, 0, bufferSize2);


                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                    //conn.setRequestMethod("GET");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();

                    fileInputStream2.close();
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

                }



            }
            catch (Exception e) {
                Log.e("das",String.valueOf(e.getMessage()));
            }
            return floatMessage;
        }




        protected void onPostExecute(String result) {
            dialog.dismiss();

            super.onPostExecute(result);
//            Log.e("afs",floatMessage);







        }
    }




    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;

    }


}


