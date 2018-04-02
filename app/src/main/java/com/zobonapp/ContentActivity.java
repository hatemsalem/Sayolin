package com.zobonapp;

import android.os.Bundle;

import com.zobonapp.utils.BasicActivity;

public class ContentActivity extends BasicActivity
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
