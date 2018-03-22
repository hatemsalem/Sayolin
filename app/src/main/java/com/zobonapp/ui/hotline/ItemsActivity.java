package com.zobonapp.ui.hotline;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zobonapp.R;
import com.zobonapp.domain.Category;
import com.zobonapp.utils.RootActivity;
import com.zobonapp.utils.ZobonApp;

public class ItemsActivity extends RootActivity
{
    private final static String EXTRA_CATEGORY_ID="categoryId";
    private String categoryId;
    private Fragment itemsFragment;
    private Category category;
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
        category= ZobonApp.getContext().getDataManager().findCategoryById(categoryId);
        if(category!=null)
        {
            getSupportActionBar().setTitle(category.getName());
            //TODO:
            getSupportActionBar().setSubtitle("Total:"+category.getEntities());

        }
        if(getFragmentManager().findFragmentById(android.R.id.content)==null)
        {
            Bundle args=BusinessEntityAdapter.newArguments(categoryId,"offer");
            itemsFragment=ItemsFragment.newInstance(args,BusinessEntityAdapter.class);
            getFragmentManager().beginTransaction().add(android.R.id.content, itemsFragment).commit();
            itemsFragment.setHasOptionsMenu(true);
        }

    }
}
