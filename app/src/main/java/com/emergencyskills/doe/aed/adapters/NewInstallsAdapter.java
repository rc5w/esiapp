package com.emergencyskills.doe.aed.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emergencyskills.doe.aed.R;
import com.emergencyskills.doe.aed.UI.activity.TabsActivity;
import com.emergencyskills.doe.aed.UI.fragments.NewInstallsFragment;
import com.emergencyskills.doe.aed.UI.fragments.toservice;
import com.emergencyskills.doe.aed.utils.Constants;
import com.emergencyskills.doe.aed.utils.MyToast;
import com.emergencyskills.doe.aed.utils.listops;
import com.emergencyskills.doe.aed.models.PickSchoolModel;
import com.emergencyskills.doe.aed.models.Schoolinfomodel;

import java.util.ArrayList;

/**
 * Created by BWT on 9/30/15.
 */
public class NewInstallsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Schoolinfomodel> mPickSchoolListModelArray=new ArrayList<Schoolinfomodel>();
    public NewInstallsAdapter(Context context, ArrayList<Schoolinfomodel> pickSchoolListModelArray1)
    {

        mContext=context;
        for(int i=0;i<pickSchoolListModelArray1.size();i++)
    {
        if(pickSchoolListModelArray1.get(i).isIncludeinnewinstall())
        {
            mPickSchoolListModelArray.add(pickSchoolListModelArray1.get(i));
        }
    }

    }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        convertView = inflater.inflate(R.layout.adapter_newinstalls, parent, false);


        final LinearLayout linearLayout=(LinearLayout)convertView.findViewById(R.id.holderfornewinstall);



       if(mPickSchoolListModelArray.get(position)!=null) {
           TextView tvSchoolCodeValue = (TextView) convertView.findViewById(R.id.tvinstallSchoolCodeValue);
           TextView tvSchoolNameValue = (TextView) convertView.findViewById(R.id.tvinstallSchoolNameValue);
           TextView tvDiscard = (TextView) convertView.findViewById(R.id.tvinstallDiscard);
           final TextView tvPendingUploadButton = (TextView) convertView.findViewById(R.id.tvinstallPendingUploadButton);
           if (mPickSchoolListModelArray.get(position).getInstallstatus().equals("Completed")) {
               tvPendingUploadButton.setText("COMPLETED");
           } else if (mPickSchoolListModelArray.get(position).getInstallstatus().equals("Pending")) {
               tvPendingUploadButton.setText("PENDING UPLOAD");
           } else {
               tvPendingUploadButton.setText("START HERE");
           }
           tvPendingUploadButton.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_corner_buttons_background_not_completed_button));
           tvPendingUploadButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   final TextView f = tvPendingUploadButton;

                   if (f.getText().toString().equals("COMPLETED")) {
                      // Toast.makeText(mContext, "Already Completed", Toast.LENGTH_SHORT).show();
                       MyToast.popmessage("Already Completed",mContext);
                   } else {
                       if (mContext instanceof TabsActivity) {
                           ((TabsActivity) mContext).addnewinstall(mPickSchoolListModelArray.get(position).getNewinstallAedids(), mPickSchoolListModelArray.get(position).getSchoolid(), (mPickSchoolListModelArray.get(position).getPrincipal()), (mPickSchoolListModelArray.get(position).getAedcontact()), (mPickSchoolListModelArray.get(position).getSchoolcode()), mPickSchoolListModelArray.get(position).getAddress(), mPickSchoolListModelArray.get(position).getPhone(), mPickSchoolListModelArray.get(position).getAedcontactemail(), mPickSchoolListModelArray.get(position).getPrincemail(), mPickSchoolListModelArray.get(position).getNewinstallid(), position,mPickSchoolListModelArray.get(position).getContactphone(),mPickSchoolListModelArray.get(position).getState(),mPickSchoolListModelArray.get(position).getCity(),mPickSchoolListModelArray.get(position).getZip());
                       }
                   }
               }
           });


           tvSchoolCodeValue.setText(mPickSchoolListModelArray.get(position).getSchoolcode());
           tvSchoolNameValue.setText(mPickSchoolListModelArray.get(position).getname());
           tvDiscard.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {


                   final Dialog dialog = new Dialog(mContext);
                   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                   dialog.setContentView(R.layout.dialog_remove_drill);
                   dialog.getWindow().setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)));
                   TextView textView = (TextView) dialog.findViewById(R.id.dialogschoolname);
                   textView.setText(mPickSchoolListModelArray.get(position).getname());
                   dialog.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           dialog.dismiss();
                       }
                   });
                   dialog.findViewById(R.id.yesbtn).setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           listops listops = new listops(mContext);

                           ArrayList<Schoolinfomodel> list=listops.getinstalllist();
                           list.remove(position);
                           listops.putinstallllist(list);
                           if (mContext instanceof TabsActivity) {

                               NewInstallsFragment fm=new NewInstallsFragment();
                               ((TabsActivity) mContext).setDatachangefragment(fm);
                           }
                           dialog.dismiss();

                       }
                   });
                   dialog.findViewById(R.id.nobtn).setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           dialog.dismiss();
                       }
                   });
                   dialog.show();


               }
           });
       }

        return convertView;
    }
}
