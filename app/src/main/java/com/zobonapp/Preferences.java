package com.zobonapp;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.zobonapp.utils.RootActivity;

public class Preferences extends RootActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getFragmentManager().findFragmentById(android.R.id.content)==null)
        {
            getFragmentManager().beginTransaction().add(android.R.id.content,new Display()).commit();
        }
    }
    public static class Display extends PreferenceFragment
    {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
        }
    }
}
