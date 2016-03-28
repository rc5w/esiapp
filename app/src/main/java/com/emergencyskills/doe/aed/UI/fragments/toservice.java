package com.emergencyskills.doe.aed.UI.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.emergencyskills.doe.aed.R;
import com.emergencyskills.doe.aed.utils.Constants;
import com.emergencyskills.doe.aed.utils.listops;
import com.emergencyskills.doe.aed.adapters.DrillsAdapter;
import com.emergencyskills.doe.aed.adapters.NewInstallsAdapter;
import com.emergencyskills.doe.aed.adapters.ServiceAdapter;
import com.emergencyskills.doe.aed.models.Schoolinfomodel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Karan on 12/3/2015.
 */
public class toservice extends Fragment {

    private ListView lvservice;

    private boolean isdo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_toservice, container, false);
        // Toast.makeText(getActivity(), "service calls", Toast.LENGTH_LONG).show();

        lvservice = (ListView) rootView.findViewById(R.id.lvservices);

        //Toast.makeText(getActivity(),String.valueOf(sc.getSize()),Toast.LENGTH_SHORT).show();

       listops listops=new listops(getActivity());
        ArrayList<Schoolinfomodel> list=listops.getservicelist();


        if(list.size()>0) {
            lvservice.setAdapter(new ServiceAdapter(getActivity(),list));

            ListAdapter adapter = lvservice.getAdapter();

            if (adapter != null) {


                ViewGroup vg = lvservice;
                int totalHeight = 0;
                for (int i = 0; i < adapter.getCount(); i++) {
                    View listItem = adapter.getView(i, null, vg);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }

                ViewGroup.LayoutParams par = lvservice.getLayoutParams();
                par.height = totalHeight + (lvservice.getDividerHeight() * (adapter.getCount() - 1));
                lvservice.setLayoutParams(par);
                lvservice.requestLayout();

            }
        }
        else
        {
            TextView txt=(TextView)rootView.findViewById(R.id.txtnoitemservice);
            txt.setVisibility(View.VISIBLE);
        }

        return rootView;
    }
}
