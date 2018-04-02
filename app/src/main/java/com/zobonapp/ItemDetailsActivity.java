package com.zobonapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zobonapp.domain.BusinessEntity;
import com.zobonapp.ui.AdapterFactory;
import com.zobonapp.ui.GenericPagerAdapter;
import com.zobonapp.utils.BasicActivity;
import com.zobonapp.utils.ZobonApp;

import java.util.List;

public class ItemDetailsActivity extends BasicActivity
{
    private static final String EXTRA_ITEM_ID="itemId";
    private GenericPagerAdapter adapter;
    private BusinessEntity item;
    public static Intent newIntent(Context ctx, String itemId)
    {
        Intent intent=new Intent(ctx,ItemDetailsActivity.class);
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
        item= ZobonApp.getContext().getDataManager().findBusinessItemById(itemId);
        if(item!=null)
            getSupportActionBar().setSubtitle(item.getName());
        setContentView(R.layout.activity_item_details);
        adapter= AdapterFactory.getAdapter(ContactsAdapter.class,ContactsAdapter.newArguments(itemId,"contacts"));
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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


                if (entities.size() > 0)
                {
                    adapter.add(entities);
                }


            }
        }.execute();

    }
}
