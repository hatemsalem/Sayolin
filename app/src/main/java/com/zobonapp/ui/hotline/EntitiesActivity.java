package com.zobonapp.ui.hotline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zobonapp.domain.Category;
import com.zobonapp.utils.BasicActivity;
import com.zobonapp.utils.ZobonApp;

public class EntitiesActivity extends BasicActivity
{
    private final static String EXTRA_CATEGORY_ID="categoryId";
    private String categoryId;
    private Fragment itemsFragment;
    private Category category;
    public static Intent newIntent(Context ctx,String categoryId)
    {
        Intent intent=new Intent(ctx,EntitiesActivity.class);
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
        category= ZobonApp.getContext().getDataManager().findCategoryById(categoryId);
        if(category!=null)
        {
            getSupportActionBar().setTitle(category.getName());
            //TODO:
            getSupportActionBar().setSubtitle("Total:"+category.getEntities());

        }
        if(getFragmentManager().findFragmentById(android.R.id.content)==null)
        {
            Bundle args= EntityAdapter.newArguments(categoryId,"menu");
            itemsFragment=ItemsFragment.newInstance(args,EntityAdapter.class);
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, itemsFragment).commit();

            itemsFragment.setHasOptionsMenu(true);
        }

    }
}
