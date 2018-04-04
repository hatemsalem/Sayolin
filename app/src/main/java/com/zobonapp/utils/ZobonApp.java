package com.zobonapp.utils;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RemoteViews;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.zobonapp.BuildConfig;
import com.zobonapp.R;
import com.zobonapp.SplashActivity;
import com.zobonapp.manager.DataManager;
import com.zobonapp.manager.ManagerRegistry;
import com.zobonapp.manager.impl.MockManagerRegistry;
import com.zobonapp.service.UpdateService;

import java.io.File;
import java.util.Locale;

import static com.zobonapp.utils.QueryPreferences.ARABIC;
import static com.zobonapp.utils.QueryPreferences.ENGLISH;

/**
 * Created by hasalem on 11/26/2017.
 */

public class ZobonApp extends Application implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String TAG = ZobonApp.class.getSimpleName();
    private String lang;
    private static ZobonApp appInstance;
    private ManagerRegistry registry;
    private SharedPreferences prefs;
    private NotificationManager notificationManager;
    private Picasso picasso;
    private static String resolution="mdpi";
    private IntentHelper intentHelper;

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

    public static IntentHelper getIntentHelper()
    {
        return  ZobonApp.getContext().intentHelper;
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
        attachBaseContext(getBaseContext());
//        Context newBase=getBaseContext();
//        Locale locale = new Locale(getLang());
//        Locale.setDefault(locale);
//        Configuration localConfiguration = newBase.getResources().getConfiguration();
//        localConfiguration.setLayoutDirection(locale);
//        localConfiguration.setLocale(locale);
//        newBase.getResources().updateConfiguration(localConfiguration, newBase.getResources().getDisplayMetrics());


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
        PreferenceManager.setDefaultValues(this,R.xml.settings,false);
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
        picasso = new Picasso.Builder(this).indicatorsEnabled(true).memoryCache(new LruCache(5000000)).downloader(new OkHttp3Downloader(getFilesDir(), 25000000)).build();
        intentHelper=new IntentHelper();
//        picasso = new Picasso.Builder(this).indicatorsEnabled(true).memoryCache(new LruCache(5000000)).build();
        //        PicassoTools.clearCache(picasso);
        //        deleteDirectoryTree(getCacheDir());
        Picasso.setSingletonInstance(picasso);
        createNotificationChannel();
        showNotificationCenter();
        getPrefs().registerOnSharedPreferenceChangeListener(this);
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel()
    {
        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        String id="ZobonApp";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null)
            {
                mChannel = new NotificationChannel(id, id, importance);
                mChannel.setDescription(id);
//                mChannel.enableVibration(true);
//                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }
        }
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
        if(QueryPreferences.isShowShortcutCenter())
        {
            NotificationCompat.Builder builder =new NotificationCompat.Builder(this,"ZobonApp");
//            Notification.Builder builder = new Notification.Builder(this);
            builder.setOngoing(true).setSmallIcon(R.drawable.call_now).setContentTitle("Notification Center").setContentText("Details");
            builder.setContent(getComplexNotificationView());
//            builder.setChannelId("abc");

            //                .setContent(getComplexNotificationView());
            //        builder.setSmallIcon(R.drawable.call_now);
            //                .setTicker("Hello Title");
            //                .setContentText("Hello Text")
            //                .setDefaults(Notification.DEFAULT_ALL)
            //                .setOngoing(true)
            //                .setContent(getComplexNotificationView())
            //                .setAutoCancel(true);
            Notification notification = builder.build();
            notificationManager.notify(0, notification);
        }
        else
        {
            notificationManager.cancel(0);
        }

    }

    private RemoteViews getComplexNotificationView()
    {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification_center);
        notificationView.setTextViewText(R.id.myNotificationTitle,"Click to start ZobonApp");
        notificationView.setImageViewResource(R.id.notificationIcon,R.mipmap.ic_launcher);
        notificationView.setImageViewResource(R.id.calculatorIcon,R.mipmap.ic_launcher);

        notificationView.setOnClickPendingIntent(R.id.notificationIcon, PendingIntent.getActivity(this,0,new Intent(this, SplashActivity.class),PendingIntent.FLAG_UPDATE_CURRENT));
//        notificationView.setOnClickPendingIntent(R.id.calculatorIcon, PendingIntent.getActivity(this,0,new Intent(this, SplashActivity.class),PendingIntent.FLAG_UPDATE_CURRENT));
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName("com.android.calculator2","com.android.calculator2.Calculator"));
        notificationView.setOnClickPendingIntent(R.id.calculatorIcon, PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT));
        notificationView.setOnClickPendingIntent(R.id.myNotificationTitle, PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        notificationView.setOnClickPendingIntent(R.id.notificationIcon, PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT));
//        startActivity(intent);


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
    public static int calculateColumns(int columnWidth)
    {
        int columns=(int)(160f/getContext().getResources().getDisplayMetrics().densityDpi*getContext().getResources().getDisplayMetrics().widthPixels/columnWidth);
        if(QueryPreferences.isShowLargeDisplay())
            columns=columns-columns/4;
//            columns--;
        return columns;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        switch (key)
        {
            case "showShortcutCenter":
                showNotificationCenter();
        }
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        if(appInstance!=null)
        {
            Log.d(TAG,"appInstance is not null");
            Context newBase=getBaseContext();
            Locale locale = new Locale(getLang());
            Locale.setDefault(locale);
            Configuration localConfiguration = newBase.getResources().getConfiguration();
            localConfiguration.setLayoutDirection(locale);
            localConfiguration.setLocale(locale);
            newBase.getResources().updateConfiguration(localConfiguration, newBase.getResources().getDisplayMetrics());
        }
        else
            super.attachBaseContext(base);
    }
    public static String getResoultionPath()
    {
        return resolution;
    }
    public static String getAssetPath(String id)
    {
        return String.format("%s/resources/%s/%s.webp",BuildConfig.BASE_URL,resolution,id);
    }
}
