package com.emergencyskills.doe.aed.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Environment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
    
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Bhupinderjit on 6/17/2015.
 */
public class CommonUtilities {

    public static String getLog()
    {
        
        try
        {
            File externalStorageDir = Environment.getExternalStorageDirectory();
            File file = new File(externalStorageDir, "ESI_log.txt");
            
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;
            
            while((line =bufferedReader.readLine())!=null){
                
                stringBuffer.append(line).append("\n");
            }
            
            return stringBuffer.toString();
        }
        catch( Exception e )
        {
            e.printStackTrace();
            return "";
        }
        
    }
    
    public static void logMe( String message )
    {
        System.err.println( message );
        File externalStorageDir = Environment.getExternalStorageDirectory();
        File file = new File(externalStorageDir, "ESI_log.txt");
           try
           {
               BufferedWriter buf = new BufferedWriter(new FileWriter(file));
               buf.append(message);
               buf.newLine();
               buf.close();
           }
           catch (IOException e)
           {
                   // TODO Auto-generated catch block
//               e.printStackTrace();
           }
    }
        

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        Log.i("key", key);
        return key;
    }


    public static boolean hasData(String str) {

        if (str != null) {
            if (!str.equals("") && !str.equals("null"))
                return true;
            else
                return false;
        } else
            return false;
    }

    public static Void toast(Context context, String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();

        return null;
    }

    public static Void toastShort(Context context, String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

        return null;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean validCellPhone(String number)
    {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }

    // check for internet connection
    public static boolean isConnectingToInternet(Context mContext){
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    public static void execute(Activity activity, AsyncTask<Void , Void,Void> asyncTask){
        if(isConnectingToInternet(activity)){
            asyncTask.execute();
        } else {
            Toast.makeText(activity, "No Internet Connection.", Toast.LENGTH_LONG).show();
            activity.finish();

        }
    }

    public static int[] getDisplayDimensions(Activity activity)
    {
        int width = activity.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        int height = activity.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        int[] dimensions=new int[]{width, height};

        return dimensions;

    }




    private static void showAlert(String message,Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }




   /* public static void openSocialShare(final Activity activity) {
        final Context context = activity.getApplicationContext();
        final Dialog dialog = new Dialog(activity);

        //setting custom layout to dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_layout);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout llFacebook = (LinearLayout) dialog.findViewById(R.id.llFacebook);
        LinearLayout llTwitter = (LinearLayout) dialog.findViewById(R.id.llTwitter);
        LinearLayout llGooglePlus = (LinearLayout) dialog.findViewById(R.id.llGooglePlus);
        LinearLayout llYoutube = (LinearLayout) dialog.findViewById(R.id.llYoutube);

        llFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookUrl = "https://www.facebook.com/pages/Glassdolls-Entertainment/1433292353576565";
                try {
                    int versionCode = context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) {
                        Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        ;
                    } else {
                        // open the Facebook app using the old method (fb://profile/id or fb://page/id)
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/336227679757310")));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    // Facebook is not installed. Open the browser
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
                }
            }
        });

        llTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetUrl = String.format("https://twitter.com/glassdolls_ent");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

// Narrow down to official Twitter app, if available:
                List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                        intent.setPackage(info.activityInfo.packageName);
                    }
                }
                activity.startActivity(intent);

            }
        });

        llGooglePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String googlePlusUrl = "https://plus.google.com/u/0/107067704581423831554/posts";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googlePlusUrl));

// Narrow down to official Twitter app, if available:
                List<ResolveInfo> matches = context.getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.google.android.apps.plus")) {
                        intent.setPackage(info.activityInfo.packageName);
                    }
                }
                activity.startActivity(intent);


            }
        });

        llYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                String url = "https://www.youtube.com/channel/UCMYwBIyvnqYOYZ5v1XF80cA";
                try {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.youtube");
                    intent.setData(Uri.parse(url));
                    activity.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    activity.startActivity(intent);
                }

            }
        });

        //adding text dynamically


        //adding button click event

        dialog.show();


    }

*/




}



