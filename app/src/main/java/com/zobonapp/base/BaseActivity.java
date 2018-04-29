package com.zobonapp.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alexvasilkov.android.commons.state.InstanceStateManager;
import com.alexvasilkov.android.commons.ui.Views;
import com.alexvasilkov.events.Events;

public abstract class BaseActivity extends AppCompatActivity
{

    private int infoTextId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InstanceStateManager.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Events.register(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        InstanceStateManager.saveInstanceState(this, outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Events.unregister(this);
    }



    protected void setInfoText(@StringRes int textId) {
        infoTextId = textId;
        invalidateOptionsMenu();
    }


    @NonNull
    protected ActionBar getSupportActionBarNotNull() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            throw new NullPointerException("Action bar was not initialized");
        }
        return actionBar;
    }

}
