package com.zobonapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by hasalem on 15/1/2018.
 */

public class OnBootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Do nothing, just to enable the ZobonApp class to be instantiated
    }
}
