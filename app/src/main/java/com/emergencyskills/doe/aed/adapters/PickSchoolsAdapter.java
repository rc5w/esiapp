package com.emergencyskills.doe.aed.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.emergencyskills.doe.aed.R;
import com.emergencyskills.doe.aed.utils.CommonUtilities;
import com.emergencyskills.doe.aed.utils.WebServiceHandler;
import com.emergencyskills.doe.aed.models.PickSchoolModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by BWT on 9/30/15.
 */
public class PickSchoolsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PickSchoolModel> mPickSchoolListModelArray;
    public PickSchoolsAdapter(Context context, ArrayList<PickSchoolModel> pickSchoolListModelArray)
    {

        mContext=context;
        mPickSchoolListModelArray = pickSchoolListModelArray;
      // new DownloadDataTask().execute();

    }
    @Override
    public int getCount() {
        try {
            return mPickSchoolListModelArray.size();
        }
        catch (NullPointerException e)
        {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mPickSchoolListModelArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//CommonUtilities.logMe( "key for position " + position + " is: " + mPickSchoolListModelArray.get(position).getKey() + "..." + isEnabled( position ) );
        if( isEnabled( position ) )
        {
            final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_pick_school, parent, false);
            TextView tvSchoolCodeValue= (TextView) convertView.findViewById(R.id.tvSchoolCodeValue);
            TextView tvSchoolNameValue= (TextView) convertView.findViewById(R.id.tvSchoolNameValue);
            TextView tvSchoolZipValue= (TextView) convertView.findViewById(R.id.tvSchoolZipValue);
            String cname = mPickSchoolListModelArray.get(position).getSchoolCampusName();
//CommonUtilities.logMe( "code: " + mPickSchoolListModelArray.get(position).getSchoolCode() );
            String zip = mPickSchoolListModelArray.get(position).getSchoolZipCode();
            if(cname != null && !"".equals( cname ) && !"None".equals( cname ) )
                zip += "/" + cname;
            
                //final TextView tvDownloadBtn=(TextView) convertView.findViewById(R.id.tvDownloadButton);
            final CheckBox cbVisitingStatus=(CheckBox) convertView.findViewById(R.id.cbVisitingStatus);
            tvSchoolCodeValue.setText(mPickSchoolListModelArray.get(position).getSchoolCode());
            tvSchoolNameValue.setText(mPickSchoolListModelArray.get(position).getSchoolName() + "\n" + mPickSchoolListModelArray.get(position).getSchoolAddress() + ", "  + mPickSchoolListModelArray.get(position).getSchoolCity());
            tvSchoolZipValue.setText(zip);
            return convertView;
        }
        else
        {
            final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            convertView = inflater.inflate(R.layout.adapter_zip_code, parent, false);
            TextView tvSchoolCodeValue= (TextView) convertView.findViewById(R.id.tvSchoolCodeValue);
//            tvSchoolCodeValue.setText(mPickSchoolListModelArray.get(position).getKey());
            return convertView;
        }
    }


    public boolean isEnabled(int position) {
        return "".equals( mPickSchoolListModelArray.get(position).getKey() );
    }
    

    private class DownloadDataTask extends AsyncTask<Void, Void, Void> {

        private WebServiceHandler wshObj = new WebServiceHandler();
        String result = "";
        private ProgressDialog dialog;
        String status="";





        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Loading data...");
            dialog.setIndeterminate(false);
            dialog.show();


            Log.e("in pre", "");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // call a web service to check user is register or not
            Log.e("in back","");
            try {
                WebServiceHandler wsb=new WebServiceHandler();
                ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("apikey", "daMEs26rufAqasw2pUYU"));
                postParameters.add(new BasicNameValuePair("method", "downloadData"));
                postParameters.add(new BasicNameValuePair("schoolid","13363" ));
                result=wsb.getWebServiceData("http://doe.emergencyskills.com/api/tester.php", postParameters);


                Log.e("result", result);

                JSONObject jsonObject=new JSONObject(result);
                status=jsonObject.getString("status");


            }
            catch(Exception e)
            {
                e.printStackTrace();
                Log.e("in catch","");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();

            // Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
            try
            {
                if(status.equals("success"))
                {
                    Toast.makeText(mContext,""+result,Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(mContext,"There is some issue while connecting. Please try again after some time.",Toast.LENGTH_LONG).show();

                }
            }

            catch(Exception e)
            {

            }

        }
    }
        // end download data task
}
