package com.zobonapp.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoTools;
import com.zobonapp.manager.DataManager;
import com.zobonapp.manager.ManagerRegistry;
import com.zobonapp.manager.impl.MockManagerRegistry;
import com.zobonapp.service.UpdateService;

import java.io.File;

import static com.zobonapp.utils.QueryPreferences.ARABIC;
import static com.zobonapp.utils.QueryPreferences.ENGLISH;

/**
 * Created by hasalem on 11/26/2017.
 */

public class ZobonApp extends Application
{
    private static final String TAG=ZobonApp.class.getSimpleName();
    private String lang;
    private static ZobonApp appInstance;
    private ManagerRegistry registry;
    private SharedPreferences prefs;
    private Picasso picasso;

    public synchronized SharedPreferences  getPrefs()
    {
        return prefs;
    }


    public static ZobonApp getContext()
    {
        return appInstance;
    }
    public DataManager getDataManager()
    {
        return registry.getDataManager();
    }
    public Picasso getPicasso()
    {
        return picasso;
    }
    public String toggleLang()
    {
        if(ARABIC.equals(getLang()))
        {
            lang=ENGLISH;

        }
        else
        {
            lang=ARABIC;
        }
        QueryPreferences.setLanguage(lang);
        return lang;
    }

    public String getLang()
    {
        if(lang==null)
        {

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
                lang=LocaleList.getDefault().get(0).getLanguage();
            else
                lang=getResources().getConfiguration().locale.getLanguage();
        }
        return lang;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d(TAG,"ZobonApp created");
        appInstance=this;
        registry=new MockManagerRegistry();
        prefs=PreferenceManager.getDefaultSharedPreferences(appInstance.getBaseContext());
        if(!QueryPreferences.isInitialized())
        {
            UpdateService.startActionInitialize(this);
        }
        Log.d(TAG,"To set the alarm on");
        if(!UpdateService.isServiceAlarmOn(this))
        {
            Log.d(TAG,"Alarm is off, and it will be settled");
            UpdateService.setServiceAlarm(this,true);
            Log.d(TAG,"Alarm settled");
        }
        lang=QueryPreferences.getLanguage();
        picasso=new Picasso.Builder(this).indicatorsEnabled(true).memoryCache(new LruCache(24000000)).downloader(new OkHttp3Downloader(getCacheDir(), 250000000)).build();
        PicassoTools.clearCache(picasso);
//        deleteDirectoryTree(getCacheDir());
        Picasso.setSingletonInstance(picasso);
    }
    public static void deleteDirectoryTree(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteDirectoryTree(child);
            }
        }

        fileOrDirectory.delete();
    }
}
