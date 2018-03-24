package com.zobonapp.ui.hotline;

import android.os.Bundle;
import android.view.ViewGroup;

import com.zobonapp.R;
import com.zobonapp.domain.BusinessEntity;
import com.zobonapp.ui.GenericPagerAdapter;
import com.zobonapp.utils.ZobonApp;

import java.util.List;

/**
 * Created by hasalem on 24/12/2017.
 */

public class BusinessEntityAdapter extends GenericPagerAdapter<ItemHolder,BusinessEntity>
{
    private static String ARG_CATEGORY="menu";

    private String categoryId;


    public static Bundle newArguments(String categoryId,String searchKey)
    {
        Bundle arguments=new Bundle();
        arguments.putString(ARG_CATEGORY,categoryId);
        arguments.putString(ARG_SEARCH_KEY,searchKey);
        arguments.putString(ARG_ADAPTER_CLASS,BusinessEntityAdapter.class.getName());
        return arguments;
    }
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ItemHolder(parent, R.layout.cell_hotline3);
    }

    @Override
    public List<BusinessEntity> loadData(int page)
    {

        return ZobonApp.getContext().getDataManager().findBusinessEntitiesForPage(page * PAGE_SIZE, PAGE_SIZE,searchQuery,categoryId);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Bundle args=getArguments();
        if(args!=null)
        {
            categoryId=args.getString(ARG_CATEGORY);
        }
    }

    @Override
    protected void notifyItemChanged(BusinessEntity item, int position)
    {
        super.notifyItemChanged(item, position);
        if(position>=0)
        {
            entities.set(position,item);
            notifyItemChanged(position);
        }
    }
}
