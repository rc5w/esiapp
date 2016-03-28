package com.emergencyskills.doe.aed.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.emergencyskills.doe.aed.R;
import com.emergencyskills.doe.aed.UI.activity.TabsActivity;
import com.emergencyskills.doe.aed.UI.fragments.DrillsFragment;
import com.emergencyskills.doe.aed.UI.fragments.toservice;
import com.emergencyskills.doe.aed.utils.MyToast;
import com.emergencyskills.doe.aed.utils.listops;
import com.emergencyskills.doe.aed.models.PickSchoolModel;
import com.emergencyskills.doe.aed.models.Schoolinfomodel;

import java.util.ArrayList;

/**
 * Created by BWT on 9/30/15.
 */
public class DrillsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Schoolinfomodel> mPickSchoolListModelArray=new ArrayList<Schoolinfomodel>();
    public DrillsAdapter(Context context, ArrayList<Schoolinfomodel> pickSchoolListModelArray1)
    {

        mContext=context;
        for(int i=0;i<pickSchoolListModelArray1.size();i++)
        {
            if(pickSchoolListModelArray1.get(i).isIncludeindrill())
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
        convertView = inflater.inflate(R.layout.adapter_drills, parent, false);

        final LinearLayout linearLayout=(LinearLayout)convertView.findViewById(R.id.holderfordrill);

        if(mPickSchoolListModelArray.get(position)!=null) {




            final TextView tvSchoolCodeValue = (TextView) convertView.findViewById(R.id.tvSchoolCodeValue);
            TextView tvSchoolNameValue = (TextView) convertView.findViewById(R.id.tvSchoolNameValue);
            final TextView tvPendingUploadButton = (TextView) convertView.findViewById(R.id.tvPendingUploadButton);
            TextView tvDiscard = (TextView) convertView.findViewById(R.id.tvDiscard);

            tvSchoolCodeValue.setText(mPickSchoolListModelArray.get(position).getSchoolcode());
            tvSchoolNameValue.setText(mPickSchoolListModelArray.get(position).getname());


            if (mPickSchoolListModelArray.get(position).getDrillstatus().equals("Completed")) {
                tvPendingUploadButton.setText("COMPLETED");
            } else if (mPickSchoolListModelArray.get(position).getDrillstatus().equals("Pending")) {
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
                        MyToast.popmessage("Already Completed",mContext);
                    } else {
                        if (mContext instanceof TabsActivity) {
                            ((TabsActivity) mContext).semiandrill((mPickSchoolListModelArray.get(position).getSchoolid()), (mPickSchoolListModelArray.get(position).getPrincipal()), (mPickSchoolListModelArray.get(position).getAedcontact()), (mPickSchoolListModelArray.get(position).getSchoolcode()), mPickSchoolListModelArray.get(position).getAddress(), mPickSchoolListModelArray.get(position).getPhone(), mPickSchoolListModelArray.get(position).getAedcontactemail(), mPickSchoolListModelArray.get(position).getPrincemail(), mPickSchoolListModelArray.get(position).getDrillid(), position,mPickSchoolListModelArray.get(position).getContactphone(),mPickSchoolListModelArray.get(position).getState(),mPickSchoolListModelArray.get(position).getCity(),mPickSchoolListModelArray.get(position).getZip());
                        }
                    }
                }
            });

            tvDiscard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final Dialog dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_remove_drill);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)));
                    TextView textView = (TextView) dialog.findViewById(R.id.dialogschoolname);
                    textView.setText(mPickSchoolListModelArray.get(position).getSchoolname());
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

                           ArrayList<Schoolinfomodel> list=listops.getdrilllist();
                            list.remove(position);
                            listops.putdrilllist(list);
                            //tell the
                            if (mContext instanceof TabsActivity) {

                                DrillsFragment fm=new DrillsFragment();
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
