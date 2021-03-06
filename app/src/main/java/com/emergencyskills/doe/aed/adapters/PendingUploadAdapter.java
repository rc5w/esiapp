package com.emergencyskills.doe.aed.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.emergencyskills.doe.aed.R;
import com.emergencyskills.doe.aed.UI.activity.LoginActivity;
import com.emergencyskills.doe.aed.UI.activity.TabsActivity;
import com.emergencyskills.doe.aed.UI.fragments.PendingUploadsFragment;
import com.emergencyskills.doe.aed.UI.fragments.toservice;
import com.emergencyskills.doe.aed.utils.Constants;
import com.emergencyskills.doe.aed.utils.listops;
import com.emergencyskills.doe.aed.models.PendingUploadModel;
import com.emergencyskills.doe.aed.models.PickSchoolModel;
import com.emergencyskills.doe.aed.models.Schoolinfomodel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by BWT on 9/30/15.
 */
public class PendingUploadAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PendingUploadModel> mPickSchoolListModelArray;
    public PendingUploadAdapter(Context context, ArrayList<PendingUploadModel> pickSchoolListModelArray)
    {

        mContext=context;
        mPickSchoolListModelArray = pickSchoolListModelArray;

    }
    @Override
    public int getCount() {
        return mPickSchoolListModelArray.size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.adapter_pending_uploads, parent, false);
        TextView tvSchoolCodeValue= (TextView) convertView.findViewById(R.id.tvSchoolCodeValue);
        TextView tvSchoolNameValue= (TextView) convertView.findViewById(R.id.tvSchoolNameValue);
        TextView tvPendingUploadButton=(TextView)convertView.findViewById(R.id.tvPendingUploadButton);
        TextView tvDiscard=(TextView)convertView.findViewById(R.id.tvDiscard);
        tvSchoolCodeValue.setText(mPickSchoolListModelArray.get(position).getCode());
        tvSchoolNameValue.setText(mPickSchoolListModelArray.get(position).getSchoolName());


        if(mPickSchoolListModelArray.get(position).getDrillstatus().equals("Pending"))
        {
            tvPendingUploadButton.setText("Drill");
            tvPendingUploadButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_buttons_background_not_completed_button));

        }
         if(mPickSchoolListModelArray.get(position).getServicestatus().equals("Pending"))
        {
            tvPendingUploadButton.setText("SC");
            tvPendingUploadButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_buttons_background_not_completed_button));

        }
        if(mPickSchoolListModelArray.get(position).getInstallstatus().equals("Pending"))
        {
            tvPendingUploadButton.setText("NI");
            tvPendingUploadButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_buttons_background_not_completed_button));

        }

         if(mPickSchoolListModelArray.get(position).getDrillstatus().equals("Pending")&&mPickSchoolListModelArray.get(position).getInstallstatus().equals("Pending"))
        {
            tvPendingUploadButton.setText("Drill,NI");
            tvPendingUploadButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_buttons_background_not_completed_button));

        }
         if(mPickSchoolListModelArray.get(position).getDrillstatus().equals("Pending")&&mPickSchoolListModelArray.get(position).getServicestatus().equals("Pending"))
        {
            tvPendingUploadButton.setText("Drill,SC");
            tvPendingUploadButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_buttons_background_not_completed_button));

        }

        if(mPickSchoolListModelArray.get(position).getInstallstatus().equals("Pending")&&mPickSchoolListModelArray.get(position).getServicestatus().equals("Pending"))
        {
            tvPendingUploadButton.setText("NI,SC");
            tvPendingUploadButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_buttons_background_not_completed_button));

        }
        if(mPickSchoolListModelArray.get(position).getInstallstatus().equals("Pending")&&mPickSchoolListModelArray.get(position).getServicestatus().equals("Pending")&&mPickSchoolListModelArray.get(position).getDrillstatus().equals("Pending"))
        {
            tvPendingUploadButton.setText("Drill,NI,SC");
            tvPendingUploadButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_buttons_background_not_completed_button));

        }
        tvDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final Dialog dialog=new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_removependingupload);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)));

                Button yes=(Button)dialog.findViewById(R.id.yesbtn);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listops listops=new listops(mContext);
                        ArrayList<PendingUploadModel> list=listops.getpendinglist();
                        list.remove(position);
                        listops.putpendinglist(list);



                        if (mContext instanceof TabsActivity) {

                            PendingUploadsFragment fm=new PendingUploadsFragment();
                            ((TabsActivity) mContext).setDatachangefragment(fm);
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
        });

        return convertView;
    }
}
