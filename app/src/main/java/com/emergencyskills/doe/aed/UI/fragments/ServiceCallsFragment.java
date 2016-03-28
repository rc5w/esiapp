package com.emergencyskills.doe.aed.UI.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emergencyskills.doe.aed.R;
import com.emergencyskills.doe.aed.utils.CommonUtilities;
import com.emergencyskills.doe.aed.UI.activity.CaptureSignature;
import com.emergencyskills.doe.aed.utils.MyToast;
import com.emergencyskills.doe.aed.utils.Networkstate;
import com.emergencyskills.doe.aed.utils.listops;
import com.emergencyskills.doe.aed.models.Aedsdatamodel;
import com.emergencyskills.doe.aed.models.ExistingAedsmodel;
import com.emergencyskills.doe.aed.models.PendingUploadModel;
import com.emergencyskills.doe.aed.models.Schoolinfomodel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import java.util.List;

/**
 * Created by naveen on 11/18/2015.
 */


    public class ServiceCallsFragment extends Fragment implements View.OnClickListener{


    private TextView scaddress,scid,scprincipal,scprinemail,sccontact,sccontactemail,sccode,updateloc,phyloc;
    private String principal,contact,code,id,address,phone,serviceid,state,city,contactemail,contactphone,principalemail,principalphone;;
    String zip;
        private TextView save,saveforlater;
    private ImageView editaddress,editcontact,editprincipal;
    int pos,insposition,drillpostion;
    JSONObject drill;
    String sign_image,date,time,esrname,esr_image;
    String name,schoolname;
    LinearLayout linearLayout;
    private  EditText totalaeds;

int count;
    ArrayList<Schoolinfomodel> drilllist,servicelist,installist;
        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_service_calls, container, false);
           // Toast.makeText(getActivity(), "service calls", Toast.LENGTH_LONG).show();


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
                serviceid=getArguments().getString("servicecallid");
                pos=getArguments().getInt("pos");
                Log.e("pos",pos+"");


            }
            initview(rootView);
            listops listops=new listops(getActivity());
            drilllist=listops.getdrilllist();
            servicelist=listops.getservicelist();
            installist=listops.getinstalllist();
            Schoolinfomodel objdr=servicelist.get(pos);
            String id=objdr.getSchoolid();
            insposition=getposition(id, installist);
            drillpostion=getposition(id,drilllist);




            return rootView;
        }

    private void initview(View rootView) {

        totalaeds=(EditText)rootView.findViewById(R.id.sertotalnofaed);
        save=(TextView)rootView.findViewById(R.id.saveservicedata);
        save.setOnClickListener(this);
        saveforlater=(TextView)rootView.findViewById(R.id.saveservicedataforlater);
        saveforlater.setOnClickListener(this);




        //creating dyanmic layout for service
        listops listops=new listops(getActivity());
       ArrayList<Schoolinfomodel> schoolinfomodelArrayList=listops.getservicelist();
       Schoolinfomodel schoolinfomodel= schoolinfomodelArrayList.get(pos);
        ArrayList<ExistingAedsmodel> existingAedsmodelArrayList=schoolinfomodel.getExistingAedsmodelArrayList();

        count=existingAedsmodelArrayList.size();
        totalaeds.setText(""+count);
        Log.e("pos",pos+"  "+count);


        linearLayout=(LinearLayout)rootView.findViewById(R.id.linear);

        LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);

        //setting essentials for getting the data from dynamically created fragment for existing aeds
       ArrayList<Aedsdatamodel> datalist= listops.getaeddatalist();
       listops.clearaedadatalist(getActivity());

        ll.setId(23421413);
        for(int i=0;i<count;i++)
        {

            Bundle bundle=new Bundle();
            bundle.putInt("aednumber",i);
            bundle.putInt("aedorder",i);
            bundle.putString("type","servicecall");
            bundle.putInt("pos",pos);

            getFragmentManager().beginTransaction().add(ll.getId(), servicecalllayouts.instantiate(getActivity(),servicecalllayouts.class.getName(),bundle),""+i).commit();

        }



        linearLayout.addView(ll);




        scaddress=(TextView)rootView.findViewById(R.id.seradress);
        scprincipal=(TextView)rootView.findViewById(R.id.serprincipal);
        sccontact=(TextView)rootView.findViewById(R.id.sercontact);
        sccode=(TextView)rootView.findViewById(R.id.serschoolcode);



        //for editing


        editaddress=(ImageView)rootView.findViewById(R.id.imgseraddress);
        editcontact=(ImageView)rootView.findViewById(R.id.imgsercontact);
        editprincipal=(ImageView)rootView.findViewById(R.id.imgserprinciapl);
        //editschoolcode=(ImageView)rootView.findViewById(R.id.imgsercode);


        editaddress.setOnClickListener(this);
        editcontact.setOnClickListener(this);
        editprincipal.setOnClickListener(this);

        scprincipal.setText((principal+" "+principalemail));
        sccontact.setText((contact+" "+contactemail+" "+contactphone));
        sccode.setText(code);
        scaddress.setText(address+" "+city+" "+state+" "+zip);
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

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.saveservicedata)
        {
            //Toast.makeText(getActivity(),"Data Saved",Toast.LENGTH_SHORT).show();

            JSONArray servicedata=new JSONArray();
            Schoolinfomodel sc=servicelist.get(pos);


            listops listops=new listops(getActivity());
            ArrayList<Aedsdatamodel> datalist=listops.getaeddatalist();
            for(int i=0;i<datalist.size();i++)
            {

                Aedsdatamodel aedmodel=datalist.get(i);
                JSONObject jobj=new JSONObject();
                JSONObject adulpad1=new JSONObject();
                try{
                    adulpad1.put("expirationdate",aedmodel.getPadaexpdate());
                    adulpad1.put("lot",aedmodel.getPadalotnumber());
                    adulpad1.put("newdate",aedmodel.getPadanewdate());
                }
                catch(JSONException E)
                {
                    E.printStackTrace();
                }

                JSONObject adulpad2=new JSONObject();
                try{
                    adulpad2.put("expirationdate",aedmodel.getPadbexpdate());
                    adulpad2.put("lot",aedmodel.getPadblotnumber());
                    adulpad2.put("newdate",aedmodel.getPadbnewdate());
                }
                catch(JSONException E)
                {
                    E.printStackTrace();
                }
                JSONObject pedpad=new JSONObject();
                try{
                    pedpad.put("expirationdate",aedmodel.getPadpedexpdate());
                    pedpad.put("lot",aedmodel.getPadpedlotnumber());
                    pedpad.put("newdate",aedmodel.getPadpednewdate());
                }
                catch(JSONException E)
                {
                    E.printStackTrace();
                }

                try {
                    jobj.put("adultpadA",adulpad1);
                    jobj.put("adultpadB",adulpad2);
                    jobj.put("pediatric",pedpad);
                    jobj.put("has_frx_pediatric_key",aedmodel.isHasfrx()?"yes":"no");

                    jobj.put("spare_battery_before_date",aedmodel.getSparedate());
                    jobj.put("aedid",aedmodel.getAedid());
                    jobj.put("spare_battery_new_date",aedmodel.getNewsparedate());
                    jobj.put("PSAL_AED_out_with_coach",aedmodel.isOutWithCoach()?"yes":"no");
                    jobj.put("request_doe_send_pediatric_key",aedmodel.isRequestpediatrickey()?"yes":"no");
                    jobj.put("request_doe_send_fast_response_kit",aedmodel.isRequestdoesendfastresponsekit()?"yes":"no");
                    jobj.put("comments",aedmodel.getComments());
                    jobj.put("serial_number",aedmodel.getSerialnum());
                    jobj.put("datacardstatus",aedmodel.isDatacardstatus()?"yes":"no");
                    jobj.put("status_indicator",aedmodel.isStatusindicator()?"yes":"no");
                    jobj.put("unit_unavailable",aedmodel.isUnitunavailable()?"yes":"no");
                    jobj.put("physicallocation",aedmodel.getPhysicallocation());
                    jobj.put("error_with_unit",aedmodel.isErrorInUnit()?"yes":"no");
                    jobj.put("request_doe_send_spare_battery",aedmodel.isSendsparebattery()?"yes":"no");
                    jobj.put("error_info",aedmodel.getErrorinfo());

                    CommonUtilities.logMe( "hmm:" + aedmodel.isUnitunavailable() + ", " + aedmodel.isStatusindicator() + ", " + aedmodel.isErrorInUnit() + ", p:" + aedmodel.getPhysicallocation() );
                    if( ( !aedmodel.isUnitunavailable()  && !aedmodel.isStatusindicator() && !aedmodel.isErrorInUnit()  ) || "".equals( aedmodel.getPhysicallocation()) )
                    {
                        MyToast.popmessage("Status and physical location required for all AEDs",getActivity());
                        return ;
                    }
                        
                    servicedata.put(jobj);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            drill=new JSONObject();
            try{
                drill.put("id",sc.getSchoolid());
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
                drill.put("code", code);
                drill.put("servicecallid",sc.getServicecallid());
                drill.put("status","success");
                drill.put("servicedata",servicedata);

            }
            catch(JSONException e){
                e.printStackTrace();
            }

            Log.e("d",drill.toString());
           Intent intent=new Intent(getActivity(), CaptureSignature.class);
            intent.putExtra("sname",sc.getSchoolname());
            schoolname=sc.getSchoolname();
            startActivityForResult(intent,1);

        }

        if(v.getId()==R.id.saveservicedataforlater)
        {
            //Toast.makeText(getActivity(),"Data Saved",Toast.LENGTH_SHORT).show();

            Schoolinfomodel schoolinfomodel=servicelist.get(pos);

            ArrayList<ExistingAedsmodel> existingAedsmodelArrayList=schoolinfomodel.getExistingAedsmodelArrayList();
            
            
            
            listops listops=new listops(getActivity());
            ArrayList<Aedsdatamodel> datalist=listops.getaeddatalist();
            for(int i=0;i<datalist.size();i++)
            {

                ExistingAedsmodel existing = existingAedsmodelArrayList.get( i );
                Aedsdatamodel aedmodel=datalist.get(i);
//                    existing.setPadaexpiration(aedmodel.getPadaexpdate()); // don't need to do this because it can't change
                existing.setPedalot( aedmodel.getPadalotnumber());
                existing.setPadanewdate(aedmodel.getPadanewdate());

                existing.setPedblot(aedmodel.getPadblotnumber());
                existing.setPadbnewdate(aedmodel.getPadbnewdate());

                existing.setPedpadlot(aedmodel.getPadpedlotnumber());
                existing.setPedpadnewdate(aedmodel.getPadpednewdate());

                existing.setLocation(aedmodel.getPhysicallocation());

                existing.setHasfrx(aedmodel.isHasfrx()?"yes":"no");
                existing.setSparebattnewdate(aedmodel.getNewsparedate());
                existing.setOutwithcoach(aedmodel.isOutWithCoach()?"yes":"no");
                existing.setSendpedkey(aedmodel.isRequestpediatrickey()?"yes":"no");
                existing.setSendfastresponse(aedmodel.isRequestdoesendfastresponsekit()?"yes":"no");
                existing.setAedcomments(aedmodel.getComments());
//CommonUtilities.logMe( "setting comments..." + aedmodel.getComments() );
                existing.setNewserial(aedmodel.getSerialnum());
                existing.setDatacardstatus(aedmodel.isDatacardstatus()?"yes":"no");
                existing.setStatusindicator(aedmodel.isStatusindicator()?"yes":"no");
                existing.setUnitunavailable(aedmodel.isUnitunavailable()?"yes":"no");
                existing.setErrorwithunit(aedmodel.isErrorInUnit()?"yes":"no");
                existing.setRequestDoeSendSpareBattery(aedmodel.isSendsparebattery()?"yes":"no");
                existing.setErrorinfo(aedmodel.getErrorinfo());
//                existing.setInstallstatus( "Saved" );
                
                existingAedsmodelArrayList.set(i, existing);
                
            }
            schoolinfomodel.setExistingAedsmodelArrayList( existingAedsmodelArrayList );

            ArrayList<Schoolinfomodel> schoolinfomodelslist=listops.getservicelist();
            
            schoolinfomodelslist.set(pos, schoolinfomodel);
            listops.putservicelist(schoolinfomodelslist);
            
            ((setservicelist)getActivity()).setupservice(0);

            
        }


         if(v.getId()==R.id.imgseraddress)
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
            edzip.setText(zip);
            edstate.setText(state);
            edphn.setText(phone);
            TextView txt=(TextView) dialog.findViewById(R.id.editinfotittle);
            txt.setText("Address and Phone");
            update.setOnClickListener(new View.OnClickListener() {
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
                        Schoolinfomodel objdr=servicelist.get(pos);

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
                        if(drillpostion!=-1) {
                            Schoolinfomodel objser = drilllist.get(drillpostion);

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

        else if(v.getId()==R.id.imgsercontact)
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
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactphone=edphn.getText().toString();
                    contact=ednme.getText().toString();
                    contactemail=edemail.getText().toString();
                    if(!contactphone.isEmpty()&&!contact.isEmpty()&&!contactemail.isEmpty())
                    {
                        sccontact.setText(contact+" "+contactemail+" "+contactphone);
                        Schoolinfomodel objdr=servicelist.get(pos);

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
                        if(drillpostion!=-1) {
                            Schoolinfomodel objser = drilllist.get(drillpostion);

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
         else if(v.getId()==R.id.updateserviceaedlocation)
         {
             final Dialog dialog = new Dialog(getActivity());
             dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
             dialog.setContentView(R.layout.dialog_editinfo);
             dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(android.R.color.transparent)));
             Button update=(Button)dialog.findViewById(R.id.editupdate);

             final EditText ed=(EditText)dialog.findViewById(R.id.editfield);
             TextView txt=(TextView) dialog.findViewById(R.id.editinfotittle);
             ed.setText(phyloc.getText().toString());
             txt.setText("Physical Location");
             update.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     String ad=ed.getText().toString();
                     if(!ad.isEmpty())
                     {
                         phyloc.setText(ad);
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
         }
        else if(v.getId()==R.id.imgserprinciapl)
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
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    principal=ednme.getText().toString();
                    principalemail=edemail.getText().toString();
                    if(!principal.isEmpty()&&!principalemail.isEmpty())
                    {
                        scprincipal.setText(principal+" "+principalemail);
                        Schoolinfomodel objdr=servicelist.get(pos);

                        objdr.setPrincipal(principal);
                        objdr.setPrincemail(principalemail);


                        //changes in installlist
                        if(insposition!=-1) {
                            Schoolinfomodel objin = installist.get(insposition);

                            objin.setPrincipal(principal);
                            objin.setPrincemail(principalemail);
                        }

                        //changes in servicelist
                        if(drillpostion!=-1) {
                            Schoolinfomodel objser = drilllist.get(drillpostion);

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
    }




    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode) {
            case 1:
                if (resultCode == getActivity().RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    String status  = bundle.getString("status");
                    if(status.equalsIgnoreCase("done")){
                        String image=bundle.getString("sign");
                        date=bundle.getString("date");
                        time=bundle.getString("time");
                        sign_image=bundle.getString("path");
                        name=bundle.getString("name");
                        esr_image=bundle.getString("pathesr");
                        esrname=bundle.getString("nameesr");
                        schoolname=bundle.getString("sname");

                        listops listops=new listops(getActivity());
                        ArrayList<Schoolinfomodel> schoolinfomodelslist=listops.getservicelist();

                        Networkstate nwst=new Networkstate(getActivity());
                        if(nwst.haveNetworkConnection())
                        {
                            //Toast.makeText(getActivity(),"Have internet,send the data to server",Toast.LENGTH_SHORT).show();
                            new Senddata().execute();

                        }
                        else
                        {




                            //Schoolinfomodel schoolinfomodel=schoolinfomodelslist.get(pos);
                            //Schoolinfomodel schoolinfomodel=schoolinfomodelslist.get(pos);
                            schoolinfomodelslist.get(pos).setServicestatus("Pending");
                            schoolinfomodelslist.get(pos).setServicedata(drill.toString());
                            schoolinfomodelslist.get(pos).setServicesignimage(sign_image);
                            schoolinfomodelslist.get(pos).setServicesdate(date);
                            schoolinfomodelslist.get(pos).setServicetime(time);
                            schoolinfomodelslist.get(pos).setSchoolnamefromsign(schoolname);
                            schoolinfomodelslist.get(pos).setServicename(name);
                            schoolinfomodelslist.get(pos).setEsrnameser(esrname);
                            schoolinfomodelslist.get(pos).setEsrimageser(esr_image);

                            // schoolinfomodel.setDrilldata(drill.toString());

                            listops.putservicelist(schoolinfomodelslist);
                            addpendinglist(schoolinfomodelslist.get(pos).getSchoolcode(),schoolinfomodelslist.get(pos).getname(),schoolinfomodelslist.get(pos).getServicedata());

                            MyToast.popmessage("Added to Pending Uploads",getActivity());

                            ((setservicelist)getActivity()).setupservice(1);



                        }


                    }
                }
                break;
        }

    }
    private void addpendinglist(String schoolcode,String name,String data) {
        listops listops=new listops(getActivity());
        ArrayList<PendingUploadModel> pendinglist=listops.getpendinglist();
        if(pendinglist.size()>0)
        {

            int position=doeslisthavethisschool(schoolcode);
            if(position!=-1) {
                Log.e("pos",position+"");
                PendingUploadModel pend = pendinglist.get(position);
                pend.setCode(schoolcode);
                pend.setSchoolName(name);
                pend.setServicestatus("Pending");
                pend.setServicedata(data);
                pendinglist.set(position,pend);
            }
            else
            {
                PendingUploadModel pend = new PendingUploadModel();
                pend.setCode(schoolcode);
                pend.setSchoolName(name);
                pend.setServicestatus("Pending");
                pend.setServicedata(data);
                pendinglist.add(pend);
            }

        }
        else
        {
            PendingUploadModel pend=new PendingUploadModel();
            pend.setCode(schoolcode);
            pend.setSchoolName(name);
            pend.setServicestatus("Pending");
            pend.setServicedata(data);
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


    class Senddata extends AsyncTask<String, Integer, String> {

        ProgressDialog dialog;
        listops listops=new listops(getActivity());
        File f,f2;

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

            //create a file to write bitmap data

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {





                 f = new File(sign_image);

                FileInputStream fileInputStream = new FileInputStream(f);


                 f2 = new File(esr_image);

                FileInputStream fileInputStream2 = new FileInputStream(f2);


                URL url = new URL("http://doe.emergencyskills.com/api/api.php");



                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                //  conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("media_file", sign_image);
                conn.setRequestProperty("media_file_esr", esr_image);


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
                dos.writeBytes("uploadServiceCall"); // mobile_no is String variable
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
                dos.writeBytes(date); // mobile_no is String variable
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);

                //adding parameter  event id
                dos.writeBytes("Content-Disposition: form-data; name=\"time\"" + lineEnd);
                //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(time); // mobile_no is String variable
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);


                //adding parameter  event id
                dos.writeBytes("Content-Disposition: form-data; name=\"uploader\"" + lineEnd);
                //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(listops.getString("username")); // mobile_no is String variable
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
                dos.writeBytes("Content-Disposition: form-data; name=\"schoolname\"" + lineEnd);
                //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(schoolname); // mobile_no is String variable
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);

                //adding parameter  event id
                dos.writeBytes("Content-Disposition: form-data; name=\"esr_name\"" + lineEnd);
                //dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                //dos.writeBytes("Content-Length: " + name.length() + lineEnd);
                dos.writeBytes(lineEnd);

                dos.writeBytes(esrname); // mobile_no is String variable
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                //adding parameter  event id





                dos.writeBytes("Content-Disposition: form-data; name=\"media_file_esr\";filename=\"" + esr_image + "\"" + lineEnd);
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

                // send multipart form data necessary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd) ;
                fileInputStream2.close();;




                dos.writeBytes("Content-Disposition: form-data; name=\"media_file\";filename=\"" + sign_image + "\"" + lineEnd);
                dos.writeBytes(lineEnd);



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

                // send multipart form data necessary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);







                //conn.setRequestMethod("GET");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                fileInputStream.close();

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
            catch (Exception e) {
                Log.e("das",String.valueOf(e.getMessage()));
            }
            return floatMessage;
        }




        protected void onPostExecute(String result) {
            dialog.dismiss();

            super.onPostExecute(result);
//            Log.e("status",floatMessage);


            listops listops=new listops(getActivity());
            ArrayList<Schoolinfomodel> schoolinfomodelslist=listops.getservicelist();


            MyToast.popmessage("Data sent to server",getActivity());
            schoolinfomodelslist.get(pos).setServicestatus("Completed");
            listops.putservicelist(schoolinfomodelslist);

            ((setservicelist)getActivity()).setupservice(0);
            if(f.exists()) {
                f.delete();
            }
            if(f2.exists()) {
                f2.delete();
            }




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

public interface setservicelist
{
    public void setupservice(int i);
}



}

