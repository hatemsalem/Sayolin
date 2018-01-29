package com.zobonapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zobonapp.domain.Contact;
import com.zobonapp.ui.GenericPagerAdapter;

import java.util.List;

public class ItemDetailsActivity extends AppCompatActivity
{
    private static final String EXTRA_ITEM_ID="itemId";
    private GenericPagerAdapter adapter;
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
        setContentView(R.layout.activity_item_details);
        adapter=new ContactsAdapter();
        adapter.setArguments(ContactsAdapter.newArguments(getIntent().getStringExtra(EXTRA_ITEM_ID),"contacts"));
        adapter.onCreate();
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
