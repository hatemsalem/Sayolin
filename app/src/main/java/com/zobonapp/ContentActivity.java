package com.zobonapp;

import android.content.Context;
import android.os.Bundle;

import com.zobonapp.utils.RootActivity;

public class ContentActivity extends RootActivity
{
    public static String EXTRA_URL="url";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getFragmentManager().findFragmentById(android.R.id.content)==null)
        {
            String url=getIntent().getStringExtra(EXTRA_URL);
            getFragmentManager().beginTransaction().add(android.R.id.content,ContentFragment.newInstance(url)).commit();
        }
    }

}
