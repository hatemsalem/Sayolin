package com.zobonapp.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.zobonapp.domain.Contact;

/**
 * Created by Admin on 4/4/2018.
 */

public class IntentHelper
{
    private static String FACEBOOK_URL = "https://www.facebook.com/";
    private boolean supportDial;
    private boolean supportMap;
    public IntentHelper(Context ctx)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (intent.resolveActivity(ctx.getPackageManager()) != null)
        {
            supportDial =true;
        } else
        {
            supportDial =false;
        }
        intent=new Intent(Intent.ACTION_VIEW,Uri.parse("geo:0,0"));
        intent.setPackage("com.google.android.apps.maps");
        if(intent.resolveActivity(ctx.getPackageManager())!=null)
        {
            supportMap=true;
        }
        else
        {
            supportMap=false;
        }

    }
    public void browse(Uri data)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(data);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ZobonApp.getContext().startActivity(intent);
    }
    public void browse(String url)
    {
        Uri uri=Uri.parse(url);
        browse(uri);
    }
    public void openFacebookPage(String id)
    {
        PackageManager packageManager = ZobonApp.getContext().getPackageManager();
        Intent intent=new Intent(Intent.ACTION_VIEW);
        String uri=FACEBOOK_URL+ id;
        try
        {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                uri= "fb://facewebmodal/f?href=" +FACEBOOK_URL+ id;
            } else { //older versions of fb app
                uri= "fb://page/" + id;
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            //TODO: no thing, we already have the correct uri
        }
        intent.setData(Uri.parse(uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ZobonApp.getContext().startActivity(intent);
    }
    public boolean launchMarket()
    {
        Uri uri = Uri.parse("market://details?id=" + ZobonApp.getContext().getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        myAppLinkToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            ZobonApp.getContext().startActivity(myAppLinkToMarket);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
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
//    public boolean showMap(String ...params)
//    {
//        String query=TextUtils.join(" ",params);
//        query=query.replaceAll("[,|-|_|,|-]"," ");
//        query=query.replaceAll("\\s+"," ");
//
////        query=query.replaceAll(" ","+");
//        Uri uri=Uri.parse("geo:0,0?q="+Uri.encode(query));
//        Intent mapIntent=new Intent(Intent.ACTION_VIEW,uri);
//
//        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        ZobonApp.getContext().startActivity(mapIntent);
//        return true;
//    }
    public boolean showMap(Contact contact,String ...params)
    {
        Uri uri=null;
        if(contact.getUri().startsWith("geo:")&&contact.getUri().length()>6)
        {

            uri = Uri.parse(String.format("geo:0,0?q=%s(%s)",contact.getUri().substring(4),Uri.encode(params[0])));
//            uri=Uri.parse(contact.getUri()+"?q=(Hello)");
//            uri=Uri.parse("https://www.google.com/maps/search/?api=1&query="+contact.getUri().substring(4));
        }

        else
        {
            String query=TextUtils.join(" ",params);
            query=query.replaceAll("[,|-|_|,|-]"," ");
            query=query.replaceAll("\\s+"," ");

            //        query=query.replaceAll(" ","+");
            uri=Uri.parse("geo:0,0?q="+Uri.encode(query));
        }
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
