package com.zobonapp.ui.hotline;

import android.os.Bundle;
import android.view.ViewGroup;

import com.zobonapp.R;
import com.zobonapp.domain.Category;
import com.zobonapp.ui.GenericPagerAdapter;
import com.zobonapp.utils.ZobonApp;

import java.util.List;

/**
 * Created by hasalem on 14/1/2018.
 */

public class FavoriteCategoryAdapter extends GenericPagerAdapter<CategoryHolder,Category>
{
    private static final String ARG_CATEGORY_TYPE="categoryType";
    private int type;

    public static Bundle newArguments(int type)
    {
        Bundle arguments=new Bundle();
        arguments.putInt(ARG_CATEGORY_TYPE,type);
        arguments.putString(ARG_ADAPTER_CLASS,FavoriteCategoryAdapter.class.getName());
        return arguments;
    }
    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new CategoryHolder(parent, R.layout.cell_category);
    }
    @Override
    public List<Category> loadData(int page)
    {
        return ZobonApp.getContext().getDataManager().findFavoriteCategoriesForPage(type,page * PAGE_SIZE, PAGE_SIZE,searchQuery);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        if(getArguments()!=null)
        {
            type=getArguments().getInt(ARG_CATEGORY_TYPE);
        }
    }

}
