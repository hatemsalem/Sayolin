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
import com.zobonapp.utils.QueryPreferences;
import com.zobonapp.utils.DataCollection;
import com.zobonapp.manager.InitializationEvent;
import com.zobonapp.utils.ZobonApp;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
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
            InputStream is= ZobonApp.getContext().getAssets().open("misc/initial2.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            DataCollection dataCollection = gson.fromJson(reader, DataCollection.class);
            reader.close();
            is.close();
            Log.d(TAG,"InitialParsing completed with: "+dataCollection.getEntities().size()+" items");
            int step=QueryPreferences.getInitializeStep();
            Log.d(TAG,"Initializing......");
            switch (step)
            {
                case 0:
                    ZobonApp.getContext().getDataManager().updateCategories(dataCollection.getCategories());
                    QueryPreferences.setInitializeStep(step++);
                case 1:
                    ZobonApp.getContext().getDataManager().updateBusinessEntities(dataCollection.getEntities());
                    QueryPreferences.setInitializeStep(step++);
                    EventBus.getDefault().post(new InitializationEvent(InitializationEvent.Status.COMPLETED));
                case 2:
                    ZobonApp.getContext().getDataManager().updateItemCategoryRelations(dataCollection.getItemsCategories());
                    QueryPreferences.setInitializeStep(step++);
                case 3:
                    QueryPreferences.setIsInitialized(true);
                    Log.d(TAG,"Event to be sent");
                    EventBus.getDefault().post(new InitializationEvent(InitializationEvent.Status.COMPLETED));
                    Log.d(TAG,"MainSetup completed");
            }
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
