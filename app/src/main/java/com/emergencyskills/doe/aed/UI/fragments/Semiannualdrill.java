package com.emergencyskills.doe.aed.UI.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emergencyskills.doe.aed.utils.CommonUtilities;
import com.emergencyskills.doe.aed.R;
import com.emergencyskills.doe.aed.UI.activity.CaptureSignature;
import com.emergencyskills.doe.aed.UI.activity.TabsActivity;
import com.emergencyskills.doe.aed.utils.Constants;
import com.emergencyskills.doe.aed.utils.MyToast;
import com.emergencyskills.doe.aed.utils.Networkstate;
import com.emergencyskills.doe.aed.utils.WebServiceHandler;

import com.emergencyskills.doe.aed.utils.listops;
import com.emergencyskills.doe.aed.models.OtherSchoolsinCampus;
import com.emergencyskills.doe.aed.models.PendingUploadModel;
import com.emergencyskills.doe.aed.models.PickSchoolModel;
import com.emergencyskills.doe.aed.models.Schoolinfomodel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
 * Created by Karan on 12/2/2015.
 */
public class Semiannualdrill extends Fragment implements OnClickListener{


    private TextView scaddress,scid,scprincipal,scprinemail,sccontact,sccontactemail,sccode,save,txtstopwatch,txttotalpoints,tstopstopwatch,tstartstopwatch;;
    private EditText semiancomments[];
    private RadioGroup pts[];
    private int[] pointid={R.id.pts1,R.id.pts2,R.id.pts3,R.id.pts4,R.id.pts5,R.id.pts6,R.id.pts7,R.id.pts8,R.id.pts9,
            R.id.pts10,R.id.pts11,R.id.pts12,R.id.pts13,R.id.pts14};


    private int[] cbid={R.id.cb1,R.id.cb2,R.id.cb3,R.id.cb4,R.id.cb5,R.id.cb6,R.id.cb7,R.id.cb8,R.id.cb9,
            R.id.cb10,R.id.cb11,R.id.cb12,R.id.cb13,R.id.cb14};

    private int[] semiancommentid={R.id.semiancomments1,R.id.semiancomments2,R.id.semiancomments3,R.id.semiancomments4,
            R.id.semiancomments5,R.id.semiancomments6,R.id.semiancomments7,R.id.semiancomments8,R.id.semiancomments9,
            R.id.semiancomments10,R.id.semiancomments11,R.id.semiancomments12,R.id.semiancomments13,R.id.semiancomments14,};

    private String principal,contact,code,id,address,phone,drillid,state,city,contactemail,contactphone,principalemail,principalphone;
    String zip;
    private ImageView editaddress,editcontact,editprincipal,editrespnders;

    private CheckBox starstopwatch,timecb2,timecb9,timecb12,faileddrillcb,cb1,cb3,cb4,cb5,cb6,cb7,cb8,cb10,cb11,cb12,cb13,cb14;

    private Integer totalpoints=0;
    private String points[]=new String[14];

    private EditText totalnoofresponders,totalnoofaed,semiantime2,semiantime9, semiantime12,edfailed;



    //stopwatch
    private long startTime = 0L;
    private Handler myHandler = new Handler();
    long timeInMillies = 0L;
    long timeSwap = 0L;
    long finalTime = 0L;

    int pos,insposition,serpostion;

    JSONObject drill;
    String sign_image,esr_image;
    String name,schoolname,time,date,esrname;

    ArrayList<Schoolinfomodel> drilllist,servicelist,installist;

    LinearLayout llothersc;
    ArrayList<String> otherschoolname=new ArrayList<>();
    String respondersname1, respondersname2, respondersname3, respondersname4, respondersname5, respondersname6;
    String responderschool1,responderschool2,responderschool3,responderschool4,responderschool5,responderschool6;

    CheckBox cbs[];




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_semi_annual_drill, container, false);
        //initailzign views
        initview(rootView);
        if(getArguments()!=null)
        {

            principalemail=getArguments().getString("principalemail");
            phone=getArguments().getString("phone");
            state=getArguments().getString("state");
            zip=getArguments().getString("zip");
            city=getArguments().getString("city");
            contactemail=getArguments().getString("contactemail");
            contactphone=getArguments().getString("contactphone");

            principal=getArguments().getString("principal");
            contact=getArguments().getString("contact");
            code=getArguments().getString("schoolcode");
            address=getArguments().getString("address");
            id=getArguments().getString("schoolid");
            drillid=getArguments().getString("drillid");


            pos=getArguments().getInt("pos");
            scprincipal.setText((principal+" "+principalemail));
            sccontact.setText((contact+" "+contactemail+" "+contactphone));
            sccode.setText(code);

            scaddress.setText(address+" "+city+" "+state+" "+zip);




        }
        listops listops=new listops(getActivity());
        drilllist=listops.getdrilllist();
        servicelist=listops.getservicelist();
        installist=listops.getinstalllist();
        Schoolinfomodel objdr=drilllist.get(pos);
        String id=objdr.getSchoolid();
        insposition=getposition(id, installist);
        serpostion=getposition(id,servicelist);




        addotherschools();


        return rootView;
    }

    private void addotherschools() {

        listops listops=new listops(getActivity());
        ArrayList<OtherSchoolsinCampus> othrsc=listops.getdrilllist().get(pos).getOtherSchoolsinCampuses();

        CommonUtilities.logMe( "hmmm how many? " + othrsc.size() );
        if(othrsc.size()>0) {
            for (int p = 0; p < othrsc.size(); p++) {

                CheckBox cb=new CheckBox(getActivity().getApplicationContext());

                cb.setText(othrsc.get(p).getCompanyname());
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String codevalue=buttonView.getText().toString();
                        if(isChecked)
                        {

                            if(!otherschoolname.contains(codevalue))
                            {
                                otherschoolname.add(codevalue);


                            }
                        }
                        else
                        {
                            if(otherschoolname.contains(codevalue))
                            {
                                otherschoolname.remove(codevalue);

                            }
                        }
                    }
                });
                llothersc.addView(cb);
            }
        }

    }

    private void initview(final View rootView) {



        totalnoofresponders=(EditText)rootView.findViewById(R.id.totalnumberofresponders);
        totalnoofaed=(EditText)rootView.findViewById(R.id.totalnumberofaedresponding);
        llothersc=(LinearLayout) rootView.findViewById(R.id.layoutforotherschool);


        tstartstopwatch=(TextView)rootView.findViewById(R.id.startstopwatch);
        tstartstopwatch.setOnClickListener(this);

        tstopstopwatch=(TextView)rootView.findViewById(R.id.stopstopwatch);
        tstopstopwatch.setOnClickListener(this);

        scaddress=(TextView)rootView.findViewById(R.id.address);
        scprincipal=(TextView)rootView.findViewById(R.id.principal);
        sccontact=(TextView)rootView.findViewById(R.id.semicontact);
        sccode=(TextView)rootView.findViewById(R.id.semisccode);

        save=(TextView)rootView.findViewById(R.id.savedrilldata);
        save.setOnClickListener(this);
        txtstopwatch=(TextView)rootView.findViewById(R.id.txtstopwatch);
        txttotalpoints=(TextView)rootView.findViewById(R.id.txttotalpoints);
        semiantime2=(EditText) rootView.findViewById(R.id.semiantime2);
        semiantime9=(EditText) rootView.findViewById(R.id.semiantime9);
        semiantime12=(EditText) rootView.findViewById(R.id.semiantime12);
       timecb2=(CheckBox)rootView.findViewById(R.id.cb2);
        timecb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    semiantime2.setText(txtstopwatch.getText().toString());
                }
            }
        });

       timecb12=(CheckBox)rootView.findViewById(R.id.cb12);
        timecb12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    semiantime12.setText(txtstopwatch.getText().toString());
                }
            }
        });

        timecb9=(CheckBox)rootView.findViewById(R.id.cb9);
        timecb9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    semiantime9.setText(txtstopwatch.getText().toString());
                }
            }
        });

        starstopwatch=(CheckBox)rootView.findViewById(R.id.checkstopwatch);




        //for editing
        editaddress=(ImageView)rootView.findViewById(R.id.imgaddress);
        editcontact=(ImageView)rootView.findViewById(R.id.imgcontact);
        editprincipal=(ImageView)rootView.findViewById(R.id.imgprincipal);
        //editschoolcode=(ImageView)rootView.findViewById(R.id.imgschoolcode);

        editrespnders=(ImageView)rootView.findViewById(R.id.imgparticipating);


        editaddress.setOnClickListener(this);
        editrespnders.setOnClickListener(this);
        editcontact.setOnClickListener(this);
        editprincipal.setOnClickListener(this);
        //editschoolcode.setOnClickListener(this);



        semiancomments=new EditText[14];
        pts=new RadioGroup[14];
        cbs=new CheckBox[14];
        for(int i=0;i<14;i++)
        {


            pts[i]=(RadioGroup) rootView.findViewById(pointid[i]);

            semiancomments[i]=(EditText)rootView.findViewById(semiancommentid[i]);
            cbs[i]=(CheckBox)rootView.findViewById(cbid[i]);

            pts[i].setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    for(int i=0;i<14;i++)
                    {


                        // find the radiobutton by returned id
                       // RadioButton rd= (RadioButton) rootView.findViewById(selectedId);
                       int id= pts[i].getCheckedRadioButtonId();

                        RadioButton radioButton = (RadioButton) rootView.findViewById(id);
                        String p=radioButton.getText().toString();
                        int point=Integer.parseInt(p);
                        points[i]=String.valueOf(point);


                        totalpoints=totalpoints+point;
                    }
                    txttotalpoints.setText(String.valueOf(totalpoints));
                    //txttotalpoints.setText(radioButton.getText().toString());
                    totalpoints=0;
                    /*int id= pts[0].getCheckedRadioButtonId();

                    RadioButton radioButton = (RadioButton) rootView.findViewById(id);
                    radioButton.getText();*/

                }
            });


        }





        //failedcheckbox
        faileddrillcb=(CheckBox)rootView.findViewById(R.id.fdrillcheck);
        edfailed=(EditText)rootView.findViewById(R.id.faileddrilleditext);


    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.startstopwatch)
        {
            startTime = SystemClock.uptimeMillis();
            myHandler.postDelayed(updateTimerMethod, 0);
            starstopwatch.setChecked(true);
            tstartstopwatch.setText("Restart");
        }
        if(v.getId()==R.id.stopstopwatch)
        {
            timeSwap += timeInMillies;
            myHandler.removeCallbacks(updateTimerMethod);
            starstopwatch.setChecked(false);
            tstartstopwatch.setText("Start");
        }


        if(v.getId()==R.id.savedrilldata)
        {

            //jsonencode
            boolean allset=true;

            String noofresponders=totalnoofresponders.getText().toString();
            String noofaed=totalnoofaed.getText().toString();
            String comments[]=new String[14];
            String cbvalue[]=new String[14];
            for(int i=0;i<semiancomments.length;i++)
            {
                comments[i]=semiancomments[i].getText().toString();
                if(comments[i].isEmpty())
                {
                    comments[i]="";
                }
                cbvalue[i]=cbs[i].isChecked()?"yes":"no";
            }
            String totalpoint=txttotalpoints.getText().toString();
            /*if(noofresponders.isEmpty()||noofaed.isEmpty())
            {
                allset=false;
            }*/
            if(allset) {


                JSONObject drilldata = new JSONObject();


                JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < 14; i++) {
                    JSONObject jsonObject = new JSONObject();
                    if (points[i] == null) {
                        points[i] = "0";
                    }

                    try {
                        if (i == 1) {

                            jsonObject.put("stepnumber", String.valueOf(i + 1));
                            jsonObject.put("points", points[i]);
                            jsonObject.put("time", semiantime2.getText().toString());
                            jsonObject.put("comments", comments[i]);
                            jsonObject.put("ischecked", cbvalue[i]);

                        } else if (i == 8) {
                            jsonObject.put("stepnumber", String.valueOf(i + 1));
                            jsonObject.put("points", points[i]);
                            jsonObject.put("time", semiantime9.getText().toString());
                            jsonObject.put("comments", comments[i]);
                            jsonObject.put("ischecked", cbvalue[i]);
                        } else if (i == 11) {
                            jsonObject.put("stepnumber", String.valueOf(i + 1));
                            jsonObject.put("points", points[i]);
                            jsonObject.put("time", semiantime12.getText().toString());
                            jsonObject.put("comments", comments[i]);
                            jsonObject.put("ischecked", cbvalue[i]);
                        } else {
                            jsonObject.put("stepnumber", String.valueOf(i + 1));
                            jsonObject.put("points", points[i]);
                            jsonObject.put("time", "");
                            jsonObject.put("comments", comments[i]);
                            jsonObject.put("ischecked", cbvalue[i]);
                        }
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        Log.e("aa", e.getMessage());
                    }
                }


                try {
                    drilldata.put("stepsdata", jsonArray);
                    drilldata.put("totalpoints", totalpoint);
                    drilldata.put("totaltime", txtstopwatch.getText().toString());
                    drilldata.put("number_of_responders", noofresponders);
                    drilldata.put("number_of_aed_responding", noofaed);

                } catch (Exception e) {
                    Log.e("fa", e.getMessage());
                }

                drill = new JSONObject();
                try {
                    drill.put("id", id);
                    drill.put("address", address);
                    drill.put("city", city);
                    drill.put("state", state);
                    drill.put("zip", zip);
                    drill.put("phone", phone);
                    drill.put("principal", principal);
                    drill.put("principalemail", principalemail);
                    drill.put("contact", contact);
                    drill.put("contactemail", contactemail);
                    drill.put("contactphone", contactphone);
                    drill.put("respondername1",respondersname1);
                    drill.put("respondername2",respondersname2);
                    drill.put("respondername3",respondersname3);
                    drill.put("respondername4",respondersname4);
                    drill.put("respondername5",respondersname5);
                    drill.put("respondername6",respondersname6);


                    drill.put("responderschool1",responderschool1);
                    drill.put("responderschool2",responderschool2);
                    drill.put("responderschool3",responderschool3);
                    drill.put("responderschool4",responderschool4);
                    drill.put("responderschool5",responderschool5);
                    drill.put("responderschool6",responderschool6);
                    drill.put("isdrillfailed",faileddrillcb.isChecked()?"yes":"no");
                    drill.put("faileddrill",edfailed.getText().toString());
                    drill.put("code", code);
                    String oth="";
                    if(otherschoolname.size()>0) {
                        for (int pass = 0; pass < otherschoolname.size(); pass++) {
                            oth = oth + "," + otherschoolname.get(pass);
                        }
                    }
                    drill.put("Other_school_participating",oth);
                    drill.put("drillid", drillid);
                    drill.put("status", "success");
                    drill.put("drillinfo", drilldata);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("fa",drill.toString());
                listops listops=new listops(getActivity());
                ArrayList<Schoolinfomodel> schoolinfomodelslist=listops.getdrilllist();


                CommonUtilities.logMe( "hmm about to start senddata" );
                if( 1 == 0 ) {
                    
                Intent intent = new Intent(getActivity(), CaptureSignature.class);
                intent.putExtra("sname",schoolinfomodelslist.get(pos).getSchoolname());
                
                startActivityForResult(intent,1);
                }
                else
                {

                    Networkstate nwst=new Networkstate(getActivity());
                    if(nwst.haveNetworkConnection())
                    {
                            //Toast.makeText(getActivity(),"Have internet,send the data to server",Toast.LENGTH_SHORT).show();
                        CommonUtilities.logMe( "we have a connection! " );
                        new Senddata().execute();
                    }
                    else
                    {
                        CommonUtilities.logMe( "nope, pending! " );
                        
                            //Schoolinfomodel schoolinfomodel=schoolinfomodelslist.get(pos);
                        schoolinfomodelslist.get(pos).setDrillstatus("Pending");
                        schoolinfomodelslist.get(pos).setDrilldata(drill.toString());
                        // schoolinfomodelslist.get(pos).setDrillsignimage(sign_image);
                        schoolinfomodelslist.get(pos).setDrilldate(CaptureSignature.getTodaysDate());
                        schoolinfomodelslist.get(pos).setDrilltime(CaptureSignature.getCurrentTime());
                        // schoolinfomodelslist.get(pos).setDrillname(name);
                        // schoolinfomodelslist.get(pos).setSchoolnamefromsign(schoolname);
                        
                        // schoolinfomodelslist.get(pos).setEsrname(esrname);
                        // schoolinfomodelslist.get(pos).setEsrimage(esr_image);
                        
                        MyToast.popmessage("Added to Pending Uploads",getActivity());
                            // schoolinfomodel.setDrilldata(drill.toString());
                        
                        
                        listops.putdrilllist(schoolinfomodelslist);
                        
                        addpendinglist(schoolinfomodelslist.get(pos).getSchoolcode(),schoolinfomodelslist.get(pos).getname(),schoolinfomodelslist.get(pos).getNewinstalldata());
                        
                        ((GoToServiceCall)getActivity()).goToServiceCall(schoolinfomodelslist.get(pos).getSchoolid());
//                        ((SetDrillsList)getActivity()).setupDrillList(1);
                        
                        
                    }
                    
                }

                //Log.e("d", drill.toString());


            }
            else
            {
                MyToast.popmessage("Please fill in all the fields",getActivity());
            }







        }
        else if(v.getId()==R.id.imgaddress)


        {


            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_address);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(android.R.color.transparent)));
            Button update=(Button)dialog.findViewById(R.id.editupdate);

            final EditText edstaddress=(EditText)dialog.findViewById(R.id.editstaddress);
            final EditText edcity=(EditText)dialog.findViewById(R.id.editcity);
            final EditText edstate=(EditText)dialog.findViewById(R.id.editstate);
            final EditText edzip=(EditText)dialog.findViewById(R.id.editzip);
            final EditText edphn=(EditText)dialog.findViewById(R.id.editphone);
            edstaddress.setText(address);
            edcity.setText(city);
            edstate.setText(state);
            edphn.setText(phone);
            edzip.setText(zip);
            TextView txt=(TextView) dialog.findViewById(R.id.editinfotittle);
            txt.setText("Address and Phone");
            update.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                     address=edstaddress.getText().toString();
                   city=edcity.getText().toString();
                    state=edstate.getText().toString();
                     zip=edzip.getText().toString();
                     phone=edphn.getText().toString();
                    if(!address.isEmpty()&&!city.isEmpty()&&!state.isEmpty()&&!zip.isEmpty()&&!phone.isEmpty())
                    {
                        scaddress.setText(address+" "+city+" "+state+" "+zip+" "+phone);

                        //change in drilllist
                        Schoolinfomodel objdr=drilllist.get(pos);

                        objdr.setAddress(address);
                        objdr.setCity(city);
                        objdr.setState(state);
                        objdr.setZip(zip);
                        objdr.setPhone(phone);

                        //changes in installlist
                        if(insposition!=-1) {
                            Schoolinfomodel objin = installist.get(insposition);

                            objin.setAddress(address);
                            objin.setCity(city);
                            objin.setState(state);
                            objin.setZip(zip);
                            objin.setPhone(phone);
                        }

                        //changes in servicelist
                            if(serpostion!=-1) {
                                Schoolinfomodel objser = servicelist.get(serpostion);

                                objser.setAddress(address);
                                objser.setCity(city);
                                objser.setState(state);
                                objser.setZip(zip);
                                objser.setPhone(phone);
                            }

                        listops listops=new listops(getActivity());
                        listops.putdrilllist(drilllist);
                        listops.putinstallllist(installist);
                        listops.putservicelist(servicelist);








                        dialog.dismiss();
                    }
                    else
                    {
                       MyToast.popmessage("Please Enter all the field",getActivity());
                    }
                }
            });
            dialog.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialog.show();

        }

        else if(v.getId()==R.id.imgcontact)
        {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_edit_prin_contacat);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(android.R.color.transparent)));
            Button update=(Button)dialog.findViewById(R.id.editupdate);

            final EditText ednme=(EditText)dialog.findViewById(R.id.editname);
            final EditText edphn=(EditText)dialog.findViewById(R.id.editphone);
            final EditText edemail=(EditText)dialog.findViewById(R.id.editemail);
            TextView txt=(TextView) dialog.findViewById(R.id.editinfotittle);
            ednme.setText(contact);
            edemail.setText(contactemail);
            edphn.setText(contactphone);
            txt.setText("Contact");
            update.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactphone=edphn.getText().toString();
                    contact=ednme.getText().toString();
                    contactemail=edemail.getText().toString();
                    if(!contactphone.isEmpty()&&!contact.isEmpty()&&!contactemail.isEmpty())
                    {
                        sccontact.setText(contact+" "+contactemail+" "+contactphone);
                        Schoolinfomodel objdr=drilllist.get(pos);

                        objdr.setContactphone(contactphone);
                        objdr.setAedcontact(contact);
                        objdr.setAedcontactemail(contactemail);


                        //changes in installlist
                        if(insposition!=-1) {
                            Schoolinfomodel objin = installist.get(insposition);

                            objin.setContactphone(contactphone);
                            objin.setAedcontact(contact);
                            objin.setAedcontactemail(contactemail);
                        }

                        //changes in servicelist
                        if(serpostion!=-1) {
                            Schoolinfomodel objser = servicelist.get(serpostion);

                            objser.setContactphone(contactphone);
                            objser.setAedcontact(contact);
                            objser.setAedcontactemail(contactemail);
                        }

                        listops listops=new listops(getActivity());
                        listops.putdrilllist(drilllist);
                        listops.putinstallllist(installist);
                        listops.putservicelist(servicelist);
                        dialog.dismiss();
                    }
                    else
                    {
                        MyToast.popmessage("Please Enter all Fields",getActivity());
                    }
                }
            });
            dialog.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
        else if(v.getId()==R.id.imgprincipal)
        {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dailog_editpricipal);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(android.R.color.transparent)));
            Button update=(Button)dialog.findViewById(R.id.editupdate);

            final EditText ednme=(EditText)dialog.findViewById(R.id.editname);

            final EditText edemail=(EditText)dialog.findViewById(R.id.editemail);
            TextView txt=(TextView) dialog.findViewById(R.id.editinfotittle);
            ednme.setText(principal);
            edemail.setText(principalemail);

            txt.setText("Principal");
            update.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    principal=ednme.getText().toString();
                    principalemail=edemail.getText().toString();
                    if(!principal.isEmpty()&&!principalemail.isEmpty())
                    {
                        scprincipal.setText(principal+" "+principalemail);
                        Schoolinfomodel objdr=drilllist.get(pos);

                        objdr.setPrincipal(principal);
                        objdr.setPrincemail(principalemail);


                        //changes in installlist
                        if(insposition!=-1) {
                            Schoolinfomodel objin = installist.get(insposition);

                            objin.setPrincipal(principal);
                            objin.setPrincemail(principalemail);
                        }

                        //changes in servicelist
                        if(serpostion!=-1) {
                            Schoolinfomodel objser = servicelist.get(serpostion);

                            objser.setPrincipal(principal);
                            objser.setPrincemail(principalemail);
                        }

                        listops listops=new listops(getActivity());
                        listops.putdrilllist(drilllist);
                        listops.putinstallllist(installist);
                        listops.putservicelist(servicelist);

                        dialog.dismiss();
                    }
                    else
                    {
                        MyToast.popmessage("Please Enter all Fields",getActivity());
                    }
                }
            });
            dialog.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
      /*  else if(v.getId()==R.id.imgschoolcode)
        {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_editinfo);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(android.R.color.transparent)));
            Button update=(Button)dialog.findViewById(R.id.editupdate);

            final EditText ed=(EditText)dialog.findViewById(R.id.editfield);
            TextView txt=(TextView) dialog.findViewById(R.id.editinfotittle);
            txt.setText("School Code");
            ed.setText(code);
            update.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    code=ed.getText().toString();
                    if(!code.isEmpty())
                    {
                        sccode.setText(code);
                        Schoolinfomodel objdr=drilllist.get(pos);

                        objdr.setSchoolcode(code);


                        //changes in installlist
                        if(insposition!=-1) {
                            Schoolinfomodel objin = installist.get(insposition);

                            objin.setSchoolcode(code);

                        }

                        //changes in servicelist
                        if(serpostion!=-1) {
                            Schoolinfomodel objser = servicelist.get(serpostion);

                            objser.setSchoolcode(code);
                        }

                        listops listops=new listops(getActivity());
                        listops.putdrilllist(drilllist);
                        listops.putinstallllist(installist);
                        listops.putservicelist(servicelist);
                        dialog.dismiss();
                    }
                    else
                    {
                        ed.setHint("Please enter");
                    }
                }
            });
            dialog.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }*/


        else if(v.getId()==R.id.imgparticipating)
        {
            final Dialog dialog = new Dialog(getActivity(),R.style.DialogStyle);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_update_responders);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(android.R.color.transparent)));
            dialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            Button update=(Button)dialog.findViewById(R.id.editupdate);

            final EditText edname=(EditText)dialog.findViewById(R.id.partname1);
            final EditText edname1=(EditText)dialog.findViewById(R.id.partname2);
            final EditText edname2=(EditText)dialog.findViewById(R.id.partname3);
            final EditText edname3=(EditText)dialog.findViewById(R.id.partname4);
            final EditText edname4=(EditText)dialog.findViewById(R.id.partname5);
            final EditText edname5=(EditText)dialog.findViewById(R.id.partname6);
            edname.setText(respondersname1);
            edname1.setText(respondersname2);
            edname2.setText(respondersname3);
            edname3.setText(respondersname4);
            edname4.setText(respondersname5);
            edname5.setText(respondersname6);

            final EditText edschool=(EditText)dialog.findViewById(R.id.partschool1);
            final EditText edschool1=(EditText)dialog.findViewById(R.id.partschool2);
            final EditText edschool2=(EditText)dialog.findViewById(R.id.partschool3);
            final EditText edschool3=(EditText)dialog.findViewById(R.id.partschool4);
            final EditText edschool4=(EditText)dialog.findViewById(R.id.partschool5);
            final EditText edschool5=(EditText)dialog.findViewById(R.id.partschool6);
            edschool.setText(responderschool1);
            edschool1.setText(responderschool2);
            edschool2.setText(responderschool3);
            edschool3.setText(responderschool4);
            edschool4.setText(responderschool5);
            edschool5.setText(responderschool6);



            update.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    respondersname1=edname.getText().toString();
                    respondersname2=edname1.getText().toString();
                    respondersname3=edname2.getText().toString();
                    respondersname4=edname3.getText().toString();
                    respondersname5=edname4.getText().toString();
                    respondersname6=edname5.getText().toString();

                    responderschool1=edschool.getText().toString();
                    responderschool2=edschool1.getText().toString();
                    responderschool3=edschool2.getText().toString();
                    responderschool4=edschool3.getText().toString();
                    responderschool5=edschool4.getText().toString();
                    responderschool6=edschool5.getText().toString();
                    dialog.dismiss();
                }

            });
            dialog.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }

    }

    private int getposition(String id, ArrayList<Schoolinfomodel> drilllist) {
        int dup=-1;

        for(int i=0;i<drilllist.size();i++)
        {
            if(id.equals(drilllist.get(i).getSchoolid()))
            {
                dup=i;
            }
        }
        return dup;

    }


    private Runnable updateTimerMethod = new Runnable() {

        public void run() {
            timeInMillies=SystemClock.uptimeMillis()-startTime;
            finalTime = timeSwap + timeInMillies;

            int seconds = (int) (finalTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (finalTime % 1000);
            txtstopwatch.setText(""+ minutes + ":"
            + String.format("%02d", seconds));
            /*+ ":"
            + String.format("%03d", milliseconds));*/
            myHandler.postDelayed(this, 0);
        }

    };


        // don't think this is used anymore! 
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
//         switch(requestCode) {
//             case 1:
//                 if (resultCode == getActivity().RESULT_OK) {

//                     Bundle bundle = data.getExtras();
//                     String status  = bundle.getString("status");
//                     if(status.equalsIgnoreCase("done")){
//                         String sign=bundle.getString("sign");
//                         sign_image=bundle.getString("path");
//                         name=bundle.getString("name");
//                         date=bundle.getString("date");
//                         time=bundle.getString("time");
//                         schoolname=bundle.getString("sname");
//                         esr_image=bundle.getString("pathesr");
//                         esrname=bundle.getString("nameesr");



//                         listops listops=new listops(getActivity());
//                         ArrayList<Schoolinfomodel> schoolinfomodelslist=listops.getdrilllist();





//                         Networkstate nwst=new Networkstate(getActivity());
//                         if(nwst.haveNetworkConnection())
//                         {
//                             //Toast.makeText(getActivity(),"Have internet,send the data to server",Toast.LENGTH_SHORT).show();
//                            new Senddata().execute();
//                         }
//                         else
//                         {



//                             //Schoolinfomodel schoolinfomodel=schoolinfomodelslist.get(pos);
//                             schoolinfomodelslist.get(pos).setDrillstatus("Pending");
//                             schoolinfomodelslist.get(pos).setDrilldata(drill.toString());
//                             schoolinfomodelslist.get(pos).setDrillsignimage(sign_image);
//                             schoolinfomodelslist.get(pos).setDrilldate(date);
//                             schoolinfomodelslist.get(pos).setDrilltime(time);
//                             schoolinfomodelslist.get(pos).setDrillname(name);
//                             schoolinfomodelslist.get(pos).setSchoolnamefromsign(schoolname);

//                             schoolinfomodelslist.get(pos).setEsrname(esrname);
//                             schoolinfomodelslist.get(pos).setEsrimage(esr_image);

//                             MyToast.popmessage("Added to Pending Uploads",getActivity());
//                             // schoolinfomodel.setDrilldata(drill.toString());


//                             listops.putdrilllist(schoolinfomodelslist);

//                             addpendinglist(schoolinfomodelslist.get(pos).getSchoolcode(),schoolinfomodelslist.get(pos).getname(),schoolinfomodelslist.get(pos).getNewinstalldata());

//                             ((GoToServiceCall)getActivity()).goToServiceCall(schoolinfomodelslist.get(pos).getSchoolid());

// //                            ((SetDrillsList)getActivity()).setupDrillList(1);




//                         }



//                     }
//                 }
//                 break;
//         }

    }

    private void addpendinglist(String schoolcode,String name,String data) {
        listops listops=new listops(getActivity());
        ArrayList<PendingUploadModel> pendinglist=listops.getpendinglist();
        if(pendinglist.size()>0)
        {
            int position= doeslisthavethisschool(schoolcode);
            if(position!=-1) {
                PendingUploadModel pend = pendinglist.get(position);
                pend.setCode(schoolcode);
                pend.setSchoolName(name);
                pend.setDrillstatus("Pending");
                pend.setDrilldata(data);
                Log.e("fa",position+"");
                pendinglist.set(position,pend);
            }
            else
            {
                PendingUploadModel pend =new PendingUploadModel();
                pend.setCode(schoolcode);
                pend.setSchoolName(name);
                pend.setDrillstatus("Pending");
                pend.setDrilldata(data);
                pendinglist.add(pend);
            }

        }
        else
        {
            PendingUploadModel pend=new PendingUploadModel();
            pend.setCode(schoolcode);
            pend.setSchoolName(name);
            pend.setDrillstatus("Pending");
            pend.setDrilldata(data);
            pendinglist.add(pend);

        }

        listops.putpendinglist(pendinglist);
    }



    private int  doeslisthavethisschool(String code) {


        listops listops=new listops(getActivity());
        ArrayList<PendingUploadModel> pendinglist=listops.getpendinglist();
        int posi=-1;
        for(int i=0;i<pendinglist.size();i++)
        {
            if(pendinglist.get(i).getCode().equals(code))
            {
                posi=i;
            }

        }
        return posi;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    class Senddata extends AsyncTask<String, Integer, String> {

        ProgressDialog dialog;
        listops listops=new listops(getActivity());
//        File f,f2;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize,bytesRead2, bytesAvailable2, bufferSize2;
        byte[] buffer,buffer2;
        int maxBufferSize = 1 * 1024 * 1024;

        String line = null;

        String floatMessage = null;


        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(getActivity());
            dialog.show();
            dialog.setMessage("Sending Data");
            dialog.setCancelable(false);

            CommonUtilities.logMe( "SENDDATA pre execute" );

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {

                CommonUtilities.logMe( "sending the drill..." );


                URL url = new URL("http://doe.emergencyskills.com/api/api.php");
                    //URL url = new URL("http://workintelligent.com/TagFrame/webservice/testing");
                
                
                
                
                
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                    //  conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    //     conn.setRequestProperty("media_file", sign_image);
                    // conn.setRequestProperty("media_file_esr", esr_image);
                
                
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
                dos.writeBytes("uploadDrill"); // mobile_no is String variable
                dos.writeBytes(lineEnd);
                
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                
                    //adding parameter  event id
                    dos.writeBytes("Content-Disposition: form-data; name=\"data\"" + lineEnd);
                    //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                    //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(drill.toString()); // mobile_no is String variable
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);


                //adding parameter  event id
                dos.writeBytes("Content-Disposition: form-data; name=\"date\"" + lineEnd);
                //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(CaptureSignature.getTodaysDate()); // mobile_no is String variable
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);

                //adding parameter  event id
                dos.writeBytes("Content-Disposition: form-data; name=\"time\"" + lineEnd);
                //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(CaptureSignature.getCurrentTime()); // mobile_no is String variable
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);


                if( 1 == 0 )
                {
                        //adding parameter  event id
                    dos.writeBytes("Content-Disposition: form-data; name=\"schoolname\"" + lineEnd);
                        //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                        //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(schoolname); // mobile_no is String variable
                    dos.writeBytes(lineEnd);
                    
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    
                        //adding parameter  event id
                    dos.writeBytes("Content-Disposition: form-data; name=\"name\"" + lineEnd);
                        //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                        //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);
                    
                    dos.writeBytes(name); // mobile_no is String variable
                    dos.writeBytes(lineEnd);
                    
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                        //adding parameter  event id


                        //adding parameter  event id
                    dos.writeBytes("Content-Disposition: form-data; name=\"esr_name\"" + lineEnd);
                        //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                        //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                    dos.writeBytes(lineEnd);
                    
                    dos.writeBytes(esrname); // mobile_no is String variable
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


                // dos.writeBytes("Content-Disposition: form-data; name=\"media_file_esr\";filename=\"" + esr_image + "\"" + lineEnd);
//                dos.writeBytes(lineEnd);


                // // create a buffer of maximum size
                // bytesAvailable = fileInputStream2.available();
                // bufferSize = Math.min(bytesAvailable, maxBufferSize);
                // buffer = new byte[bufferSize];
                // // read file and write it into form...
                // bytesRead = fileInputStream2.read(buffer, 0, bufferSize);

                // while (bytesRead > 0) {


                //     dos.write(buffer, 0, bufferSize);
                //     bytesAvailable = fileInputStream2.available();
                //     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //     bytesRead = fileInputStream2.read(buffer, 0, bufferSize);


                // }

                // // send multipart form data necessary after file data...
                // dos.writeBytes(lineEnd);
                // dos.writeBytes(twoHyphens + boundary + lineEnd) ;
                // fileInputStream2.close();;




//                    dos.writeBytes("Content-Disposition: form-data; name=\"media_file\";filename=\"" + sign_image + "\"" + lineEnd);
//                    dos.writeBytes(lineEnd);



                    // create a buffer of maximum size
                    // bytesAvailable2 = fileInputStream.available();
                    // bufferSize2 = Math.min(bytesAvailable2, maxBufferSize);
                    // buffer2 = new byte[bufferSize2];
                    // // read file and write it into form...
                    // bytesRead2 = fileInputStream.read(buffer2, 0, bufferSize2);

                    // while (bytesRead2 > 0) {


                    //     dos.write(buffer2, 0, bufferSize2);
                    //     bytesAvailable2 = fileInputStream.available();
                    //     bufferSize2 = Math.min(bytesAvailable2, maxBufferSize);
                    //     bytesRead2 = fileInputStream.read(buffer2, 0, bufferSize2);


                    // }

                    // // send multipart form data necessary after file data...
                    // dos.writeBytes(lineEnd);
                    // dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                    // fileInputStream2.close();



                    //conn.setRequestMethod("GET");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();


                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line + '\n');
                    }

                    String jsonString = stringBuilder.toString();
                    Log.e("jsonString", jsonString);
                    CommonUtilities.logMe( "json string: " + jsonString );
                    JSONObject resJson = new JSONObject(jsonString);
                    floatMessage = resJson.getString("status");
                    CommonUtilities.logMe( "message!: " + floatMessage );
                    Log.e("floatMessage", floatMessage);
                    dos.flush();
                    dos.close();



            }
            catch (Exception e) {
                e.printStackTrace();
                Log.e("das",String.valueOf(e.getMessage()));
            }
            return floatMessage;
        }


//         @Override
//         protected String doInBackgroundOLD(String... urls) {
//             try {




//                 f= new File(sign_image);
//                 f2=new File(esr_image);

//                     FileInputStream fileInputStream = new FileInputStream(f);



//                 FileInputStream fileInputStream2 = new FileInputStream(f2);


//               URL url = new URL("http://doe.emergencyskills.com/api/api.php");
//                 //URL url = new URL("http://workintelligent.com/TagFrame/webservice/testing");





//                     conn = (HttpURLConnection) url.openConnection();
//                     conn.setDoInput(true); // Allow Inputs
//                     conn.setDoOutput(true); // Allow Outputs
//                     conn.setUseCaches(false); // Don't use a Cached Copy
//                     conn.setRequestMethod("POST");
//                     conn.setRequestProperty("Connection", "Keep-Alive");
//                     //  conn.setRequestProperty("ENCTYPE", "multipart/form-data");
//                     conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                     conn.setRequestProperty("media_file", sign_image);
//                 conn.setRequestProperty("media_file_esr", esr_image);


//                     dos = new DataOutputStream(conn.getOutputStream());


//                     dos.writeBytes(twoHyphens + boundary + lineEnd);

// //Adding Parameter name


//                     dos.writeBytes("Content-Disposition: form-data; name=\"apikey\"" + lineEnd);
//                     //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
//                     //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
//                     dos.writeBytes(lineEnd);
//                     dos.writeBytes("daMEs26rufAqasw2pUYU"); // mobile_no is String variable
//                     dos.writeBytes(lineEnd);

//                     dos.writeBytes(twoHyphens + boundary + lineEnd);


//                     //adding parameter description
//                     dos.writeBytes("Content-Disposition: form-data; name=\"method\"" + lineEnd);
//                     //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
//                     //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
//                     dos.writeBytes(lineEnd);
//                     dos.writeBytes("uploadDrill"); // mobile_no is String variable
//                     dos.writeBytes(lineEnd);

//                     dos.writeBytes(twoHyphens + boundary + lineEnd);

//                     //adding parameter  event id
//                     dos.writeBytes("Content-Disposition: form-data; name=\"data\"" + lineEnd);
//                     //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
//                     //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
//                     dos.writeBytes(lineEnd);
//                     dos.writeBytes(drill.toString()); // mobile_no is String variable
//                     dos.writeBytes(lineEnd);

//                     dos.writeBytes(twoHyphens + boundary + lineEnd);


//                 //adding parameter  event id
//                 dos.writeBytes("Content-Disposition: form-data; name=\"date\"" + lineEnd);
//                 //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
//                 //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
//                 dos.writeBytes(lineEnd);
//                 dos.writeBytes(date); // mobile_no is String variable
//                 dos.writeBytes(lineEnd);

//                 dos.writeBytes(twoHyphens + boundary + lineEnd);

//                 //adding parameter  event id
//                 dos.writeBytes("Content-Disposition: form-data; name=\"time\"" + lineEnd);
//                 //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
//                 //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
//                 dos.writeBytes(lineEnd);
//                 dos.writeBytes(time); // mobile_no is String variable
//                 dos.writeBytes(lineEnd);

//                 dos.writeBytes(twoHyphens + boundary + lineEnd);


//                 //adding parameter  event id
//                 dos.writeBytes("Content-Disposition: form-data; name=\"schoolname\"" + lineEnd);
//                 //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
//                 //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
//                 dos.writeBytes(lineEnd);
//                 dos.writeBytes(schoolname); // mobile_no is String variable
//                 dos.writeBytes(lineEnd);

//                 dos.writeBytes(twoHyphens + boundary + lineEnd);

//                     //adding parameter  event id
//                     dos.writeBytes("Content-Disposition: form-data; name=\"name\"" + lineEnd);
//                     //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
//                     //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
//                     dos.writeBytes(lineEnd);

//                     dos.writeBytes(name); // mobile_no is String variable
//                     dos.writeBytes(lineEnd);

//                     dos.writeBytes(twoHyphens + boundary + lineEnd);
//                     //adding parameter  event id


//                 //adding parameter  event id
//                 dos.writeBytes("Content-Disposition: form-data; name=\"esr_name\"" + lineEnd);
//                 //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
//                 //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
//                 dos.writeBytes(lineEnd);

//                 dos.writeBytes(esrname); // mobile_no is String variable
//                 dos.writeBytes(lineEnd);

//                 dos.writeBytes(twoHyphens + boundary + lineEnd);

//                 //adding parameter  event id
//                 dos.writeBytes("Content-Disposition: form-data; name=\"uploader\"" + lineEnd);
//                 //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
//                 //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
//                 dos.writeBytes(lineEnd);
//                 dos.writeBytes(listops.getString("username")); // mobile_no is String variable
//                 dos.writeBytes(lineEnd);

//                 dos.writeBytes(twoHyphens + boundary + lineEnd);
//                 //adding parameter  event id


//                 dos.writeBytes("Content-Disposition: form-data; name=\"media_file_esr\";filename=\"" + esr_image + "\"" + lineEnd);
//                 dos.writeBytes(lineEnd);


//                 // create a buffer of maximum size
//                 bytesAvailable = fileInputStream2.available();
//                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                 buffer = new byte[bufferSize];
//                 // read file and write it into form...
//                 bytesRead = fileInputStream2.read(buffer, 0, bufferSize);

//                 while (bytesRead > 0) {


//                     dos.write(buffer, 0, bufferSize);
//                     bytesAvailable = fileInputStream2.available();
//                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                     bytesRead = fileInputStream2.read(buffer, 0, bufferSize);


//                 }

//                 // send multipart form data necessary after file data...
//                 dos.writeBytes(lineEnd);
//                 dos.writeBytes(twoHyphens + boundary + lineEnd) ;
//                 fileInputStream2.close();;




//                     dos.writeBytes("Content-Disposition: form-data; name=\"media_file\";filename=\"" + sign_image + "\"" + lineEnd);
//                     dos.writeBytes(lineEnd);



//                     // create a buffer of maximum size
//                     bytesAvailable2 = fileInputStream.available();
//                     bufferSize2 = Math.min(bytesAvailable2, maxBufferSize);
//                     buffer2 = new byte[bufferSize2];
//                     // read file and write it into form...
//                     bytesRead2 = fileInputStream.read(buffer2, 0, bufferSize2);

//                     while (bytesRead2 > 0) {


//                         dos.write(buffer2, 0, bufferSize2);
//                         bytesAvailable2 = fileInputStream.available();
//                         bufferSize2 = Math.min(bytesAvailable2, maxBufferSize);
//                         bytesRead2 = fileInputStream.read(buffer2, 0, bufferSize2);


//                     }

//                     // send multipart form data necessary after file data...
//                     dos.writeBytes(lineEnd);
//                     dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);





//                 fileInputStream2.close();



//                     //conn.setRequestMethod("GET");
//                     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                     StringBuilder stringBuilder = new StringBuilder();


//                     while ((line = bufferedReader.readLine()) != null) {
//                         stringBuilder.append(line + '\n');
//                     }

//                     String jsonString = stringBuilder.toString();
//                     Log.e("jsonString", jsonString);
//                     JSONObject resJson = new JSONObject(jsonString);
//                     floatMessage = resJson.getString("status");
//                     Log.e("floatMessage", floatMessage);
//                     dos.flush();
//                     dos.close();



//             }
//             catch (Exception e) {
//                 Log.e("das",String.valueOf(e.getMessage()));
//             }
//             return floatMessage;
//         }




        protected void onPostExecute(String result) {
            dialog.dismiss();

            CommonUtilities.logMe( "SENDDATA post execute" );
            super.onPostExecute(result);
//            Log.e("afs",floatMessage);


            listops listops=new listops(getActivity());
            ArrayList<Schoolinfomodel> schoolinfomodelslist=listops.getdrilllist();


            MyToast.popmessage("Data sent to server",getActivity());
            schoolinfomodelslist.get(pos).setDrillstatus("Completed");
            listops.putdrilllist(schoolinfomodelslist);

                // hmm this is where we move to the other place
            ((GoToServiceCall)getActivity()).goToServiceCall(schoolinfomodelslist.get(pos).getSchoolid());

           // if(f.exists()) {
           //     f.delete();
           // }
           //  if(f2.exists()) {
           //      f2.delete();
           //  }


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

    public interface SetDrillsList
    {
        public void setupDrillList(int i);
    }

    public interface GoToServiceCall
    {
        public void goToServiceCall(String id);
    }


}
