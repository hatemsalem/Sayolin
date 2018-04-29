package com.zobonapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zobonapp.utils.BasicActivity;
import com.zobonapp.utils.ZobonApp;

public class MainActivity extends BasicActivity implements NavigationView.OnNavigationItemSelectedListener,DialogInterface.OnClickListener
{
    private static final String TAG=MainActivity.class.getSimpleName();
    private ShareActionProvider share;
    private ContentAdapter adapter;
    private Intent shareIntent=new Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT,"Hello");
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        strictModeSetup();
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
//        toolbar.setSubtitle("Hotlines");
        updateNavText();
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager pager = findViewById(R.id.pager);
        adapter=new ContentAdapter(this);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);
        TabLayout tabs=findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);
        Log.i(TAG,"in create method");

    }
    private void updateNavText()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        Resources res=getResources();
        TextView lblAppVer=headerView.findViewById(R.id.lblAppVer);


        String value=res.getString(R.string.app_name_ver,res.getString(R.string.app_name),BuildConfig.VERSION_NAME);
        lblAppVer.setText(value);

    }
    @Override
    protected void onResume()
    {
        Log.i(TAG,"in resume method");
        super.onResume();
    }

    private void strictModeSetup()
    {
//        StrictMode.enableDefaults();
        StrictMode.ThreadPolicy.Builder b=new StrictMode.ThreadPolicy.Builder();
        if(BuildConfig.DEBUG)
        {
            b.detectAll().penaltyLog();
        }
        else
        {
            b.detectAll().penaltyLog();
        }
        StrictMode.setThreadPolicy(b.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.options,menu);
//        (ShareActionProvider) menu.findItem(R.id.nav_share).getActionProvider();
//        share= (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.nav_share));
        share=(ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.nav_share));
        share.setShareIntent(shareIntent);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent=new Intent(this,ContentActivity.class);
        switch (item.getItemId())
        {
            case R.id.about:
                intent.putExtra(ContentActivity.EXTRA_URL,"file:///android_asset/misc/initial.json");
                startActivity(intent);
                return true;
            case R.id.help:
                intent.putExtra(ContentActivity.EXTRA_URL,"file:///android_asset/misc/help.html");
                startActivity(intent);
                return true;
            case R.id.mnuLangToggle:
                toggleLang();
                return true;
            case R.id.search:
                return true;
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void toggleLang()
    {

        ZobonApp.toggleLang() ;
//        Context baseContext=getBaseContext();
//        Locale localLocale = new Locale(SayolinApp.getContext().toggleLang());
//        Locale.setDefault(localLocale);
//        Configuration localConfiguration = baseContext.getResources().getConfiguration();
//        localConfiguration.setLayoutDirection(localLocale);
//        localConfiguration.setLocale(localLocale);
//        baseContext.getResources().updateConfiguration(localConfiguration, baseContext.getResources().getDisplayMetrics());



        recreate();
//        this.appSessionManager.updateLanguage(paramString);
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        paramString.setFlags(268468224);
//        startActivity(intent);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        switch (item.getItemId())
        {
            case R.id.nav_manage:
                startActivity(new Intent(this,PreferencesActivity.class));
                break;
            case R.id.nav_camera:
                break;
            case R.id.nav_rate:
                    if(!ZobonApp.getIntentHelper().launchMarket())
                        Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
                    break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                StringBuilder sb = new StringBuilder();
                sb.append("Hi, I am using ZobonApp. I like this and I want you to check it out.\n");
                sb.append("https://play.google.com/store/apps/details?id=" + getPackageName());
                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Test");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                startActivity(Intent.createChooser(sharingIntent, "Test"));
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_view:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        if(getFragmentManager().getBackStackEntryCount()==0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.msgExit);
            builder.setPositiveButton(R.string.ok, this);
            builder.setNegativeButton(R.string.cancel, this);
            builder.show();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        if(which== DialogInterface.BUTTON_POSITIVE)
        {
            finish();
        }
    }
}
