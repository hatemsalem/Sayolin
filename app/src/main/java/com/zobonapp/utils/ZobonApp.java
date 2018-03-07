package com.zobonapp.utils;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RemoteViews;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoTools;
import com.zobonapp.MainActivity;
import com.zobonapp.R;
import com.zobonapp.SplashActivity;
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
    private static final String TAG = ZobonApp.class.getSimpleName();
    private String lang;
    private static ZobonApp appInstance;
    private ManagerRegistry registry;
    private SharedPreferences prefs;
    private Picasso picasso;
    private static String resolution="ldpi";


    public synchronized SharedPreferences getPrefs()
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
        if (ARABIC.equals(getLang()))
        {
            lang = ENGLISH;

        } else
        {
            lang = ARABIC;
        }
        QueryPreferences.setLanguage(lang);
        return lang;
    }

    public String getLang()
    {
        if (lang == null)
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                lang = LocaleList.getDefault().get(0).getLanguage();
            else lang = getResources().getConfiguration().locale.getLanguage();
        }
        return lang;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        initializeResoution();
        Log.d(TAG, "ZobonApp created");
        appInstance = this;
        registry = new MockManagerRegistry();
        prefs = PreferenceManager.getDefaultSharedPreferences(appInstance.getBaseContext());
        if (!QueryPreferences.isInitialized())
        {
            UpdateService.startActionInitialize(this);
        }
        Log.d(TAG, "To set the alarm on");
        if (!UpdateService.isServiceAlarmOn(this))
        {
            Log.d(TAG, "Alarm is off, and it will be settled");
            UpdateService.setServiceAlarm(this, true);
            Log.d(TAG, "Alarm settled");
        }
        lang = QueryPreferences.getLanguage();
        picasso = new Picasso.Builder(this).indicatorsEnabled(false).memoryCache(new LruCache(5000000)).downloader(new OkHttp3Downloader(getFilesDir(), 25000000)).build();
        //        PicassoTools.clearCache(picasso);
        //        deleteDirectoryTree(getCacheDir());
        Picasso.setSingletonInstance(picasso);
        showNotificationCenter();
    }

    public static void deleteDirectoryTree(File fileOrDirectory)
    {
        if (fileOrDirectory.isDirectory())
        {
            for (File child : fileOrDirectory.listFiles())
            {
                deleteDirectoryTree(child);
            }
        }

        fileOrDirectory.delete();
    }

    private void showNotificationCenter()
    {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setOngoing(true).setContent(getComplexNotificationView());
        Notification.Builder builder = new Notification.Builder(this);
        builder.setOngoing(true)
                .setSmallIcon(R.drawable.call_now)
                .setContentTitle("Notification Center")
                .setContentText("Details")

        ;
        builder.setContent(getComplexNotificationView());
//                .setContent(getComplexNotificationView());
//        builder.setSmallIcon(R.drawable.call_now);
//                .setTicker("Hello Title");
//                .setContentText("Hello Text")
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setOngoing(true)
//                .setContent(getComplexNotificationView())
//                .setAutoCancel(true);
        Notification notification=builder.build();
        notificationManager.notify(0,notification);
    }

    private RemoteViews getComplexNotificationView()
    {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification_center);
        notificationView.setTextViewText(R.id.myNotificationTitle,"Click to start ZobonApp");
        notificationView.setImageViewResource(R.id.notificationIcon,R.mipmap.ic_launcher);

        notificationView.setOnClickPendingIntent(R.id.notificationIcon, PendingIntent.getActivity(this,0,new Intent(this, SplashActivity.class),PendingIntent.FLAG_UPDATE_CURRENT));
        return notificationView;
    }
    private void initializeResoution()
    {
        switch(getResources().getDisplayMetrics().densityDpi)
        {
            case DisplayMetrics.DENSITY_LOW:
                resolution= "ldpi";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                resolution= "mdpi";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                resolution= "hdpi";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                resolution= "xhdpi";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                resolution= "xxhdpi";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                resolution= "xxxhdpi";
                break;
            default:
                resolution= "mdpi";
                break;
        }
    }
    public static String getResoultionPath()
    {
        return resolution;
    }
}
