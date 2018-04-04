package com.zobonapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Admin on 4/4/2018.
 */

public class IntentHelper
{
    private boolean supportDial;
    private boolean supportMap;
    public IntentHelper()
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (intent.resolveActivity(ZobonApp.getContext().getPackageManager()) != null)
        {
            supportDial =true;
        } else
        {
            supportDial =false;
        }
        intent=new Intent(Intent.ACTION_VIEW,Uri.parse("geo:0,0"));
        intent.setPackage("com.google.android.apps.maps");
        if(intent.resolveActivity(ZobonApp.getContext().getPackageManager())!=null)
        {
            supportMap=true;
        }
        else
        {
            supportMap=false;
        }

    }

    public boolean dial(Uri data)
    {
        if(supportDial)
        {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(data);
            dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ZobonApp.getContext().startActivity(dialIntent);
        }
        return supportDial;
    }
    public boolean showMap(String ...params)
    {
        String query=TextUtils.join(" ",params);
        query=query.replaceAll("[,|-|_|,|-]"," ");
        query=query.replaceAll("\\s+"," ");

//        query=query.replaceAll(" ","+");
        Uri uri=Uri.parse("geo:0,0?q="+Uri.encode(query));
        Intent mapIntent=new Intent(Intent.ACTION_VIEW,uri);

        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ZobonApp.getContext().startActivity(mapIntent);
        return true;
    }
    public boolean isSupportDial()
    {
        return supportDial;
    }
}
