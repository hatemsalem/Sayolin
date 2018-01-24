package com.zobonapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;

/**
 * Created by hasalem on 11/12/2017.
 */

public class ContentFragment extends WebViewFragment
{
    private static final String KEY_FILE="file";
    public static ContentFragment newInstance(String file)
    {
        ContentFragment fragment=new ContentFragment();
        Bundle args=new Bundle();
        args.putString(KEY_FILE,file);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View result= super.onCreateView(inflater, container, savedInstanceState);
        getWebView().getSettings().setJavaScriptEnabled(true);
        getWebView().getSettings().setSupportZoom(true);
        getWebView().getSettings().setBuiltInZoomControls(true);
        getWebView().setWebViewClient(new WebViewClient());
        getWebView().loadUrl(getPage());
        return result;

    }

    private String getPage()
    {
        return getArguments().getString(KEY_FILE);
    }
}
