package com.zobonapp.ui;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by hasalem on 21/1/2018.
 */

public class AdapterFactory
{
    public static <T extends GenericPagerAdapter> T getAdapter(Class<T> clazz,Bundle args)
    {
        T adapter=null;
        try
        {
            adapter=clazz.newInstance();
            adapter.setArguments(args);


        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        adapter.onCreate();
        return adapter;

    }
}
