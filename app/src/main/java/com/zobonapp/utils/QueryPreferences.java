package com.zobonapp.utils;

import android.text.TextUtils;

/**
 * Created by hasalem on 11/30/2017.
 */

public class QueryPreferences
{
    public enum ViewType{
        ITEM(0),CATEGORY(1);
        private final int code;
        ViewType(int code)
        {
            this.code=code;
        }
        int getCode()
        {
            return code;
        }
        static ViewType fromInteger(int code)
        {
            switch (code)
            {
                case 0:
                    return ITEM;
                case 1:
                    return CATEGORY;
            }
            return null;
        }
    };
    public static String ARABIC="ar";
    public static String ENGLISH="en";
    private static final String IS_INITIALIZED="initialized";
    private static final String IS_ALARM_ON="alarmOn";
    private static final String LANGUAGE="language";
    private static final String SEARCH_QUERY="searchQuery";
    private static final String INITIALIZE_STEP="initializeStep";
    private static final String UPDATE_STEP="updateStep";
    private static final String TOTAL_STEPS ="totalSteps";
    private static final String HOTLINES_VIEW_TYPE="hotilinesViewType";
    private static final String SHOW_LARGE_DISPLAY="showLargeDisplay";
    private static final String SHOW_SHORTCUT_CENTER ="showShortcutCenter";
    private static final String LATEST_UPDATE="latestUpdate";


    public static boolean isInitialized()
    {
        return  ZobonApp.getContext().getPrefs().getBoolean(IS_INITIALIZED,false);
    }

    public static void setIsInitialized(boolean isInitialized)
    {
        ZobonApp.getContext().getPrefs().edit().putBoolean(IS_INITIALIZED,isInitialized).apply();
    }
    public static String getLanguage()
    {
        return ZobonApp.getContext().getPrefs().getString(LANGUAGE,null);
    }
    public static void setLanguage(String language)
    {
        ZobonApp.getContext().getPrefs().edit().putString(LANGUAGE,language).apply();
    }
    public static boolean isAlarmOn()
    {
        return ZobonApp.getContext().getPrefs().getBoolean(IS_ALARM_ON,false);
    }
    public static void setAlarmOn(boolean isOn)
    {
        ZobonApp.getContext().getPrefs().edit().putBoolean(IS_ALARM_ON,isOn).apply();
    }
    public static void setSearchQuery(String searchQuery)
    {
        ZobonApp.getContext().getPrefs().edit().putString(SEARCH_QUERY,searchQuery).apply();
    }
    public static void setSearchQuery(String key,String searchQuery)
    {
        if(TextUtils.isEmpty(key))
            ZobonApp.getContext().getPrefs().edit().putString(SEARCH_QUERY,searchQuery).apply();
        else
            ZobonApp.getContext().getPrefs().edit().putString(key,searchQuery).apply();

    }
    public static String getSearchQuery()
    {
        return ZobonApp.getContext().getPrefs().getString(SEARCH_QUERY,"");
    }
    public static String getSearchQuery(String key)
    {
        if(TextUtils.isEmpty(key))
            return getSearchQuery();

        return ZobonApp.getContext().getPrefs().getString(key,"");
    }
    public static void setTotalSteps(int steps)
    {
        ZobonApp.getContext().getPrefs().edit().putInt(TOTAL_STEPS,steps).apply();
    }
    public static int getTotalSteps()
    {
        return ZobonApp.getContext().getPrefs().getInt(TOTAL_STEPS,-1);
    }
    public static void setInitializeStep(int step)
    {
        ZobonApp.getContext().getPrefs().edit().putInt(INITIALIZE_STEP,step).apply();
    }
    public static int getInitializeStep()
    {
        return ZobonApp.getContext().getPrefs().getInt(INITIALIZE_STEP,-1);
    }
    public static void setUpdateStep(int step)
    {
        ZobonApp.getContext().getPrefs().edit().putInt(UPDATE_STEP,step).apply();
    }
    public static int getUpdateStep()
    {
        return ZobonApp.getContext().getPrefs().getInt(UPDATE_STEP,-1);
    }
    public static void setHotlinesViewType(ViewType type)
    {
        ZobonApp.getContext().getPrefs().edit().putInt(HOTLINES_VIEW_TYPE,type.getCode()).apply();
    }
    public static ViewType getHotlinesViewType()
    {
        return ViewType.fromInteger(ZobonApp.getContext().getPrefs().getInt(HOTLINES_VIEW_TYPE,ViewType.ITEM.getCode()));
    }

    public static ViewType getViewType(String key)
    {
        return ViewType.fromInteger(ZobonApp.getContext().getPrefs().getInt(key,ViewType.ITEM.getCode()));
    }
    public static void setViewType(String key,ViewType type)
    {
        ZobonApp.getContext().getPrefs().edit().putInt(key,type.getCode()).apply();
    }
    public static void setLatestUpdate(long latestUpdate)
    {
        ZobonApp.getContext().getPrefs().edit().putLong(LATEST_UPDATE,latestUpdate).apply();
    }
    public static long getLatestUpdate()
    {
        return ZobonApp.getContext().getPrefs().getLong(LATEST_UPDATE,0);
    }

    public static boolean isShowLargeDisplay()
    {
        return ZobonApp.getContext().getPrefs().getBoolean(SHOW_LARGE_DISPLAY,false);
    }
    public static boolean isShowShortcutCenter()
    {
        return ZobonApp.getContext().getPrefs().getBoolean(SHOW_SHORTCUT_CENTER,false);
    }

}
