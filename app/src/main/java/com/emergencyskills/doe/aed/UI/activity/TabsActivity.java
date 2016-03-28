/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.emergencyskills.doe.aed.UI.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.net.Uri;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emergencyskills.doe.aed.R;
import com.emergencyskills.doe.aed.UI.fragments.Addnewinstall;
import com.emergencyskills.doe.aed.UI.fragments.DrillsFragment;
import com.emergencyskills.doe.aed.UI.fragments.NewInstallsFragment;
import com.emergencyskills.doe.aed.UI.fragments.PendingUploadsFragment;
import com.emergencyskills.doe.aed.UI.fragments.PendingUploadsFragment.RefreshList;
import com.emergencyskills.doe.aed.UI.fragments.PickSchoolsFragment;
import com.emergencyskills.doe.aed.UI.fragments.Semiannualdrill;
import com.emergencyskills.doe.aed.UI.fragments.ServiceCallsFragment;

import com.emergencyskills.doe.aed.UI.fragments.toservice;
import com.emergencyskills.doe.aed.utils.Constants;

import com.emergencyskills.doe.aed.utils.MyToast;
//import com.emergencyskills.doe.aed.utils.NetworkStateBroadcast;
import com.emergencyskills.doe.aed.utils.WebServiceHandler;
import com.emergencyskills.doe.aed.utils.listops;
import com.emergencyskills.doe.aed.utils.CommonUtilities;
import com.emergencyskills.doe.aed.adapters.NewInstallsAdapter;
import com.emergencyskills.doe.aed.adapters.PendingUploadAdapter;
import com.emergencyskills.doe.aed.adapters.PickSchoolsAdapter;
import com.emergencyskills.doe.aed.adapters.DrillsAdapter;
import com.emergencyskills.doe.aed.interfaces.AdapterNotifyDatachange;
import com.emergencyskills.doe.aed.models.PendingUploadModel;
import com.emergencyskills.doe.aed.models.PickSchoolModel;
import com.emergencyskills.doe.aed.models.Schoolinfomodel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class TabsActivity extends FragmentActivity implements ServiceCallsFragment.setservicelist,PickSchoolsFragment.onpickschool,AdapterNotifyDatachange,Addnewinstall.setinstalllist,Semiannualdrill.SetDrillsList,Semiannualdrill.GoToServiceCall,RefreshList {


    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private LinearLayout ll4;
    private LinearLayout ll5;
    private LinearLayout ll6;
    private LinearLayout llPickSchools;
    private LinearLayout llDrills;
    private LinearLayout llServiceCalls;
    private LinearLayout llNewInstalls;
    private LinearLayout llPendingUploads;
    FragmentTransaction transaction;
//    private ImageView img;
    String s;

    private FrameLayout frameLayout;


    public static final String AUTHORITY = "com.emergencyskills.doe.aed.utils.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "emergencyskills.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    Account mAccount;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */


    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        

        init();
        
        mAccount=CreateSyncAccount(this);
        //registering the broadcast
        registerReceiver(broadcastReceiver, new IntentFilter("uploadsuccessful"));


        // Set up the action bar.
        final ActionBar actionBar = getActionBar();


        actionBar.hide();
        //atteching fragment to framelayout
        PickSchoolsFragment myf = new PickSchoolsFragment();
        transaction=getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame, myf);
        transaction.commit();


        llPickSchools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(checkcurrentfragment())
                {
                   showconfirmationdialog(1);
                }
                else {

                   showpickupschool();
                }


            }
        });

        llDrills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkcurrentfragment())
                {
                    showconfirmationdialog(2);
                }
                else {

                   showdrill();
                }

            }
        });

        llServiceCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkcurrentfragment())
                {
                    showconfirmationdialog(3);
                }
                else {
                    showservice();
                }

            }
        });

        llNewInstalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkcurrentfragment())
                {
                    showconfirmationdialog(4);
                }
                else {

                   showinstall();
                }
            }
        });

        llPendingUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkcurrentfragment())
                {
                    showconfirmationdialog(5);
                }
                else {
                   showpending();
                }
            }
        });







    }


    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            return newAccount;
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            return null;
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {



            Bundle settingsBundle = new Bundle();
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_MANUAL, true);
            settingsBundle.putBoolean(
                    ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
            ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);



        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    public void showpickupschool()
    {
        ll1.setBackgroundColor(getResources().getColor(R.color.White));
        ll2.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll3.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll4.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll5.setBackgroundColor(getResources().getColor(R.color.FullTransparent));


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PickSchoolsFragment myf = new PickSchoolsFragment();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.frame, myf);
        transaction.commit();
    }
    public void showdrill()
    {
        ll2.setBackgroundColor(getResources().getColor(R.color.White));
        ll1.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll3.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll4.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll5.setBackgroundColor(getResources().getColor(R.color.FullTransparent));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DrillsFragment myf = new DrillsFragment();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.frame, myf);

        transaction.commit();
    }
    public void showservice()
    {
        ll3.setBackgroundColor(getResources().getColor(R.color.White));
        ll2.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll5.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll4.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll1.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        toservice myf = new toservice();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.frame, myf);
        transaction.commit();
    }
    public void showinstall()
    {
        ll4.setBackgroundColor(getResources().getColor(R.color.White));
        ll2.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll3.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll1.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll5.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        NewInstallsFragment myf = new NewInstallsFragment();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.frame, myf);
        transaction.commit();
    }
    public void showpending()
    {
        ll5.setBackgroundColor(getResources().getColor(R.color.White));
        ll2.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll3.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll4.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll1.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PendingUploadsFragment myf = new PendingUploadsFragment();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.frame, myf);
        transaction.commit();
    }

    public void showconfirmationdialog(final int fragnumber)
    {
        final Dialog dialog=new Dialog(TabsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_leaving_confirmation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        Button yes=(Button)dialog.findViewById(R.id.yesbtn);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (fragnumber)
                {
                    case 1:
                        showpickupschool();
                        break;
                    case 2:
                        showdrill();
                        break;
                    case 3:
                        showservice();
                        break;
                    case 4:
                        showinstall();
                        break;
                    case 5:
                        showpending();
                        break;

            }
                dialog.dismiss();

            }
        });
        Button no=(Button)dialog.findViewById(R.id.nobtn);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ImageView close=(ImageView) dialog.findViewById(R.id.ivClose);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void semiandrill(String schoolid,String principal,String contact,String schoolcode,String address,String phone,String aedemail,String principalemail,String drillis,int position,String contactphone,String state,String city,String zip)
    {
        ll2.setBackgroundColor(getResources().getColor(R.color.White));
        ll1.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll3.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll4.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll5.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        Semiannualdrill myf=new Semiannualdrill();
        Bundle bundle=new Bundle();
        bundle.putString("schoolid",schoolid);
        bundle.putString("principal",principal);
        bundle.putString("contact",contact);
        bundle.putString("schoolcode",schoolcode);
        bundle.putString("address",address);
        bundle.putString("phone",phone);
        bundle.putString("contactemail",aedemail);
        bundle.putString("principalemail",principalemail);
        bundle.putString("drillid",drillis);
        bundle.putInt("pos",position);
        bundle.putString("contactphone",contactphone);
        bundle.putString("state",state);
        bundle.putString("city",city);
        bundle.putString("zip",zip);
        myf.setArguments(bundle);
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.frame, myf,"Drill");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addnewinstall(ArrayList serialnums, String schoolid,String principal,String contact,String schoolcode,String address,String phone,String aedemail,String principalemail,String drillis,int pos,String contactphone,String state,String city,String zip)
    {
        ll4.setBackgroundColor(getResources().getColor(R.color.White));
        ll2.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll3.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll1.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll5.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        Addnewinstall myf=new Addnewinstall();
        Bundle bundle=new Bundle();
        bundle.putString("schoolid",schoolid);
        bundle.putString("principal",principal);
        bundle.putString("contact",contact);
        bundle.putString("schoolcode",schoolcode);
        bundle.putString("address",address);
        bundle.putString("phone",phone);
        bundle.putString("contactemail",aedemail);
        bundle.putString("principalemail",principalemail);
        bundle.putString("installid",drillis);
        bundle.putStringArrayList("serialnumbers",serialnums);
        bundle.putInt("pos",pos);
        bundle.putString("contactphone",contactphone);
        bundle.putString("state",state);
        bundle.putString("city",city);
        bundle.putString("zip",zip);
        myf.setArguments(bundle);
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.frame, myf,"Newinstall");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addservice(String schoolid,String principal,String contact,String schoolcode,String address,String phone,String aedemail,String principalemail,String serviceid,int pos,String contactphone,String state,String city,String zip)
    {
        ll3.setBackgroundColor(getResources().getColor(R.color.White));
        ll2.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll5.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll4.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        ll1.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        ServiceCallsFragment myf=new ServiceCallsFragment();
        Bundle bundle=new Bundle();
        bundle.putString("schoolid",schoolid);
        bundle.putString("principal",principal);
        bundle.putString("contact",contact);
        bundle.putString("schoolcode",schoolcode);
        bundle.putString("address",address);
        bundle.putString("phone",phone);
        bundle.putString("servicecallid",serviceid);
        bundle.putString("contactemail",aedemail);
        bundle.putString("principalemail",principalemail);
        bundle.putInt("pos",pos);
        bundle.putString("contactphone",contactphone);
        bundle.putString("state",state);
        bundle.putString("city",city);
        bundle.putString("zip",zip);
        myf.setArguments(bundle);
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.frame, myf,"Servicecall");
        transaction.addToBackStack(null);
        transaction.commit();
    }




    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void setupdrill() {

    }






    public boolean checkcurrentfragment()
    {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (f instanceof Semiannualdrill|| f instanceof ServiceCallsFragment|| f instanceof Addnewinstall)
        {
         return true;
        }
        else
        {
            return false;
        }

    }

    public boolean checkcurrentfragmentispending()
    {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (f instanceof PendingUploadsFragment)
        {
            return true;
        }
        else
        {
            return false;
        }

    }









    void init() {

//        img=(ImageView)findViewById(R.id.logo);

        TextView build =(TextView)findViewById(R.id.checkfornew);
        build.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                        System.err.println( "version is : " + version );
                        if( !LoginActivity.myversion.equals( version ) )
                        {
                            MyToast.popmessagelong("There is a new build available. Please download for these features: " + features,TabsActivity.this);
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vireo.org/esiapp"));
                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(browserIntent);
                        }
                        else
                        {
                            MyToast.popmessagelong("You have the most current version!",TabsActivity.this);
                        }
                    }
        catch( Exception exc )
        {
            exc.printStackTrace();
        }


                }
            }
            );
            
        TextView maillog =(TextView)findViewById(R.id.maillog);
        maillog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                final Dialog dialog=new Dialog(TabsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_logout);

                TextView question=(TextView)dialog.findViewById(R.id.question);
                question.setText( "Are you sure you want to email the log?" );
                TextView extra=(TextView)dialog.findViewById(R.id.extratext);
                extra.setText( "" );
                
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                Button yes=(Button)dialog.findViewById(R.id.yesbtn);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"rachelc@gmail.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, "Sending Log");
                        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(TabsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                        
                        finish();
                    }
                });
                Button no=(Button)dialog.findViewById(R.id.nobtn);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    dialog.dismiss();
                    }
                });
                ImageView close=(ImageView) dialog.findViewById(R.id.ivClose);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                            }
                });

                dialog.show();

                }
            }
            );
            

        
        listops listops=new listops(TabsActivity.this);
        CommonUtilities.logMe( "logging in as: " + listops.getString( "firstname" ) );
        TextView name =(TextView)findViewById(R.id.welcome);
        name.setText( "Welcome, " + listops.getString( "firstname" ) );

        TextView logoutname =(TextView)findViewById(R.id.logoutname);
        logoutname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(TabsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_logout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                dialog.setContentView(R.layout.dialog_logout);
                Button yes=(Button)dialog.findViewById(R.id.yesbtn);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyToast.popmessagelong("Logging out... ",TabsActivity.this);
                        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
                        SharedPreferences.Editor editor=prefs.edit();
                        editor.putString(Constants.loginkey,"");
                        editor.commit();
                        listops listops=new listops(TabsActivity.this);
                        //make sure to remove the downloaded schools
                        
                        Intent intent=new Intent(TabsActivity.this,LoginActivity.class);
                        startActivity(intent);
                        ArrayList<Schoolinfomodel> ls=new ArrayList<Schoolinfomodel>();
                        listops.putdrilllist(ls);
                        listops.putservicelist(ls);
                        listops.putinstallllist(ls);
                        ArrayList<PendingUploadModel> l=new ArrayList<PendingUploadModel>();
                        listops.putpendinglist(l);


                        finish();
                    }
                });
                Button no=(Button)dialog.findViewById(R.id.nobtn);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    dialog.dismiss();
                    }
                });
                ImageView close=(ImageView) dialog.findViewById(R.id.ivClose);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                            }
                });

                dialog.show();
            }



        });



        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3 = (LinearLayout) findViewById(R.id.ll3);
        ll4 = (LinearLayout) findViewById(R.id.ll4);
        ll5 = (LinearLayout) findViewById(R.id.ll5);
        ll6 = (LinearLayout) findViewById(R.id.ll6);
        ll1.setBackgroundColor(getResources().getColor(R.color.White));

        llPickSchools = (LinearLayout) findViewById(R.id.llPickSchool);
        llDrills = (LinearLayout) findViewById(R.id.llDrills);
        llServiceCalls = (LinearLayout) findViewById(R.id.llServiceCalls);
        llNewInstalls = (LinearLayout) findViewById(R.id.llNewInstalls);
        llPendingUploads = (LinearLayout) findViewById(R.id.llPendingUploads);

        frameLayout=(FrameLayout)findViewById(R.id.frame);


    }


    @Override
    public void setDatachangefragment(Fragment fm) {



        ll1.setBackgroundColor(getResources().getColor(R.color.FullTransparent));
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();

           transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.frame, fm);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {

        if(checkcurrentfragment())

        {
            final Dialog dialog=new Dialog(TabsActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_leaving_confirmation);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

            Button yes=(Button)dialog.findViewById(R.id.yesbtn);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                   showpickupschool();



                }
            });
            Button no=(Button)dialog.findViewById(R.id.nobtn);
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ImageView close=(ImageView) dialog.findViewById(R.id.ivClose);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void setupinstalllist(int i) {
        if(i==0) {
            showinstall();
        }
        else
        {
            showpending();
        }
    }

    @Override
    public void goToServiceCall(String id) {
        // who knows? 
        Schoolinfomodel mymodel = null;
        listops listops=new listops(TabsActivity.this);
        ArrayList<Schoolinfomodel> list=listops.getservicelist();
        int position = 0;
        int count = 0;
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).isIncludeinservice())
            {
                if( list.get( i ).getSchoolid().equals( id ) )
                {
                    mymodel = list.get( i );
                    position = count;
                    break;
                }
                count++;
            }
        }
        if( mymodel.getServicestatus().equals( "Completed" ) )
            setupDrillList( 0 );
        else
            addservice(mymodel.getSchoolid(), (mymodel.getPrincipal()), (mymodel.getAedcontact()), (mymodel.getSchoolcode()), mymodel.getAddress(), mymodel.getPhone(), mymodel.getAedcontactemail(), mymodel.getPrincemail(), mymodel.getServicecallid(), position,mymodel.getContactphone(),mymodel.getState(),mymodel.getCity(),mymodel.getZip());
        
    }

    @Override
    public void setupDrillList(int i) {

        if(i==0) {
            showdrill();
        }
        else
        {
            showpending();
        }

    }


    
    @Override
    public void onlistdatachanged() {
        showpending();
    }

    @Override
    public void setupservice(int i) {

        if(i==0) {
            showservice();
        }
        else
        {
            showpending();
        }

    }
}


