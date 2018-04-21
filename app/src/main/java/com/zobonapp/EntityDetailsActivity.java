package com.zobonapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zobonapp.domain.BusinessEntity;
import com.zobonapp.ui.AdapterFactory;
import com.zobonapp.ui.GenericPagerAdapter;
import com.zobonapp.utils.BasicActivity;
import com.zobonapp.utils.ZobonApp;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class EntityDetailsActivity extends BasicActivity implements View.OnClickListener
{
    private static String TAG = EntityDetailsActivity.class.getSimpleName();
    private static final String EXTRA_ITEM_ID="itemId";
    private GenericPagerAdapter adapter;
    private BusinessEntity entity;
    private ImageView imgLogo;
    private ImageView imgFavorite;
    private ImageView imgOffers;
    private TextView lblHotline;
    private TextView lblName;
    private TextView lblDesc;
    public static Intent newIntent(Context ctx, String itemId)
    {
        Intent intent=new Intent(ctx,EntityDetailsActivity.class);
        intent.putExtra(EXTRA_ITEM_ID,itemId);
        return intent;
    }
    public static void start(Context ctx,String itemId)
    {
        ctx.startActivity(newIntent(ctx,itemId));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        String itemId=getIntent().getStringExtra(EXTRA_ITEM_ID);
        entity = ZobonApp.getDataManager().findBusinessItemById(itemId);
        if(entity !=null)
            getSupportActionBar().setSubtitle(entity.getName());
        setContentView(R.layout.activity_entity_details);
        imgLogo=findViewById(R.id.imgLogo);
        imgFavorite =findViewById(R.id.imgFavorite);
        imgOffers=findViewById(R.id.imgOffers);
        if(entity.getOffers()>0)
        {
            imgOffers.setVisibility(View.VISIBLE);
        }
        imgFavorite.setOnClickListener(this);
        if(entity.isFavorite())

        {
            imgFavorite.setImageDrawable(ZobonApp.getContext().getResources().getDrawable(R.drawable.fav_on));
        }
        else
        {
            imgFavorite.setImageDrawable(ZobonApp.getContext().getResources().getDrawable(R.drawable.fav_off));
        }
        lblHotline=findViewById(R.id.lblHotline);
        lblHotline.setText(entity.getContact().getSchemeSpecificPart());
        lblName=findViewById(R.id.lblName);
        lblDesc=findViewById(R.id.lblDesc);
        ZobonApp.getPicasso().load(Uri.parse(ZobonApp.getAssetPath(entity.getId().toString())))
                .error(R.drawable.notfoundimage)
                .placeholder(R.drawable.placeholder   ).into(imgLogo);
        lblName.setText(entity.getName());
        lblDesc.setText(entity.getDesc());
        adapter= AdapterFactory.getAdapter(ContactsAdapter.class,ContactsAdapter.newArguments(itemId,"contacts"));
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        update();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.fragment_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                Log.d(TAG, "QueryTextSubmit:" + query);
                searchView.clearFocus();
                adapter.setSearchQuery(query);
                update();
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String query)
            {
                Log.d(TAG, "QueryTextSubmit:" + query);
                adapter.setSearchQuery(query);
                update();
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                adapter.setSearchQuery("");
                update();
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    private void update()
    {
        new AsyncTask<Void, Void, List<?>>()
        {
            @Override
            protected List<?> doInBackground(Void... voids)
            {
                return adapter.loadData(0);
            }

            @Override
            protected void onPostExecute(List<?> entities)
            {
                adapter.clear();

                if (entities.size() > 0)
                {
                    adapter.add(entities);
                }


            }
        }.execute();

    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imgFavorite:
                entity.setFavorite(!entity.isFavorite());
                ZobonApp.getDataManager().updateBusinessItem(entity);
                if(entity.isFavorite())

                {
                    imgFavorite.setImageDrawable(ZobonApp.getContext().getResources().getDrawable(R.drawable.fav_on));
                }
                else
                {
                    imgFavorite.setImageDrawable(ZobonApp.getContext().getResources().getDrawable(R.drawable.fav_off));
                }
                break;
        }
    }
}
