package com.zobonapp.ui.hotline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zobonapp.R;
import com.zobonapp.domain.Category;
import com.zobonapp.utils.BasicActivity;
import com.zobonapp.utils.ZobonApp;

public class OffersActivity extends BasicActivity
{
    private final static String EXTRA_CATEGORY_ID="categoryId";
    private String categoryId;
    private Fragment itemsFragment;
    private Category category;
    public static Intent newIntent(Context ctx,String categoryId)
    {
        Intent intent=new Intent(ctx,OffersActivity.class);
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
        category= ZobonApp.getDataManager().findCategoryById(categoryId);
        if(category!=null)
        {
            getSupportActionBar().setTitle(category.getName());
            //TODO:
            getSupportActionBar().setSubtitle(getResources().getQuantityString(R.plurals.itemsCount,category.getEntities(),category.getEntities()));

        }
        if(getFragmentManager().findFragmentById(android.R.id.content)==null)
        {
            Bundle args= OffersAdapter.newArguments(categoryId,"inCatOffers");
//            Bundle args= OffersAdapter.newArguments(categoryId,"menu");
            itemsFragment=ItemsFragment.newInstance(args,OffersAdapter.class);
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, itemsFragment).commit();

            itemsFragment.setHasOptionsMenu(true);
        }

    }
}
