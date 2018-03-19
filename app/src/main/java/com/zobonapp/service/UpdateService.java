package com.zobonapp.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.commonsware.cwac.security.ZipUtils;
import com.google.gson.Gson;
import com.zobonapp.BuildConfig;
import com.zobonapp.manager.InitializationEvent;
import com.zobonapp.utils.DataCollection;
import com.zobonapp.utils.QueryPreferences;
import com.zobonapp.utils.ZobonApp;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by hasalem on 12/11/2017.
 */

public class UpdateService extends IntentService
{

    public static final String TAG=UpdateService.class.getSimpleName();
    private static final String UPDATE_URL="http://test.zobonapp.com/web/updates/ver-%s/%d";
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
    protected void update()
    {
        Log.i(TAG,"Check update will be here");
        synchronized (this)
        {
            if (QueryPreferences.isInitialized() && inUpdate==false)
                inUpdate=true;
            else
                return;
        }
        String url=String.format(Locale.US,UPDATE_URL,BuildConfig.VERSION_NAME,QueryPreferences.getLatestUpdate());
        try
        {

            int step=QueryPreferences.getUpdateStep();

            if(step<0)
            {
                File update=download(url);
                File updateDir=new File(getFilesDir(),UPDATE_BASE_DIR);
                ZipUtils.delete(updateDir);
                updateDir.mkdirs();
                ZipUtils.unzip(update,updateDir);
                update.delete();
                QueryPreferences.setUpdateStep(step=0);
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
                            //TODO:remove contacts
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
                   //TODO: delete categories , entities and contacts

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
        ZobonApp.getContext().getDataManager().deleteItems(dataCollection.getDeletedEntities());
        ZobonApp.getContext().getDataManager().deleteCategories(dataCollection.getDeletedCategories());

    }

    private File download(String url) throws IOException
    {
        File output=new File(getFilesDir(),UPDATE_FILE_NAME);
        if(output.exists())
            output.delete();
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        Response response=client.newCall(request).execute();
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
            }
            EventBus.getDefault().post(new InitializationEvent(InitializationEvent.Status.COMPLETED));

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

        ZobonApp.getContext().getDataManager().populateContacts(dataCollection.getContacts());
    }

    private int updateBasicData(InputStream is)throws IOException
    {
        Gson gson = new Gson();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        DataCollection dataCollection = gson.fromJson(reader, DataCollection.class);
        reader.close();
        ZobonApp.getContext().getDataManager().populateCategories(dataCollection.getCategories());
        ZobonApp.getContext().getDataManager().populateBusinessEntities(dataCollection.getEntities());
        ZobonApp.getContext().getDataManager().populateContacts(dataCollection.getContacts());
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
