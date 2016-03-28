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
import android.widget.Toast;

import com.emergencyskills.doe.aed.R;
import com.emergencyskills.doe.aed.utils.CommonUtilities;
import com.emergencyskills.doe.aed.utils.Constants;
import com.emergencyskills.doe.aed.utils.listops;
import com.emergencyskills.doe.aed.adapters.DrillsAdapter;
import com.emergencyskills.doe.aed.adapters.NewInstallsAdapter;
import com.emergencyskills.doe.aed.models.Schoolinfomodel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by naveen on 11/18/2015.
 */

public class NewInstallsFragment extends Fragment {

        private ListView lvNewInstalls;
        private TextView tvPageName;
        private boolean isdo;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_drill, container, false);
           // Toast.makeText(getActivity(), "new install", Toast.LENGTH_LONG).show();
            lvNewInstalls = (ListView) rootView.findViewById(R.id.lvDrills);
            tvPageName = (TextView) rootView.findViewById(R.id.tvPageName);
            tvPageName.setText("NEW INSTALLS");

            listops listops=new listops(getActivity());
            ArrayList<Schoolinfomodel> list=listops.getinstalllist();


            if(list.size()>0) {
                lvNewInstalls.setAdapter(new NewInstallsAdapter(getActivity(), list));



                ListAdapter adapter = lvNewInstalls.getAdapter();

                if (adapter != null) {


                    ViewGroup vg = lvNewInstalls;
                    int totalHeight = 0;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        View listItem = adapter.getView(i, null, vg);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight();
                    }

                    ViewGroup.LayoutParams par = lvNewInstalls.getLayoutParams();
                    par.height = totalHeight + (lvNewInstalls.getDividerHeight() * (adapter.getCount() - 1));
                    lvNewInstalls.setLayoutParams(par);
                    lvNewInstalls.requestLayout();

                }

            }
            else
            {
                TextView txt=(TextView)rootView.findViewById(R.id.txtnoitemdrill);
                txt.setVisibility(View.VISIBLE);
            }
            return rootView;
        }
    }

