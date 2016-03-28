package com.emergencyskills.doe.aed.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.emergencyskills.doe.aed.R;
import com.emergencyskills.doe.aed.models.PickSchoolModel;

import java.util.ArrayList;

/**
 * Created by naveen on 10/10/2015.
 */
public class UpdateRespondersDialogAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<PickSchoolModel> mPickSchoolListModelArray;
    public UpdateRespondersDialogAdapter(Context context, ArrayList<PickSchoolModel> pickSchoolListModelArray)
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.adapter_for_update_responders_dialog, parent, false);
        TextView tvSchoolCodeValue= (TextView) convertView.findViewById(R.id.tvSchoolCodeValue);

        tvSchoolCodeValue.setText(mPickSchoolListModelArray.get(position).getSchoolID());


        return convertView;
    }
}
