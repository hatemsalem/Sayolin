package com.zobonapp.ui.hotline;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zobonapp.R;

public class ItemsActivity extends AppCompatActivity
{
    private final static String EXTRA_CATEGORY_ID="categoryId";
    private String categoryId;
    public static Intent newIntent(Context ctx,String categoryId)
    {
        Intent intent=new Intent(ctx,ItemsActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID,categoryId);
        return intent;
    }
    public static void start(Context ctx,String categoryId)
    {
        ctx.startActivity(newIntent(ctx,categoryId));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        categoryId=getIntent().getStringExtra(EXTRA_CATEGORY_ID);
        if(getFragmentManager().findFragmentById(android.R.id.content)==null)
        {
            Bundle args=BusinessEntityAdapter.newArguments(categoryId,"category");
            getFragmentManager().beginTransaction().add(android.R.id.content, ItemsFragment.newInstance(args,BusinessEntityAdapter.class)).commit();
        }
    }
}
