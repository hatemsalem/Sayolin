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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RemoteViews;

import com.alexvasilkov.events.Events;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.zobonapp.BuildConfig;
import com.zobonapp.R;
import com.zobonapp.SplashActivity;
import com.zobonapp.flashlight.FlashLightActivity;
import com.zobonapp.gallery.utils.FlickrApi;
import com.zobonapp.manager.DataManager;
import com.zobonapp.manager.ManagerRegistry;
import com.zobonapp.manager.impl.MockManagerRegistry;
import com.zobonapp.service.UpdateService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    public static SharedPreferences getPrefs()
    {
        validateState();
        return appInstance.prefs;
    }


    public static ZobonApp getContext()
    {
        return appInstance;
    }

    public static DataManager getDataManager()
    {
        validateState();
        return appInstance.registry.getDataManager();
    }

    public static Picasso getPicasso()
    {
        validateState();
        return appInstance.picasso;
    }

    public static IntentHelper getIntentHelper()
    {
        return  ZobonApp.getContext().intentHelper;
    }

    public static String toggleLang()
    {
        if (ARABIC.equals(getLang()))
        {
            appInstance.lang = ENGLISH;

        } else
        {
            appInstance.lang = ARABIC;
        }
        QueryPreferences.setLanguage(appInstance.lang);
        appInstance.attachBaseContext(appInstance.getBaseContext());
//        Context newBase=getBaseContext();
//        Locale locale = new Locale(getLang());
//        Locale.setDefault(locale);
//        Configuration localConfiguration = newBase.getResources().getConfiguration();
//        localConfiguration.setLayoutDirection(locale);
//        localConfiguration.setLocale(locale);
//        newBase.getResources().updateConfiguration(localConfiguration, newBase.getResources().getDisplayMetrics());


        return appInstance.lang;
    }
    private static void validateState()
    {
        if(appInstance==null)
            throw new IllegalStateException("ZobonApp not initialezed yet");
    }
    public static String getLang()
    {
        validateState();
        if (appInstance.lang == null)
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                appInstance.lang = LocaleList.getDefault().get(0).getLanguage();
            else appInstance.lang = appInstance.getResources().getConfiguration().locale.getLanguage();
        }
        return appInstance.lang;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();
        //TODO: to be removed
//        Events.register(FlickrApi.class);


        initializeResoution();
        PreferenceManager.setDefaultValues(this,R.xml.settings,false);
        Log.d(TAG, "ZobonApp created");

        registry = new MockManagerRegistry();
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //        picasso = new Picasso.Builder(this).indicatorsEnabled(true).memoryCache(new LruCache(20000000)).downloader(new OkHttp3Downloader(getFilesDir(), 25000000)).build();
        picasso = new Picasso.Builder(this).indicatorsEnabled(true).memoryCache(new LruCache(this)).downloader(new OkHttp3Downloader(getFilesDir(), 25000000)).build();

        //        picasso = new Picasso.Builder(this).indicatorsEnabled(true).memoryCache(new LruCache(5000000)).build();
        //        PicassoTools.clearCache(picasso);
        //        deleteDirectoryTree(getCacheDir());
        Picasso.setSingletonInstance(picasso);

        intentHelper=new IntentHelper(this);


        appInstance = this;


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
            NotificationChannel mChannel = notificationManager.getNotificationChannel(id);
            if (mChannel == null)
            {
                mChannel = new NotificationChannel(id, id, NotificationManager.IMPORTANCE_HIGH);
                mChannel.setDescription(id);
                mChannel.setSound(null,null);
                mChannel.setShowBadge(false);
                mChannel.setVibrationPattern(null);
                mChannel.enableVibration(false);
//                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }
        }
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
            builder.setOngoing(true).setSmallIcon(R.drawable.ic_z).setContentTitle("Notification Center").setContentText("Details").setPriority(NotificationCompat.PRIORITY_HIGH);
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
//        notificationView.setTextViewText(R.id.myNotificationTitle,"ZobonApp");
        notificationView.setImageViewResource(R.id.notificationIcon,R.drawable.zobon);
        notificationView.setImageViewResource(R.id.calculatorIcon,R.drawable.calculator);
        notificationView.setImageViewResource(R.id.FlashIcon,R.drawable.flash);
        notificationView.setImageViewResource(R.id.QRCodeIcon,R.drawable.qrcode);
        Intent intent=new Intent(this, SplashActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationView.setOnClickPendingIntent(R.id.notificationIcon, PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT));
        intent=new Intent(this, FlashLightActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationView.setOnClickPendingIntent(R.id.FlashIcon, PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT));
//        notificationView.setOnClickPendingIntent(R.id.calculatorIcon, PendingIntent.getActivity(this,0,new Intent(this, SplashActivity.class),PendingIntent.FLAG_UPDATE_CURRENT));
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setComponent(new ComponentName("com.android.calculator2","com.android.calculator2.Calculator"));


        //        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_APP_CALCULATOR);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent=getCalculatorIntent();
        if(intent!=null)
        {
            notificationView.setOnClickPendingIntent(R.id.calculatorIcon, PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
        else
        {
            //TODO: Disable the calculator
        }
//        notificationView.setOnClickPendingIntent(R.id.myNotificationTitle, PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT));


        //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        notificationView.setOnClickPendingIntent(R.id.notificationIcon, PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT));
//        startActivity(intent);


        return notificationView;
    }
    private Intent getCalculatorIntent()
    {
        String packageName="com.android.calculator2";
        PackageManager pm=getPackageManager();
        List<PackageInfo> packs=pm.getInstalledPackages(0);

        for (PackageInfo pi:packs)
        {


            if(pi.packageName.toLowerCase().contains("cal") )
            {


                String appName=pi.applicationInfo.loadLabel(pm).toString();
                try
                {
                    Resources res=pm.getResourcesForApplication(pi.applicationInfo);
                    Configuration conf= res.getConfiguration();
                    conf.setLocale(Locale.US);
                    res.updateConfiguration(conf,res.getDisplayMetrics());
                    appName=res.getString(pi.applicationInfo.labelRes);
                    appName=appName.toLowerCase();
                } catch (PackageManager.NameNotFoundException |Resources.NotFoundException e)
                {
                    //TODO: Nothing
                }
                appName=appName.toLowerCase();



                if(appName.contains("calculator")||appName.contains("حاسبة"))
                {
                    packageName = pi.packageName;
                    if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) break;
                }
            }
        }
        return pm.getLaunchIntentForPackage(packageName);
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
    public static int calculateColumnWidth(int columnsCount)
    {
        return (int)(getContext().getResources().getDisplayMetrics().widthPixels/columnsCount)-30;
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
