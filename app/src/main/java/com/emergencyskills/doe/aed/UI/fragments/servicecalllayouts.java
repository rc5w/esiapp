package com.emergencyskills.doe.aed.UI.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.emergencyskills.doe.aed.R;
import com.emergencyskills.doe.aed.utils.CommonUtilities;
import com.emergencyskills.doe.aed.UI.activity.CaptureSignature;
import com.emergencyskills.doe.aed.utils.MonthYearPicker;
import com.emergencyskills.doe.aed.utils.MyToast;
import com.emergencyskills.doe.aed.utils.Networkstate;
import com.emergencyskills.doe.aed.utils.listops;
import com.emergencyskills.doe.aed.models.Aedsdatamodel;
import com.emergencyskills.doe.aed.models.ExistingAedsmodel;
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
 * Created by Karan on 1/14/2016.
 */
public class servicecalllayouts extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    View rootView;
    private TextView save,updateloc,phyloc,serialnumber;
    private ImageView editaddress,editcontact,editprincipal,editschoolcode;
    int pos;
    JSONObject drill;
    String sign_image;
    String name;
    private EditText pedalot,pedblot,pedpadlot,pedaexpdate,pedbexpdate,pedpadexpdate,sparedate,sercomments,editSerial;
    TextView updatepadadate,updatepadbdate,updatepadpeddate,updatesparebattery;
    private EditText errorinfo, newserial;
    Aedsdatamodel aedsdatamodel=new Aedsdatamodel();

    CheckBox statin,unitunavailable,error,frxkey,requestdoepedkey,requestdoesndfastrespkey,hasnewserial,datacard,sendbattery, outwithcoach;
    ArrayList<Aedsdatamodel> datalist;
    int aedorder;


    public void setColorRedIfExpired( EditText editobject )
    {
        try
        {
            
            String text = editobject.getText().toString();
//CommonUtilities.logMe( "text is: " + text );
            if( "null".equals( text) || "".equals( text) ) {
                editobject.setText( "" );
                return;
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date dateStr = formatter.parse(text);
            if( dateStr.before( new Date() ) )
                editobject.setTextColor(Color.RED);
        }
        catch( Exception exc )
        {
            exc.printStackTrace();
                // do nothing
        }
    }





    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.servicecall_layout,container,false);

        initView();



        if(getArguments()!=null)
        {
            //Save data essentials
            listops listops=new listops(getActivity());
            datalist=listops.getaeddatalist();

                // number is the position in the EXISTING list
            int aednumber=getArguments().getInt("aednumber");
                // order is the position in the displayed list (may be different for NIs)
            aedorder=getArguments().getInt("aedorder");
            pos=getArguments().getInt("pos");
            String type =getArguments().getString("type");


            ArrayList<Schoolinfomodel> schoolinfomodelArrayList=null;
            if( type.equals( "newinstall" ) )
                schoolinfomodelArrayList=listops.getinstalllist();
            else
                schoolinfomodelArrayList=listops.getservicelist();
                
            Schoolinfomodel schoolinfomodel= schoolinfomodelArrayList.get(pos);

            ArrayList<ExistingAedsmodel> existingAedsmodelArrayList=schoolinfomodel.getExistingAedsmodelArrayList();
            ExistingAedsmodel existingAedsmodel=existingAedsmodelArrayList.get(aednumber);

            serialnumber.setText("SERIAL #: "+getNull(existingAedsmodel.getSerialnum()));
            aedsdatamodel.setSerialnum(existingAedsmodel.getSerialnum());


                // new stuff
            updatepadadate.setText(existingAedsmodel.getPadanewdate());
            aedsdatamodel.setPadanewdate(existingAedsmodel.getPadanewdate());
            
            updatepadbdate.setText(existingAedsmodel.getPadbnewdate());
            aedsdatamodel.setPadbnewdate(existingAedsmodel.getPadbnewdate());
            
            updatepadpeddate.setText(existingAedsmodel.getPedpadnewdate());
            aedsdatamodel.setPadpednewdate(existingAedsmodel.getPedpadnewdate());
            
            updatesparebattery.setText(existingAedsmodel.getSparebattnewdate());
            aedsdatamodel.setNewsparedate(existingAedsmodel.getSparebattnewdate());
            

            statin.setChecked( "yes".equals( existingAedsmodel.getStatusindicator() )?true:false );
CommonUtilities.logMe( "during loading status is..." + existingAedsmodel.getStatusindicator() );
            
            aedsdatamodel.setStatusindicator( "yes".equals( existingAedsmodel.getStatusindicator() )?true:false );
            unitunavailable.setChecked( "yes".equals( existingAedsmodel.getUnitunavailable() )?true:false );
            aedsdatamodel.setUnitunavailable( "yes".equals( existingAedsmodel.getUnitunavailable() )?true:false );

            outwithcoach.setChecked( "yes".equals( existingAedsmodel.getOutwithcoach() )?true:false );
            aedsdatamodel.setIsOutWithCoach( "yes".equals( existingAedsmodel.getOutwithcoach() )?true:false );

            error.setChecked( "yes".equals( existingAedsmodel.getErrorwithunit() )?true:false );
            aedsdatamodel.setErrorinunit( "yes".equals( existingAedsmodel.getErrorwithunit() )?true:false );
            editSerial.setText( existingAedsmodel.getNewserial() );
            aedsdatamodel.setSerialnum( existingAedsmodel.getNewserial() );
            frxkey.setChecked( "yes".equals( existingAedsmodel.getHasfrx() )?true:false );
            aedsdatamodel.setHasfrx( "yes".equals( existingAedsmodel.getHasfrx() )?true:false );
            requestdoepedkey.setChecked( "yes".equals( existingAedsmodel.getSendpedkey() )?true:false );
            aedsdatamodel.setRequestpediatrickey( "yes".equals( existingAedsmodel.getSendpedkey() )?true:false );
            requestdoesndfastrespkey.setChecked( "yes".equals( existingAedsmodel.getSendfastresponse() )?true:false );
            aedsdatamodel.setRequestdoesendfastresponsekit( "yes".equals( existingAedsmodel.getSendfastresponse() )?true:false );
            datacard.setChecked( "yes".equals( existingAedsmodel.getDatacardstatus() )?true:false );
            aedsdatamodel.setDatacardstatus( "yes".equals( existingAedsmodel.getDatacardstatus() )?true:false );

            hasnewserial.setChecked( "yes".equals( existingAedsmodel.isHasnewserialnumber() )?true:false );
            aedsdatamodel.setHasnewserialnumber( "yes".equals( existingAedsmodel.isHasnewserialnumber() )?true:false );


            sendbattery.setChecked( "yes".equals( existingAedsmodel.getRequestDoeSendSpareBattery() )?true:false );
            aedsdatamodel.setSendsparebattery( "yes".equals( existingAedsmodel.getRequestDoeSendSpareBattery() )?true:false );

            aedsdatamodel.setErrorinfo( existingAedsmodel.getErrorinfo() );
            

        // end new stuff

            pedalot.setText(getNull(existingAedsmodel.getPedalot()));
            aedsdatamodel.setPadalotnumber(existingAedsmodel.getPedalot());

            pedblot.setText(getNull(existingAedsmodel.getPedblot()));
            aedsdatamodel.setPadblotnumber(existingAedsmodel.getPedblot());

            pedpadlot.setText(getNull(existingAedsmodel.getPedpadlot()));
            aedsdatamodel.setPadpedlotnumber(existingAedsmodel.getPedpadlot());

            if(existingAedsmodel.getPediatricpads().equals("0000-00-00"))
            {
                pedpadexpdate.setText("");
            }
            else
            {
                pedpadexpdate.setText(getNull(existingAedsmodel.getPediatricpads()));
                setColorRedIfExpired( pedpadexpdate );
            }
            aedsdatamodel.setPadpedexpdate(existingAedsmodel.getPediatricpads());

            if(existingAedsmodel.getPadaexpiration().equals("0000-00-00"))
            {
                pedaexpdate.setText("");
            }
            else
            {
                pedaexpdate.setText(getNull(existingAedsmodel.getPadaexpiration()));
                setColorRedIfExpired( pedaexpdate );
            }
            
            aedsdatamodel.setPadaexpdate(existingAedsmodel.getPadaexpiration());

            if(existingAedsmodel.getPadbexpiration().equals("0000-00-00"))
            {
                pedbexpdate.setText("");
            }
            else
            {
                pedbexpdate.setText(getNull(existingAedsmodel.getPadbexpiration()));
            }
            aedsdatamodel.setPadbexpdate(existingAedsmodel.getPadbexpiration());
            setColorRedIfExpired( pedbexpdate );

            phyloc.setText(existingAedsmodel.getLocation());
            aedsdatamodel.setPhysicallocation(existingAedsmodel.getLocation());

            if(existingAedsmodel.getSparedate().equals("0000-00-00"))
            {
                sparedate.setText("");
            }
            else
            {
                sparedate.setText(getNull( existingAedsmodel.getSparedate() ));
                setColorRedIfExpired( sparedate );
            }
            aedsdatamodel.setSparedate(existingAedsmodel.getSparedate());

            aedsdatamodel.setAedid(existingAedsmodel.getAedid());




            aedsdatamodel.setComments(existingAedsmodel.getAedcomments());



//            CommonUtilities.logMe( "adding aed number: " + aednumber );
            aedsdatamodel.setNo(aednumber+"");

            datalist.add(aedsdatamodel);
            listops.putaeddatalist(datalist);
            sercomments.setText(getNull( existingAedsmodel.getAedcomments() ));



        }

        return rootView;
    }
    private static boolean checkElementsThere( ArrayList<Aedsdatamodel> datalist, int aedorder )
    {
        if( datalist.size() < aedorder+1 )
        {
//CommonUtilities.logMe( "no data list yet size:" + datalist.size() + ", looking for:" + aedorder );
            return false;
        }
        else
        {
//CommonUtilities.logMe( "we're ok size:" + datalist.size() + ", looking for:" + aedorder );
            return true;
        }
        
    }
    
    private void initView() {

        phyloc=(TextView) rootView.findViewById(R.id.servicephyloc);

        editSerial=(EditText) rootView.findViewById(R.id.serednewserial);
        editSerial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                listops listops=new listops(getActivity());
                datalist=listops.getaeddatalist();
                if (!checkElementsThere( datalist, aedorder ) )
                    return;

                Aedsdatamodel obje=datalist.get(aedorder);
                obje.setSerialnum(s.toString());
                listops.putaeddatalist(datalist);
            }



            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sercomments=(EditText) rootView.findViewById(R.id.sercomments);
        sercomments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                listops listops=new listops(getActivity());
                datalist=listops.getaeddatalist();
                if (!checkElementsThere( datalist, aedorder ) )
                    return;

                Aedsdatamodel obje=datalist.get(aedorder);
                obje.setComments(s.toString());
                listops.putaeddatalist(datalist);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

                updateloc=(TextView) rootView.findViewById(R.id.updateserviceaedlocation);
        updateloc.setOnClickListener(this);

       // save=(TextView)rootView.findViewById(R.id.saveservicedata);
       // save.setOnClickListener(this);
        serialnumber=(TextView)rootView.findViewById(R.id.seraedserialnumber);


        pedalot=(EditText)rootView.findViewById(R.id.serpad1lot);
        pedalot.addTextChangedListener(new TextWatcher() {
            @Override       public void beforeTextChanged(CharSequence s, int start, int count, int after) {        }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                listops listops=new listops(getActivity());
                datalist=listops.getaeddatalist();

                if (!checkElementsThere( datalist, aedorder ) )
                    return;

                Aedsdatamodel obje=datalist.get(aedorder);
                obje.setPadalotnumber(s.toString());
                listops.putaeddatalist(datalist);
            }

            @Override        public void afterTextChanged(Editable s) {    }
        });
        
        sparedate=(EditText)rootView.findViewById(R.id.sparebattery);



        pedblot=(EditText)rootView.findViewById(R.id.serpad2lot);
        pedblot.addTextChangedListener(new TextWatcher() {
            @Override       public void beforeTextChanged(CharSequence s, int start, int count, int after) {        }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                listops listops=new listops(getActivity());
                datalist=listops.getaeddatalist();
                if (!checkElementsThere( datalist, aedorder ) )
                    return;

                Aedsdatamodel obje=datalist.get(aedorder);
                obje.setPadblotnumber(s.toString());
                listops.putaeddatalist(datalist);
            }

            @Override        public void afterTextChanged(Editable s) {    }
        });

        pedpadlot=(EditText)rootView.findViewById(R.id.serpadpedlot);
        pedpadlot.addTextChangedListener(new TextWatcher() {
            @Override       public void beforeTextChanged(CharSequence s, int start, int count, int after) {        }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                listops listops=new listops(getActivity());
                datalist=listops.getaeddatalist();
                if (!checkElementsThere( datalist, aedorder ) )
                    return;
                
                Aedsdatamodel obje=datalist.get(aedorder);
                obje.setPadpedlotnumber(s.toString());
                listops.putaeddatalist(datalist);
            }

            @Override        public void afterTextChanged(Editable s) {    }
        });
        pedaexpdate=(EditText)rootView.findViewById(R.id.serpad1expdate);
        pedbexpdate=(EditText)rootView.findViewById(R.id.serpad2expdate);
        pedpadexpdate=(EditText)rootView.findViewById(R.id.serpadpedexpdate);



        updatepadadate=(TextView) rootView.findViewById(R.id.updatepad1newdate);
        updatepadbdate=(TextView) rootView.findViewById(R.id.updatepad2newdate);
        updatepadpeddate=(TextView) rootView.findViewById(R.id.updatepadpednewdate);
        updatesparebattery=(TextView) rootView.findViewById(R.id.updatesparebatterynewdate);

        updatepadadate.setOnClickListener(this);
        updatepadbdate.setOnClickListener(this);
        updatepadpeddate.setOnClickListener(this);

        updatesparebattery.setOnClickListener(this);

//    private EditText statusindicator, errorinfo, requestsparebatt, errorwithunit, hasfrx, outwithcoach, sendpedkey, sendfastresponse, newserial, fastresponsekit, datacardstatus, unitunavailable;


        statin=(CheckBox)rootView.findViewById(R.id.serstatusindicator);
        unitunavailable=(CheckBox)rootView.findViewById(R.id.serunitisunavailable);
        error=(CheckBox)rootView.findViewById(R.id.sererrorwithunit);

        error.setOnCheckedChangeListener(this);
        unitunavailable.setOnCheckedChangeListener(this);
        statin.setOnCheckedChangeListener(this);

        editSerial=(EditText)rootView.findViewById(R.id.serednewserial);

        frxkey=(CheckBox)rootView.findViewById(R.id.serfrxpedkey);
        frxkey.setOnCheckedChangeListener(this);

        outwithcoach=(CheckBox)rootView.findViewById(R.id.seroutwithcoach);
        outwithcoach.setOnCheckedChangeListener(this);

        requestdoepedkey=(CheckBox)rootView.findViewById(R.id.serrequestdoepedkey);
        requestdoepedkey.setOnCheckedChangeListener(this);

        requestdoesndfastrespkey=(CheckBox)rootView.findViewById(R.id.serrequestdoesendfastresponse);
        requestdoesndfastrespkey.setOnCheckedChangeListener(this);

        hasnewserial=(CheckBox)rootView.findViewById(R.id.serhasnewserial);
        hasnewserial.setOnCheckedChangeListener(this);

        datacard=(CheckBox)rootView.findViewById(R.id.serdatacardstat);
        datacard.setOnCheckedChangeListener(this);

        sendbattery=(CheckBox)rootView.findViewById(R.id.sersenddsparebattery);
        sendbattery.setOnCheckedChangeListener(this);





    }

    @Override
    public void onClick(View v) {





        if(v.getId()==R.id.updatesparebatterynewdate)
        {

            final MonthYearPicker myp=new MonthYearPicker(getActivity());
            myp.build(new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {





                    String monthname=myp.getSelectedMonthName();
                    String mon;

                    int mont=monthnum(monthname);
                    if(mont<10)
                    {
                        mon="0"+mont;
                    }
                    else
                    {
                        mon=String.valueOf(mont);
                    }
                    String year=String.valueOf(myp.getSelectedYear());


                    updatesparebattery.setText(year+"-"+mon+"-01");


                    listops listops=new listops(getActivity());
                    datalist=listops.getaeddatalist();
                    
                    Aedsdatamodel obje=datalist.get(aedorder);
                    obje.setNewsparedate(updatesparebattery.getText().toString());
                    listops.putaeddatalist(datalist);
                    Log.e("fas",obje.getNewsparedate());



                }
            }, null);
            myp.show();
        }



       else  if(v.getId()==R.id.updateserviceaedlocation)
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


                        listops listops=new listops(getActivity());
                        datalist=listops.getaeddatalist();
                        Aedsdatamodel obje=datalist.get(aedorder);
                        obje.setPhysicallocation(ad);
                        listops.putaeddatalist(datalist);
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

       else if(v.getId()==R.id.updatepadpednewdate)
        {
          final MonthYearPicker myp=new MonthYearPicker(getActivity());
            myp.build(new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String monthname=myp.getSelectedMonthName();
                    String mon;

                    int mont=monthnum(monthname);
                    if(mont<10)
                    {
                        mon="0"+mont;
                    }
                    else
                    {
                        mon=String.valueOf(mont);
                    }
                    String year=String.valueOf(myp.getSelectedYear());
                    listops listops=new listops(getActivity());
                    datalist=listops.getaeddatalist();
                    updatepadpeddate.setText(year+"-"+mon+"-01");
                    Aedsdatamodel obje=datalist.get(aedorder);
                    obje.setPadpednewdate(updatepadpeddate.getText().toString());

                    listops.putaeddatalist(datalist);



                }
            }, null);
            myp.show();
        }
       else if(v.getId()==R.id.updatepad1newdate)
        {
          final MonthYearPicker myp=new MonthYearPicker(getActivity());
            myp.build(new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String monthname=myp.getSelectedMonthName();
                    String mon;

                    int mont=monthnum(monthname);
                    if(mont<10)
                    {
                        mon="0"+mont;
                    }
                    else
                    {
                        mon=String.valueOf(mont);
                    }
                    String year=String.valueOf(myp.getSelectedYear());
                    listops listops=new listops(getActivity());
                    datalist=listops.getaeddatalist();
                    updatepadadate.setText(year+"-"+mon+"-01");
                    Aedsdatamodel obje=datalist.get(aedorder);
                    obje.setPadanewdate(updatepadadate.getText().toString());


                    listops.putaeddatalist(datalist);



                }
            }, null);
            myp.show();
        }
        else if(v.getId()==R.id.updatepad2newdate)
        {
            final MonthYearPicker myp=new MonthYearPicker(getActivity());
            myp.build(new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {





                    String monthname=myp.getSelectedMonthName();
                    String mon;

                    int mont=monthnum(monthname);
                    if(mont<10)
                    {
                        mon="0"+mont;
                    }
                    else
                    {
                        mon=String.valueOf(mont);
                    }
                    String year=String.valueOf(myp.getSelectedYear());
                    updatepadbdate.setText(year+"-"+mon+"-01");

                    listops listops=new listops(getActivity());
                    datalist=listops.getaeddatalist();
                    Aedsdatamodel obje=datalist.get(aedorder);
                    obje.setPadbnewdate(updatepadbdate.getText().toString());
                    listops.putaeddatalist(datalist);

                }
            }, null);
            myp.show();
        }





    }



    private int monthnum(String mont) {
        // TODO Auto-generated method stub
        String[] months={"January","Febuary","March","April","May","June","July","August","September","October","November","December"};
        int index=0;
        for(int i=0;i<months.length;i++)
        {
            if(mont.equals(months[i]))
                index=i;

        }
        return index+1;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        listops listops=new listops(getActivity());
        datalist= listops.getaeddatalist();
        if (!checkElementsThere( datalist, aedorder ) )
            return;
        
        Aedsdatamodel obj=datalist.get(aedorder);

        if(buttonView.getId()==R.id.serstatusindicator)
        {
                obj.setStatusindicator(buttonView.isChecked());
                if( buttonView.isChecked() )
                {
                    obj.setErrorinfo("");
                    obj.setErrorinunit(false);
                    obj.setUnitunavailable(false);
//        statin
                    unitunavailable.setChecked( false );
                    error.setChecked( false );
                }
        }
        else if(buttonView.getId()==R.id.serunitisunavailable)
        {
CommonUtilities.logMe( "ok in unavailable" );
            if(buttonView.isChecked())
            {
CommonUtilities.logMe( "ok setting it to unavailable" );
                obj.setUnitunavailable(true);

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_unit_is_mssing);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(android.R.color.transparent)));



                dialog.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();

                statin.setChecked( false );
                error.setChecked( false );
                obj.setStatusindicator( false );
                obj.setErrorinunit( false );
                obj.setErrorinfo("");

            }
            else
            {
                obj.setUnitunavailable(false);
            }
//        statin
            
        }
        else if(buttonView.getId()==R.id.sererrorwithunit)
        {
            if(buttonView.isChecked())
            {
                obj.setErrorinunit(true);
                unitunavailable.setChecked( false );
                obj.setUnitunavailable( false );
                obj.setStatusindicator( false );
                statin.setChecked( false );
                final Dialog dialog = new Dialog(getActivity());

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_error_in_the_unit);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(android.R.color.transparent)));

                final EditText errorinfo=(EditText)dialog.findViewById(R.id.errorinfo);
                errorinfo.setText(obj.getErrorinfo());
                dialog.findViewById(R.id.submit_error).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        if(!errorinfo.getText().toString().isEmpty())
                        {
                            listops listops=new listops(getActivity());
                            datalist=listops.getaeddatalist();
                            Aedsdatamodel obje=datalist.get(aedorder);
                            obje.setErrorinfo(errorinfo.getText().toString());
                            listops.putaeddatalist(datalist);
                        }
                        else
                        {
                            MyToast.popmessage("Please enter error",getActivity());
                        }
                        dialog.cancel();
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
            else
            {
                obj.setErrorinunit(false);
                obj.setErrorinfo("");
            }

        }
        else if(buttonView.getId()==R.id.serfrxpedkey)
        {
            if(buttonView.isChecked())
            {
                obj.setHasfrx(true);
            }
            else
            {
                obj.setHasfrx(false);
            }
        }
        else if(buttonView.getId()==R.id.serrequestdoepedkey)
        {
            if(buttonView.isChecked())
            {
                obj.setRequestpediatrickey(true);
            }
            else
            {
                obj.setRequestpediatrickey(false);
            }
        }
        else if(buttonView.getId()==R.id.seroutwithcoach)
        {
            if(buttonView.isChecked())
            {
                obj.setIsOutWithCoach(true);
            }
            else
            {
                obj.setIsOutWithCoach(false);
            }
        }
        else if(buttonView.getId()==R.id.serrequestdoesendfastresponse)
        {
            if(buttonView.isChecked())
            {
                obj.setRequestdoesendfastresponsekit(true);
            }
            else
            {
                obj.setRequestdoesendfastresponsekit(false);
            }
        }
        else if(buttonView.getId()==R.id.serhasnewserial)
        {
            if(buttonView.isChecked())
            {
                obj.setHasnewserialnumber(true);
            }
            else
            {
                obj.setHasnewserialnumber(false);
            }
        }
        else if(buttonView.getId()==R.id.serdatacardstat)
        {
            if(buttonView.isChecked())
            {
                obj.setDatacardstatus(true);
            }
            else
            {
                obj.setDatacardstatus(false);
            }
        }

        else if(buttonView.getId()==R.id.sersenddsparebattery)
        {
            if(buttonView.isChecked())
            {
                obj.setSendsparebattery(true);
            }
            else
            {
                obj.setSendsparebattery(false);
            }
        }

        datalist.set( aedorder, obj );
        listops.putaeddatalist(datalist);
        
        Aedsdatamodel obj2=datalist.get(aedorder);


    }

    class updateui extends AsyncTask<String ,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public String getNull( String value )
    {
        if( "null".equals( value ) || "".equals( value ) || value == null ) return "";
        else
            return value;
    }
    public boolean isNull( String value )
    {
        if( "null".equals( value ) || "".equals( value ) || value == null ) return true;
        return false;
    }
}
