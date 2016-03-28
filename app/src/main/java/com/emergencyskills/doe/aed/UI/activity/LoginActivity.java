package com.emergencyskills.doe.aed.UI.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.emergencyskills.doe.aed.R;
import com.emergencyskills.doe.aed.utils.CommonUtilities;
import com.emergencyskills.doe.aed.utils.Constants;
import com.emergencyskills.doe.aed.utils.MyToast;
import com.emergencyskills.doe.aed.utils.Networkstate;
import com.emergencyskills.doe.aed.utils.WebServiceHandler;
import com.emergencyskills.doe.aed.utils.listops;
import com.emergencyskills.doe.aed.models.PickSchoolModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends Activity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private ActionBar actionBar;
    String username;
    String userid;
    String fullname, firstname;
    String password;

    static String logmess = "";
    public static String myversion = "2.8"; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();


        
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;


        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        String login=prefs.getString(Constants.loginkey,"");
        listops listops=new listops(LoginActivity.this);


        Networkstate nest=new Networkstate(LoginActivity.this);

            if(login.equals(Constants.loginstatus))
            {
                Intent intent = new Intent(LoginActivity.this, TabsActivity.class);
                startActivity(intent);
                finish();
            }




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername.getText().toString().equals("") || (etPassword.getText().toString().equals(""))) {
                    //Toast.makeText(getApplicationContext(), "Please fill all the required fields.", Toast.LENGTH_LONG).show();
                    MyToast.popmessage("Please Enter all the fields",LoginActivity.this);
                } else {


                    if(CommonUtilities.isConnectingToInternet(getApplicationContext()))
                    {
                        new SignInTask().execute();
                        username = etUsername.getText().toString();
                        password = etPassword.getText().toString();
                    }
                    else
                    {
                        // Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_LONG).show();
                        MyToast.popmessage("No Internet Connection",LoginActivity.this);
                    }

                }
            }
        });

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        CommonUtilities.logMe( "about to check for version " );
        try {
            WebServiceHandler wsb = new WebServiceHandler();
            ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            String      result = wsb.getWebServiceData("http://doe.emergencyskills.com/api/version.php", postParameters);
            JSONObject jsonObject = new JSONObject(result);
            String version = jsonObject.getString( "version" );
            String features = jsonObject.getString( "features" );
            CommonUtilities.logMe( "version is : " + version );
            if( !myversion.equals( version ) )
            {
                MyToast.popmessagelong("There is a new build available. Please download for these features: " + features,LoginActivity.this);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vireo.org/esiapp"));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(browserIntent);
            }
        }
        catch( Exception exc )
        {
            exc.printStackTrace();
        }

    }

    private void init() {
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

       // etUsername.setText("tapan");
       // etPassword.setText("tapan");
        btnLogin = (Button) findViewById(R.id.btnLogin);
        actionBar = getActionBar();
        actionBar.hide();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class SignInTask extends AsyncTask<Void, Void, Void> {
        private WebServiceHandler wshObj = new WebServiceHandler();
        String result = "";
        private ProgressDialog dialog;
        String status = "nothingyet";
        JSONArray visibleZips = new JSONArray();
        String error_message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Signing In...");
            dialog.setIndeterminate(false);
            dialog.show();



        }

        @Override
        protected Void doInBackground(Void... params) {
            // call a web service to check user is register or not



            try {
                WebServiceHandler wsb = new WebServiceHandler();
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("apikey", "daMEs26rufAqasw2pUYU"));
                postParameters.add(new BasicNameValuePair("method", "doLogin"));
                postParameters.add(new BasicNameValuePair("username", username));
                postParameters.add(new BasicNameValuePair("password", password));
                result = wsb.getWebServiceData("http://doe.emergencyskills.com/api/api.php", postParameters);
//                Log.e("username", ""+etUsername.getText().toString());
//                Log.e("password", ""+etPassword.getText().toString());
                CommonUtilities.logMe( "result is: " + result );
                
                JSONObject jsonObject = new JSONObject(result);
                JSONObject attrs = jsonObject.getJSONObject( "attrs" );
                
                status = jsonObject.getString("status");
                userid = jsonObject.getString("userid");
                firstname = attrs.getString("firstname");
                fullname = attrs.getString("name");
                visibleZips = attrs.getJSONArray("visiblezips");
                
//                error_message = jsonObject.getString("error_message");


            } catch (Exception e) {
                e.printStackTrace();

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            // Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();


            try {

                if (status.equals("success")) {


                    etPassword.setText("");
                    etUsername.setText("");

                    SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
                    SharedPreferences.Editor editor=prefs.edit();
                    editor.putString(Constants.loginkey,Constants.loginstatus);
                    editor.commit();

                    listops listops=new listops(LoginActivity.this);
                    listops.putString("username",username);
                    listops.putString("firstname",firstname);
                    listops.putString("fullname",fullname);
                    listops.putString("userid",userid);

                    ArrayList<String> list = new ArrayList<String>();
//                    CommonUtilities.logMe( "zip list is : " + visibleZips.length() );
                    list.add( "Search By Zip" );
                    for(int i = visibleZips.length()-1; i >= 0; i--){
                        list.add(visibleZips.getJSONObject(i).getString("zip"));
                    }
                    listops.putZipList( list );
                    
//                    CommonUtilities.logMe(" setting first name to : " + firstname ); 

                    new GetSchoolsTask().execute();

                } else if (status.equals("error")) {

//                    if (error_message.contains("invalid username")) {
                        //Toast.makeText(getApplicationContext(), "Invalid username or password.", Toast.LENGTH_LONG).show();
                    MyToast.popmessage("Invalid Username and Password",LoginActivity.this);
//                    }

                } else {
                   // Toast.makeText(getApplicationContext(), "There is some issue while connecting. Please try again after some time.", Toast.LENGTH_LONG).show();
                    MyToast.popmessage("There is some issue while connecting. Please try again after some time",LoginActivity.this);
                }


            } catch (Exception e) {

            }


        }
    }

    private class GetSchoolsTask extends AsyncTask<Void, Void, Void> {

        private WebServiceHandler wshObj = new WebServiceHandler();
        String result = "";
        private ProgressDialog dialog;
        String status = "nothingyet";
        private ArrayList<PickSchoolModel> pickSchoolModelArrayList;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Loading data...");
            dialog.setIndeterminate(false);
            dialog.show();


            pickSchoolModelArrayList = new ArrayList<>();



        }

        @Override
        protected Void doInBackground(Void... params) {
            // call a web service to check user is register or not



            try {
                WebServiceHandler wsb = new WebServiceHandler();
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("apikey", "daMEs26rufAqasw2pUYU"));
                postParameters.add(new BasicNameValuePair("method", "getSchools"));
                postParameters.add(new BasicNameValuePair("userid", userid));
                result = wsb.getWebServiceData("http://doe.emergencyskills.com/api/api.php", postParameters);



                JSONObject jsonObject = new JSONObject(result);
                status = jsonObject.getString("status");
                JSONArray jsonArray = jsonObject.getJSONArray("schools");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    PickSchoolModel pickSchoolModel = new PickSchoolModel();
                    String SchoolID = jsonObject1.getString("id");

                    String SchoolName = jsonObject1.getString("name");
                    String SchoolCode = jsonObject1.getString("code");
                    String SchoolZipCode = jsonObject1.getString("zipcode");
                    String SchoolCampusID = jsonObject1.getString("campusid");
                    String SchoolAddress = jsonObject1.getString("address");
                    String SchoolCity = jsonObject1.getString("city");
                    String key = jsonObject1.getString("key");
                    String SchoolCampusName = jsonObject1.getString("campusname");
                    pickSchoolModel.setSchoolID(SchoolID);
                    pickSchoolModel.setSchoolName(SchoolName);
                    pickSchoolModel.setSchoolCode(SchoolCode);
                    pickSchoolModel.setSchoolZipCode(SchoolZipCode);
                    pickSchoolModel.setSchoolCampusID(SchoolCampusID);
                    pickSchoolModel.setSchoolAddress(SchoolAddress);
CommonUtilities.logMe( "key?"  + SchoolName + "...." + key );
                    pickSchoolModel.setKey(key);
CommonUtilities.logMe( "city?"  + SchoolCity );
                    pickSchoolModel.setSchoolCity(SchoolCity);
                    pickSchoolModel.setSchoolCampusName(SchoolCampusName);

                    pickSchoolModelArrayList.add(pickSchoolModel);

                }


            } catch (Exception e) {
                e.printStackTrace();

            }

            logmess += "done with loading list: " + pickSchoolModelArrayList.size() + "\n";

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

//            logmess += ("result is: "+result + "\n");
            logmess += ("status is: "+status + "\n");

            try {

                if (status.equals("success")) {
//                        Toast.makeText(getApplicationContext(), "" + result, Toast.LENGTH_LONG).show();


                    if(pickSchoolModelArrayList.size()!=0)
                    {
                        listops listops=new listops(LoginActivity.this);
                        listops.putpickschoollist(pickSchoolModelArrayList);

                        Intent intent = new Intent(LoginActivity.this, TabsActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    else
                    {
                       // Toast.makeText(getApplicationContext(), "No Schools Found", Toast.LENGTH_LONG).show();
                        MyToast.popmessage("No Schools Found",LoginActivity.this);


                    }








                } else {
                    MyToast.popmessage("There is some issue while connecting. Please try again after some time",LoginActivity.this);

                }


            } catch (Exception e) {

            }


        }
    }
}
