package com.zobonapp.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.zobonapp.manager.InitializationEvent;
import com.zobonapp.utils.DataCollection;
import com.zobonapp.utils.QueryPreferences;
import com.zobonapp.utils.ZobonApp;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * Created by hasalem on 12/11/2017.
 */

public class UpdateService extends IntentService
{
    public static final String TAG=UpdateService.class.getSimpleName();
    private static final long POLL_INTERVAL_MS= TimeUnit.MINUTES.toMillis(1);
    private static final String ACTION_INITIALIZE = "com.zobonapp.service.action.INITIALIZE";
    private static final String ACTION_CHECK_UPDATE = "com.zobonapp.service.action.CHECK_UPDATE";
    private boolean inInitialize =false;
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
                    Log.i(TAG,"Check update will be here");
                    break;
            }

        }
    }
    protected void initialize()
    {
        synchronized (this)
        {
            if(inInitialize ==true)
                return;;
            inInitialize =true;
        }

        //TODO: to initialize
        Gson gson=new Gson();
        try
        {
            int step=QueryPreferences.getInitializeStep();
            if(step<0)
            {
                InputStream is= ZobonApp.getContext().getAssets().open("misc/initial.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                DataCollection dataCollection = gson.fromJson(reader, DataCollection.class);
                reader.close();
                is.close();
                Log.i(TAG,"InitialParsing completed with: "+dataCollection.getEntities().size()+" items");
                ZobonApp.getContext().getDataManager().populateCategories(dataCollection.getCategories());
                ZobonApp.getContext().getDataManager().populateBusinessEntities(dataCollection.getEntities());
                ZobonApp.getContext().getDataManager().populateContacts(dataCollection.getContacts());
//                ZobonApp.getContext().getDataManager().populateItemCategoryRelations(dataCollection.getItemsCategories());
                QueryPreferences.setInitializeStep(step=0);
                QueryPreferences.setTotalSteps(dataCollection.getSteps());
                QueryPreferences.setLatestUpdate(dataCollection.getLatestUpdate());
            }
            Log.i(TAG,"Event to be sent");
            EventBus.getDefault().post(new InitializationEvent(InitializationEvent.Status.COMPLETED));
            Log.i(TAG,"MainSetup completed");

            int totalSteps=QueryPreferences.getTotalSteps();
            if(step>=0&&step<totalSteps)
            {

                for(int i=step;i<totalSteps;i++)
                {
                    InputStream is= ZobonApp.getContext().getAssets().open(String.format("misc/step%d.json",i));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    DataCollection dataCollection = gson.fromJson(reader, DataCollection.class);
                    reader.close();
                    is.close();
                    Log.i(TAG,"InitialParsing completed with step No.: "+i);
                    ZobonApp.getContext().getDataManager().populateCategories(dataCollection.getCategories());
                    ZobonApp.getContext().getDataManager().populateBusinessEntities(dataCollection.getEntities());
                    ZobonApp.getContext().getDataManager().populateContacts(dataCollection.getContacts());
                    ZobonApp.getContext().getDataManager().populateItemCategoryRelations(dataCollection.getItemsCategories());
                    QueryPreferences.setInitializeStep(i+1);
                }
            }
            QueryPreferences.setIsInitialized(true);




//            int step=QueryPreferences.getInitializeStep();
//            Log.i(TAG,"Initializing......");
//            switch (step)
//            {
//                case 0:
//
//
//                case 1:
//
//                    QueryPreferences.setInitializeStep(step++);
//                case 2:
//                    QueryPreferences.setInitializeStep(step++);
//                case 3:
//                    QueryPreferences.setIsInitialized(true);
//            }
        } catch (Exception e)
        {
            Log.e(TAG,"Initialization Problem",e);
            //TODO: log exception
        }
        inInitialize =false;
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
