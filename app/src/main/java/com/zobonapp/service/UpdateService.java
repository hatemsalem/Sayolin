package com.zobonapp.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.commonsware.cwac.security.ZipUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.zobonapp.BuildConfig;
import com.zobonapp.R;
import com.zobonapp.manager.InitializationEvent;
import com.zobonapp.utils.DataCollection;
import com.zobonapp.utils.QueryPreferences;
import com.zobonapp.utils.ZobonApp;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by hasalem on 12/11/2017.
 */

public class UpdateService extends IntentService
{

    public static final String TAG=UpdateService.class.getSimpleName();

    private static final String UPDATE_FILE_NAME="update.zip";
    public static final String UPDATE_BASE_DIR="updates";
    private static final long POLL_INTERVAL_MS= TimeUnit.MINUTES.toMillis(1);
    private static final String ACTION_INITIALIZE = "com.zobonapp.service.action.INITIALIZE";
    private static final String ACTION_CHECK_UPDATE = "com.zobonapp.service.action.CHECK_UPDATE";
    private boolean inInitialize =false;
    private boolean inUpdate=false;
    public UpdateService()
    {
        super(TAG);
    }


    public static void startActionInitialize(Context context)
    {
        Intent intent = new Intent(context, UpdateService.class);
        intent.setAction(ACTION_INITIALIZE);
        context.startService(intent);
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        if(intent!=null)
        {
            switch (intent.getAction())
            {
                case ACTION_INITIALIZE:
                    initialize();
                    break;
                case ACTION_CHECK_UPDATE:
                    update();
                    break;
            }

        }
    }
    private String getUpdatePath()
    {
        return String.format(Locale.US,"%s/updates/ver-%s/%d",BuildConfig.BASE_URL,BuildConfig.VERSION_CODE,QueryPreferences.getLatestUpdate());
//        return String.format(Locale.US,"%s/updates/ver-%s/%d",BuildConfig.BASE_URL,BuildConfig.VERSION_CODE,0);
    }

    protected void update()
    {
        ////////////////////TODO: to be removed/////////

//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        NotificationCompat.Builder builder =new NotificationCompat.Builder(this,"ZobonApp");
//        builder.setSmallIcon(R.drawable.ic_z).setContentTitle("Update").setContentText("Update @:"+new Date());
//        Notification notification = builder.build();
//        notificationManager.notify(2, notification);

        ////////////////////////////////////////////
        Log.i(TAG,"Check update will be here");
        synchronized (this)
        {
            if (QueryPreferences.isInitialized() && inUpdate==false)
                inUpdate=true;
            else
                return;
        }
        try
        {

            int step=QueryPreferences.getUpdateStep();

            if(step<0)
            {
                File update=download(getUpdatePath());
                if(update!=null)
                {
                    File updateDir = new File(getFilesDir(), UPDATE_BASE_DIR);
                    ZipUtils.delete(updateDir);
                    updateDir.mkdirs();
                    ZipUtils.unzip(update, updateDir);
                    update.delete();
                    QueryPreferences.setUpdateStep(step = 0);
                }
            }
            if(step==0)
            {
                File file=new File(getFilesDir(),UPDATE_BASE_DIR+"/datanew.json");
                if(file.exists())
                {
                    InputStream is= new FileInputStream(file);
                    int steps=updateBasicData(is);
                    is.close();
                    for(int i=0;i<steps;i++)
                    {
                        File stepFile=new File(getFilesDir(),String.format(Locale.US,"%s/n_step%d.json",UPDATE_BASE_DIR,i));
                        if(stepFile.exists())
                        {
                            InputStream stepStream=new FileInputStream(stepFile);
                            updateContactsData(stepStream);
                            stepFile.delete();
                        }
                    }

                    file.delete();
                }


                QueryPreferences.setUpdateStep(step=1);
            }
            if(step==1)
            {
                File file=new File(getFilesDir(),UPDATE_BASE_DIR+"/dataupdate.json");
                if(file.exists())
                {
                    InputStream is= new FileInputStream(file);
                    int steps=updateBasicData(is);
                    is.close();
                    for(int i=0;i<steps;i++)
                    {
                        File stepFile=new File(getFilesDir(),String.format(Locale.US,"%s/u_step%d.json",UPDATE_BASE_DIR,i));
                        if(stepFile.exists())
                        {
                            InputStream stepStream=new FileInputStream(stepFile);
                            updateContactsData(stepStream);
                            stepFile.delete();
                        }
                    }

                    file.delete();
                }


                QueryPreferences.setUpdateStep(step=2);
            }
            if(step==2)
            {
                File file=new File(getFilesDir(),UPDATE_BASE_DIR+"/datadelete.json");
                if(file.exists())
                {
                    InputStream is= new FileInputStream(file);
                    deleteItems(is);
                    is.close();
                    file.delete();
                }


                QueryPreferences.setInitializeStep(step=-1);
            }


//            EventBus.getDefault().post(new BookUpdateEvent());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            inUpdate=false;
            QueryPreferences.setUpdateStep(-1);
        }



    }

    private void deleteItems(InputStream is) throws IOException
    {

        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        DataCollection dataCollection = gson.fromJson(reader, DataCollection.class);
        reader.close();
        ZobonApp.getDataManager().deleteItems(dataCollection.getDeletedEntities());
        ZobonApp.getDataManager().deleteCategories(dataCollection.getDeletedCategories());
        ZobonApp.getDataManager().deleteOffers(dataCollection.getDeletedOffers());

    }

    private File download(String url) throws IOException
    {
        File output=new File(getFilesDir(),UPDATE_FILE_NAME);
        if(output.exists())
            output.delete();
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(60,TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS)
                .build();
//        OkHttpClient client=new OkHttpClient.Builder().build();


        RequestBody reqbody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{}");
        Request request=new Request.Builder()
                .cacheControl(CacheControl.FORCE_NETWORK)
                .url(url).post(reqbody).build();
//        Request request=new Request.Builder()
//                .cacheControl(CacheControl.FORCE_NETWORK)
//                .url(url).build();

        Response response=client.newCall(request).execute();
//        InputStream is=response.body().byteStream();
//        BufferedInputStream input=new BufferedInputStream(is);
//        OutputStream outputStream=new FileOutputStream(output);
//        byte[] data=new byte[1024*100];
//        long total=0;
//        int count;
//        while((count=input.read(data))!=-1)
//        {
//            total+=count;
//            outputStream.write(data,0,count);
//        }
//        outputStream.flush();
//        outputStream.close();
//        input.close();
        if(response.code()!=200)
            return null;
        Log.d(TAG,"Response:"+response.code());
        BufferedSink sink= Okio.buffer(Okio.sink(output));
        sink.writeAll(response.body().source());
        sink.close();
        return output;
    }

    protected void initialize()
    {
        synchronized (this)
        {
            if(inInitialize ==true)
                return;;
            inInitialize =true;
        }

        try
        {
            int step=QueryPreferences.getInitializeStep();
            if(step<0)
            {
                InputStream is= ZobonApp.getContext().getAssets().open("misc/datanew.json");
                QueryPreferences.setTotalSteps(updateBasicData(is));
                is.close();
                QueryPreferences.setInitializeStep(step=0);

                EventBus.getDefault().post(new InitializationEvent(InitializationEvent.Status.COMPLETED));
            }




            int totalSteps=QueryPreferences.getTotalSteps();
            if(step>=0&&step<totalSteps)
            {

                for(int i=step;i<totalSteps;i++)
                {
                    InputStream is= ZobonApp.getContext().getAssets().open(String.format(Locale.US, "misc/n_step%d.json",i));
                    updateContactsData(is);
                    is.close();
                    QueryPreferences.setInitializeStep(i+1);
                }
            }
            QueryPreferences.setIsInitialized(true);


        } catch (Exception e)
        {
            Log.e(TAG,"Initialization Problem",e);
            //TODO: log exception
        }
        finally
        {
            inInitialize =false;
        }
    }


    private void updateContactsData(InputStream is) throws IOException
    {
        Gson gson = new Gson();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        DataCollection dataCollection = gson.fromJson(reader, DataCollection.class);
        reader.close();

        ZobonApp.getDataManager().populateContacts(dataCollection.getContacts());
    }

    private int updateBasicData(InputStream is)throws IOException
    {

        Gson gson = new GsonBuilder().create();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        DataCollection dataCollection = gson.fromJson(reader, DataCollection.class);
        reader.close();
        switch (dataCollection.getType())
        {
            case "BLOCK":
                break;
            case "INIT":
            case "NORM":
            case "RESET":
                ZobonApp.getDataManager().resetData();
                break;
            case "WARN":
        }
        ZobonApp.getDataManager().populateCategories(dataCollection.getCategories());
        ZobonApp.getDataManager().populateBusinessEntities(dataCollection.getEntities());
        ZobonApp.getDataManager().populateContacts(dataCollection.getContacts());
        ZobonApp.getDataManager().populateOffers(dataCollection.getOffers());
        ZobonApp.getDataManager().populateMenus(dataCollection.getMenus());
        QueryPreferences.setLatestUpdate(dataCollection.getLatestUpdate());
        return dataCollection.getSteps();


    }
    public static void setServiceAlarm(Context ctx,boolean isOn)
    {
        Intent intent=new Intent(ctx,UpdateService.class);
        intent.setAction(ACTION_CHECK_UPDATE);
        PendingIntent pendingIntent=PendingIntent.getService(ctx,0,intent,0);
        AlarmManager alarmManager= (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        if(isOn)
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),POLL_INTERVAL_MS,pendingIntent);
        else
        {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
        QueryPreferences.setAlarmOn(isOn);

    }
public static boolean isServiceAlarmOn(Context ctx)
    {
        Intent intent=new Intent(ctx,UpdateService.class);
        PendingIntent pendingIntent=PendingIntent.getService(ctx,0,intent,PendingIntent.FLAG_NO_CREATE);
        return pendingIntent!=null;
    }

}
