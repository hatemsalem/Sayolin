package com.zobonapp.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

/**
 * Created by hasalem on 12/2/2017.
 */

public class RootActivity extends AppCompatActivity
{
    @Override
    protected void attachBaseContext(Context newBase)
    {

        Locale locale = new Locale(ZobonApp.getContext().getLang());
        Locale.setDefault(locale);
        Configuration localConfiguration = newBase.getResources().getConfiguration();
        localConfiguration.setLayoutDirection(locale);
        localConfiguration.setLocale(locale);
        newBase.getResources().updateConfiguration(localConfiguration, newBase.getResources().getDisplayMetrics());
        super.attachBaseContext(newBase);
    }
}
