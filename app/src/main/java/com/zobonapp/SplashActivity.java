package com.zobonapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zobonapp.manager.InitializationEvent;
import com.zobonapp.utils.QueryPreferences;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by hasalem on 12/15/2017.
 */

public class SplashActivity extends AppCompatActivity
{
    private static final String LOG_TAG=SplashActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart()
    {
        super.onStart();


        EventBus.getDefault().register(this);
        if (QueryPreferences.isInitialized())
            finishSpalsh();

    }

    @Override
    protected void onStop()
    {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInitialized(InitializationEvent event)
    {
        Log.d("UpdateService",event.getStatus().toString());
        finishSpalsh();
//        Log.d(LOG_TAG,event.getStatus().toString());
//        if(event.getStatus()== InitializationEvent.Status.PARSING_COMPLETED)
//            new Handler().postDelayed(new Runnable()
//            {
//                @Override
//                public void run()
//                {
//                    finishSpalsh();
//                }
//            },500);

    }
    private void finishSpalsh()
    {
        Log.i(LOG_TAG,"finish splash & Initiate Main activity");
        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
        Log.i(LOG_TAG,"to start the main activity");
        startActivity(intent);
        Log.i(LOG_TAG,"main activity started");
        finish();
        Log.i(LOG_TAG,"Splash activity finished");

    }
}
