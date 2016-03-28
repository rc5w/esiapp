package com.emergencyskills.doe.aed.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.emergencyskills.doe.aed.models.Aedsdatamodel;
import com.emergencyskills.doe.aed.models.PendingUploadModel;
import com.emergencyskills.doe.aed.models.PickSchoolModel;
import com.emergencyskills.doe.aed.models.Schoolinfomodel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Karan on 12/24/2015.
 */
public class listops {
    Context mcontext;
    SharedPreferences prefs;


    public listops(Context context)
    {
        mcontext=context;
        prefs= mcontext.getSharedPreferences("prefs", Context.MODE_PRIVATE|Context.MODE_MULTI_PROCESS);

    }

    //tinyDb.putListObjectSchool("downloaddata",schoolinfomodellist);
    Gson gson = new Gson();

    public ArrayList<Schoolinfomodel> getschoollist(String listname )
    {

        SharedPreferences.Editor editor=prefs.edit();

        Type type = new TypeToken<ArrayList<Schoolinfomodel>>(){}.getType();
        String lists=prefs.getString(listname,"");
        Gson gson=new Gson();
        ArrayList<Schoolinfomodel> schoolinfomodels;
        if(!lists.equals("")) {
            schoolinfomodels = gson.fromJson(lists, type);
        }
        else
        {
            schoolinfomodels=new ArrayList<Schoolinfomodel>();
        }
        return  schoolinfomodels;
    }

    public ArrayList<PendingUploadModel> getpendinglist( )
    {

        SharedPreferences.Editor editor=prefs.edit();

        Type type = new TypeToken<ArrayList<PendingUploadModel>>(){}.getType();
        String lists=prefs.getString("pending","");
        Gson gson=new Gson();


        ArrayList<PendingUploadModel> schoolinfomodels;
        if(!lists.equals("")) {
            schoolinfomodels = gson.fromJson(lists, type);
        }
        else
        {
            schoolinfomodels=new ArrayList<PendingUploadModel>();
        }
        return  schoolinfomodels;
    }
    public void putschoollist(String listname, ArrayList<Schoolinfomodel> arrayList)
    {

        SharedPreferences.Editor editor=prefs.edit();
        String jsonCars = gson.toJson(arrayList);
        editor.putString(listname,jsonCars);
        editor.commit();
    }
    public void putpendinglist( ArrayList<PendingUploadModel> arrayList)
    {

        SharedPreferences.Editor editor=prefs.edit();
        String jsonCars = gson.toJson(arrayList);
        editor.putString("pending",jsonCars);
        editor.commit();
    }

    public ArrayList<Schoolinfomodel> getdrilllist( )
    {

        SharedPreferences.Editor editor=prefs.edit();

        Type type = new TypeToken<ArrayList<Schoolinfomodel>>(){}.getType();
        String lists=prefs.getString("drilllist","");
        Gson gson=new Gson();


        ArrayList<Schoolinfomodel> schoolinfomodels;
        if(!lists.equals("")) {
            schoolinfomodels = gson.fromJson(lists, type);
        }
        else
        {
            schoolinfomodels=new ArrayList<Schoolinfomodel>();
        }
        return  schoolinfomodels;
    }
    public void putdrilllist( ArrayList<Schoolinfomodel> arrayList)
    {

        SharedPreferences.Editor editor=prefs.edit();
        String jsonCars = gson.toJson(arrayList);
        editor.putString("drilllist",jsonCars);
        editor.commit();
    }

    public ArrayList<String> getZipList( )
    {

        SharedPreferences.Editor editor=prefs.edit();

        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        String lists=prefs.getString("ziplist","");
        Gson gson=new Gson();


        ArrayList<String> ziplist;
        if(!lists.equals("")) {
            ziplist = gson.fromJson(lists, type);
        }
        else
        {
            ziplist=new ArrayList<String>();
        }
        return  ziplist;
    }
    public void putZipList( ArrayList<String> arrayList)
    {

        SharedPreferences.Editor editor=prefs.edit();
        String jsonCars = gson.toJson(arrayList);
        editor.putString("ziplist",jsonCars);
        editor.commit();
    }

    public ArrayList<Schoolinfomodel> getservicelist( )
    {

        SharedPreferences.Editor editor=prefs.edit();

        Type type = new TypeToken<ArrayList<Schoolinfomodel>>(){}.getType();
        String lists=prefs.getString("servicelist","");
        Gson gson=new Gson();


        ArrayList<Schoolinfomodel> schoolinfomodels;
        if(!lists.equals("")) {
            schoolinfomodels = gson.fromJson(lists, type);
        }
        else
        {
            schoolinfomodels=new ArrayList<Schoolinfomodel>();
        }
        return  schoolinfomodels;
    }
    public void putservicelist( ArrayList<Schoolinfomodel> arrayList)
    {

        SharedPreferences.Editor editor=prefs.edit();
        String jsonCars = gson.toJson(arrayList);
        editor.putString("servicelist",jsonCars);
        editor.commit();
    }
    public ArrayList<Schoolinfomodel> getinstalllist( )
    {

        SharedPreferences.Editor editor=prefs.edit();

        Type type = new TypeToken<ArrayList<Schoolinfomodel>>(){}.getType();
        String lists=prefs.getString("installlist","");
        Gson gson=new Gson();

CommonUtilities.logMe( "getting lists: " + lists );
        ArrayList<Schoolinfomodel> schoolinfomodels;
        if(!lists.equals("")) {
            schoolinfomodels = gson.fromJson(lists, type);
        }
        else
        {
            schoolinfomodels=new ArrayList<Schoolinfomodel>();
        }
        return  schoolinfomodels;
    }
    public void putinstallllist( ArrayList<Schoolinfomodel> arrayList)
    {

        SharedPreferences.Editor editor=prefs.edit();
        String jsonCars = gson.toJson(arrayList);
CommonUtilities.logMe( "putting install lists: " + jsonCars );
        editor.putString("installlist",jsonCars);
        editor.commit();
    }
    public void putaeddatalist( ArrayList<Aedsdatamodel> arrayList)
    {
//        System.err.println( "before saving" );
        SharedPreferences.Editor editor=prefs.edit();
        String jsonCars = gson.toJson(arrayList);
        editor.putString("aedlist",jsonCars);
        editor.commit();
//        System.err.println( "after saving" );
    }

    public ArrayList<Aedsdatamodel> getaeddatalist( )
    {

        SharedPreferences.Editor editor=prefs.edit();

        Type type = new TypeToken<ArrayList<Aedsdatamodel>>(){}.getType();
        String lists=prefs.getString("aedlist","");
        Gson gson=new Gson();


        ArrayList<Aedsdatamodel> schoolinfomodels;
        if(!lists.equals("")) {
            schoolinfomodels = gson.fromJson(lists, type);
        }
        else
        {
            schoolinfomodels=new ArrayList<Aedsdatamodel>();
        }
        return  schoolinfomodels;
    }

    public void clearaedadatalist(Context mcontext)
    {
        listops listops=new listops(mcontext);
        if(listops.getaeddatalist().size()>0)
        {
            listops.putaeddatalist(new ArrayList<Aedsdatamodel>());
        }

    }



    public void putpickschoollist( ArrayList<PickSchoolModel> arrayList)
    {

        SharedPreferences.Editor editor=prefs.edit();
        String jsonCars = gson.toJson(arrayList);
        editor.putString("pickschool",jsonCars);
        editor.commit();
    }
    public ArrayList<PickSchoolModel> getpickschoollist( )
    {

        SharedPreferences.Editor editor=prefs.edit();

        Type type = new TypeToken<ArrayList<PickSchoolModel>>(){}.getType();
        String lists=prefs.getString("pickschool","");
        Gson gson=new Gson();


        ArrayList<PickSchoolModel> schoolinfomodels;
        if(!lists.equals("")) {
            schoolinfomodels = gson.fromJson(lists, type);
        }
        else
        {
            schoolinfomodels=new ArrayList<PickSchoolModel>();
        }
        return  schoolinfomodels;
    }

    public void putString(String key,String value)
    {
        SharedPreferences.Editor editor=prefs.edit();

        editor.putString(key,value);
        editor.commit();
    }

    public String getString(String key)
    {
        return prefs.getString(key,"");
    }

    public void putInt(String key,int value)
    {
        SharedPreferences.Editor editor=prefs.edit();

        editor.putInt(key,value);
        editor.commit();
    }

    public int getInt(String key)
    {
        return prefs.getInt(key,1);
    }



}
