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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.emergencyskills.doe.aed.utils.CommonUtilities;
import com.emergencyskills.doe.aed.R;
import com.emergencyskills.doe.aed.utils.Constants;
import com.emergencyskills.doe.aed.utils.MyToast;
import com.emergencyskills.doe.aed.utils.Networkstate;
import com.emergencyskills.doe.aed.utils.WebServiceHandler;
import com.emergencyskills.doe.aed.utils.listops;
import com.emergencyskills.doe.aed.adapters.PickSchoolsAdapter;
import com.emergencyskills.doe.aed.models.ExistingAedsmodel;
import com.emergencyskills.doe.aed.models.OtherSchoolsinCampus;
import com.emergencyskills.doe.aed.models.PickSchoolModel;
import com.emergencyskills.doe.aed.models.Schoolinfomodel;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveen on 11/18/2015.
 */
public  class PickSchoolsFragment extends Fragment {


    private ListView lvPickSchool;
    private CheckBox cbx;
    private TextView btndownload;
   private ArrayList<String> selectedschool,schoolnames,selectedid;
   private ArrayList<Schoolinfomodel> schoolinfomodelsarraylist,drilllist,servicelist,installlist;
    String items[] = null;
    int firstinstance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pick_schools, container, false);
        lvPickSchool = (ListView) rootView.findViewById(R.id.lvPickSchool);
//        lvPickSchool.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        listops listops=new listops(getActivity());
      
        schoolinfomodelsarraylist=listops.getschoollist("downloadedlist");
        drilllist=listops.getdrilllist();
        servicelist=listops.getservicelist();
        installlist=listops.getinstalllist();
        btndownload=(TextView)rootView.findViewById(R.id.btndownnloaddata);

        Networkstate nest=new Networkstate(getActivity());

        if(nest.haveNetworkConnection())
        {
            try {
                lvPickSchool.setAdapter(new PickSchoolsAdapter(getContext(), listops.getpickschoollist()));
                btndownload.setVisibility(View.VISIBLE);
            }
            catch (NullPointerException e)
            {
                //do nothing
                btndownload.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            MyToast.popmessage("No Internet Connection",getActivity());
        }

        ListAdapter adapter = lvPickSchool.getAdapter();
        selectedschool=new ArrayList<String>();
        schoolnames=new ArrayList<String>();
        selectedid=new ArrayList<String>();

        if (adapter != null) {

            try {
                
                ViewGroup vg = lvPickSchool;
                int totalHeight = 0;
                for (int i = 0; i < adapter.getCount(); i++) {
                    View listItem = adapter.getView(i, null, vg);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
                
                ViewGroup.LayoutParams par = lvPickSchool.getLayoutParams();
                par.height = totalHeight + (lvPickSchool.getDividerHeight() * (adapter.getCount() - 1));
                lvPickSchool.setLayoutParams(par);
                lvPickSchool.requestLayout();
                
                
            }
            catch (NullPointerException E)
            {
                
            }
        }

        //getting the values

        lvPickSchool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.cbVisitingStatus);
                TextView code=(TextView)view.findViewById(R.id.tvSchoolCodeValue);
                PickSchoolModel p=(PickSchoolModel) lvPickSchool.getAdapter().getItem(position);
                TextView tvSchoolNameValue= (TextView) view.findViewById(R.id.tvSchoolNameValue);
                String codevalue=p.getSchoolID();
                String name=tvSchoolNameValue.getText().toString();
                cb.setChecked(!cb.isChecked());
                if(cb.isChecked())
                {

                    if(!selectedschool.contains(codevalue))
                    {
                        selectedschool.add(codevalue);
                        schoolnames.add(name);
                        
                    }
                }
                else
                {
                    if(selectedschool.contains(codevalue))
                    {
                        selectedschool.remove(codevalue);
                        schoolnames.remove(name);
                    }
                }
            }
        });

        btndownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              
                if(selectedschool.size()>0) {

                    new DownloadDataTask().execute();
                }
                else
                    Toast.makeText(getActivity(),"Please select",Toast.LENGTH_SHORT).show();
            }


    });


//        CommonUtilities.logMe( "hmmmmm"  + listops.getZipList().size() );
        Spinner dropdown = (Spinner)rootView.findViewById(R.id.zipspinner);

        List<String> ziplist = listops.getZipList();
        items = ziplist.toArray(new String[ziplist.size()] );
        ArrayAdapter<String> listadapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(listadapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            // your code here
                        String selected = items[position];
                        ListAdapter adapter = lvPickSchool.getAdapter();
                        firstinstance = 0;
                        ViewGroup vg = lvPickSchool;
                        if( adapter != null )
                        {
                            for (int i = 0; i < adapter.getCount(); i++) {
                                View listItem = adapter.getView(i, null, vg);
                                TextView tvSchoolNameValue= (TextView) listItem.findViewById(R.id.tvSchoolZipValue);
                                    // CommonUtilities.logMe( "found it?" + tvSchoolNameValue.getText() + "?" + selected );
                                    // CommonUtilities.logMe( "index is: " + tvSchoolNameValue.getText().toString().indexOf( selected ) );
                                if( tvSchoolNameValue.getText().toString().indexOf( selected ) != -1 )
                                {
//                                    CommonUtilities.logMe( "found on!" ) ;
                                    firstinstance = i;
                                    break; 
                                }
                            }
                        }
                        CommonUtilities.logMe( "first is: " +firstinstance );
                        lvPickSchool.setSelection(firstinstance);
                        // lvPickSchool.clearFocus();
                        // lvPickSchool.requestFocusFromTouch();
                        // lvPickSchool.postDelayed(new Runnable() {
                        //             @Override
                        //             public void run() {
                        //             lvPickSchool.setSelection(firstinstance);
                        //             }
                        //     }, 500);

//                         lvPickSchool.post(new Runnable() {
//                                 @Override
//                                 public void run() {
//                                     lvPickSchool.smoothScrollToPosition( firstinstance );
//                                     lvPickSchool.setSelection( firstinstance );
// //                                        lvPickSchool.requestFocus();                        
//                                 }
//                             });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                    }

            });
        


        return rootView;
    }

    private class DownloadDataTask extends AsyncTask<Void, String, Void> {

        private WebServiceHandler wshObj = new WebServiceHandler();
        String result = "";
        private ProgressDialog dialog;
        String status="";


        //TinyDb tinyDb=new TinyDb(getActivity());


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Downloading data for "+schoolnames.get(0));
            dialog.setIndeterminate(false);


            dialog.show();


            Log.e("in pre", "");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // call a web service to check user is register or not

        for(int i=0;i<selectedschool.size();i++) {

            String schoolid=selectedschool.get(i);
            CommonUtilities.logMe(" starting a school: " + schoolid );

            Log.e("in back", "");
            try {
                WebServiceHandler wsb = new WebServiceHandler();
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("apikey", "daMEs26rufAqasw2pUYU"));
                postParameters.add(new BasicNameValuePair("method", "downloadData"));
                postParameters.add(new BasicNameValuePair("schoolid", schoolid));
                CommonUtilities.logMe(" starting data download: " + schoolid );
                result = wsb.getWebServiceData("http://doe.emergencyskills.com/api/api.php", postParameters);
                CommonUtilities.logMe(" after data download: " + schoolid );
                    //dialog.setMessage("Downloading data for "+schoolnames.get(i)+" ");

                Log.e("result", result);
                try {
                    publishProgress(schoolnames.get(i+1));
                }
                catch (Exception e)
                {

                }


                JSONObject jsonObject = new JSONObject(result);
                status = jsonObject.getString("status");



                    Schoolinfomodel Schoolifomodel = new Schoolinfomodel();

                    String principal = jsonObject.getString("principalname");
                    String prinemail = jsonObject.getString("principalemail");
                    String Schoolphone = jsonObject.getString("schoolphone");
                    String Schoolcode = jsonObject.getString("schoolcode");
                    String contactname = jsonObject.getString("contactname");
                    String contactemail = jsonObject.getString("contactemail");

                String contactphone = jsonObject.getString("contactphone");
                String city=jsonObject.getString("city");
                String state=jsonObject.getString("state");
                String zip=jsonObject.getString("zip");
                String addrees=jsonObject.getString("address");
                    Schoolifomodel.setSchoolid(selectedschool.get(i));

                Schoolifomodel.setPrincipal(principal);
                Schoolifomodel.setname(schoolnames.get(i));
                Schoolifomodel.setPrincemail(prinemail);
                Schoolifomodel.setSize(selectedschool.size());
                Schoolifomodel.setPhone(Schoolphone);
                Schoolifomodel.setSchoolcode(Schoolcode);
                Schoolifomodel.setAedcontact(contactname);
                Schoolifomodel.setAedcontactemail(contactemail);
                Schoolifomodel.setDrillstatus("d");
                Schoolifomodel.setInstallstatus("d");
                Schoolifomodel.setServicestatus("d");
                Schoolifomodel.setAddress(addrees);
                Schoolifomodel.setZip(zip);
                Schoolifomodel.setCity(city);
                Schoolifomodel.setState(state);
                Schoolifomodel.setContactphone(contactphone);
                Schoolifomodel.setSchoolname(jsonObject.getString("companyname"));
                CommonUtilities.logMe( "company name is : " + Schoolifomodel.getSchoolname() );

                //getting the drills,service and newinstallid
                JSONObject drill=jsonObject.getJSONObject("drill");
                JSONObject servicecall=jsonObject.getJSONObject("servicecall");
                JSONObject newinstall=jsonObject.getJSONObject("newinstall");

                JSONArray existingaeds=jsonObject.getJSONArray("existingaeds");
                try {
                    ArrayList<OtherSchoolsinCampus> otherSchoolsinCampuseslist = new ArrayList<>();
                    JSONArray otherschoolsincampus = jsonObject.getJSONArray("otherschools_in_campus");

                    for (int pass = 0; pass < otherschoolsincampus.length(); pass++) {
                        JSONObject sc = otherschoolsincampus.getJSONObject(pass);
                        OtherSchoolsinCampus other = new OtherSchoolsinCampus();
                        other.setCompanyname(sc.getString("companyname"));
                        other.setId(sc.getString("id"));
                        otherSchoolsinCampuseslist.add(other);
                    }
                    Schoolifomodel.setOtherSchoolsinCampuses(otherSchoolsinCampuseslist);
                }
                catch (JSONException e)
                {
//                    e.printStackTrace();
                    ArrayList<OtherSchoolsinCampus> otherSchoolsinCampuseslist = new ArrayList<>();
                    Schoolifomodel.setOtherSchoolsinCampuses(otherSchoolsinCampuseslist);

                }


                ArrayList<ExistingAedsmodel> existingAedsmodelArrayList=new ArrayList<>();

                for(int count=0;count<existingaeds.length();count++)
                {
                    try {
                        JSONObject obj = existingaeds.getJSONObject(count);
                        ExistingAedsmodel existingAedsmodel = new ExistingAedsmodel();
                        existingAedsmodel.setAedid(obj.getString("aedid"));
                        CommonUtilities.logMe( "adding a serial: " + count + ". " + obj.getString("serial") + " (aedid: " + obj.getString("aedid") + ")" );
                        existingAedsmodel.setSerialnum(obj.getString("serial"));
                        existingAedsmodel.setLocation(obj.getString("location"));
                        existingAedsmodel.setPadaexpiration(obj.getString("padaexpiration"));
                        existingAedsmodel.setPadbexpiration(obj.getString("padbexpiration"));
                        existingAedsmodel.setBatterydate(obj.getString("batterydate"));
                        existingAedsmodel.setSparedate(obj.getString("sparedate"));
                        existingAedsmodel.setFillingexpirationdate(obj.getString("filingexpirationdate"));
                        existingAedsmodel.setDate(obj.getString("date"));
                        existingAedsmodel.setPediatricpads(obj.getString("pediatricpads"));
                        existingAedsmodel.setLotnumber(obj.getString("lotnumber"));
                        existingAedsmodel.setHasbeenupdated(obj.getString("hasbeenupdated"));
                        existingAedsmodel.setNewinstall(obj.getString("newinstall"));
                        existingAedsmodel.setInstallcomplete(obj.getString("installcomplete"));
                        existingAedsmodel.setAedmissing(obj.getString("aedmissing"));
                        existingAedsmodel.setDatecompleted(obj.getString("datecompleted"));
                        existingAedsmodel.setPediatrickey(obj.getString("pediatrickey"));
                        existingAedsmodel.setAedstolen(obj.getString("aedstolen"));
                        existingAedsmodel.setAedstolentext(obj.getString("aedstolentext"));
                        existingAedsmodel.setPedalot(obj.getString("padalot"));
                        existingAedsmodel.setPedblot(obj.getString("padblot"));
                        existingAedsmodel.setPedpadlot(obj.getString("pedpadlot"));
                        existingAedsmodel.setPedpadno(obj.getString("pedpadna"));
                        existingAedsmodel.setSparebattna(obj.getString("sparebattna"));
                        existingAedsmodel.setAedcomments(obj.getString("aedcomments"));
                        existingAedsmodelArrayList.add(existingAedsmodel);

                    }
                    catch (JSONException e)
                    {
                        Log.e("fa",e.getMessage());
                    }


                }


                Schoolifomodel.setExistingAedsmodelArrayList(existingAedsmodelArrayList);
                Schoolifomodel.setDrillid(drill.getString("id"));
                Schoolifomodel.setServicecallid(servicecall.getString("id"));
                Schoolifomodel.setNewinstallid(newinstall.getString("id"));
                if(!newinstall.getString("newaedids").equals("null"))
                {
                    JSONArray aedids=   newinstall.getJSONArray("newaedids");
                    ArrayList<String> newaedids = new ArrayList<String>();
                    for( int ji = 0 ; ji < aedids.length(); ji++ )
                    {
                        JSONObject obj = aedids.getJSONObject(ji);
                        newaedids.add( obj.getString( "serial" ) );
                    }
                    
                    Schoolifomodel.setNewinstallAedids(newaedids);
                }
                if(!drill.getString("id").equals("null"))
                {

                    int check=checkduplicate(selectedschool.get(i),drilllist);
                    if(check==-1) {
                        drilllist.add(Schoolifomodel);
                    }
                    else
                    {

                        drilllist.set(check,Schoolifomodel);
                    }
                }
                if(!servicecall.getString("id").equals("null"))
                {

                    int check=checkduplicate(selectedschool.get(i),servicelist);
                    if(check==-1) {
                        servicelist.add(Schoolifomodel);
                    }
                    else
                    {

                        servicelist.set(check,Schoolifomodel);
                    }
                }
                if(!newinstall.getString("id").equals("null"))
                {

                    int check=checkduplicate(selectedschool.get(i),installlist);
                    if(check==-1) {
                        installlist.add(Schoolifomodel);
                    }
                    else
                    {

                        installlist.set(check,Schoolifomodel);
                    }
                }


                    schoolinfomodelsarraylist.add(Schoolifomodel);
                    CommonUtilities.logMe( "done adding school!"+ schoolid );




            } catch (Exception e) {
                e.printStackTrace();
                Log.e("in catch", "");
            }




        }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            dialog.setMessage("Downloading data for "+values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);



            dialog.dismiss();

            // Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
            try {

                if (status.equals("success")) {
                    listops listops=new listops(getActivity());
                    listops.putdrilllist(drilllist);
                    listops.putservicelist(servicelist);
                    listops.putinstallllist(installlist);
                    
                        MyToast.popmessage("Data Downloaded",getActivity());
                        
                } else {
                    MyToast.popmessage("There is some issue while connecting. Please try again after some time",getActivity());
                    
                }
                
                
            } catch (Exception e) {
                
            }
            
        }
    }

    private int checkduplicate(String s,ArrayList<Schoolinfomodel> list) {
        int dup=-1;
        listops listops=new listops(getActivity());

        for(int i=0;i<list.size();i++)
        {
            if(s.equals(list.get(i).getSchoolid()))
            {
                dup=i;
            }
        }
        return dup;

    }


    public interface onpickschool
    {
        public void setupdrill();
    }

}

