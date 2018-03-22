package com.zobonapp.ui.hotline;

import android.os.Bundle;
import android.view.ViewGroup;

import com.zobonapp.R;
import com.zobonapp.domain.Category;
import com.zobonapp.domain.Offer;
import com.zobonapp.ui.GenericPagerAdapter;
import com.zobonapp.utils.ZobonApp;

import java.util.List;

/**
 * Created by hasalem on 14/1/2018.
 */

public class OffersAdapter extends GenericPagerAdapter<OfferHolder,Offer>
{
    private static final String ARG_CATEGORY_TYPE="categoryType";
    private int type;

    public static Bundle newArguments(int type)
    {
        Bundle arguments=new Bundle();
        arguments.putInt(ARG_CATEGORY_TYPE,type);
        arguments.putString(ARG_ADAPTER_CLASS,OffersAdapter.class.getName());
        return arguments;
    }
    @Override
    public OfferHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new OfferHolder(parent, R.layout.cell_offer);
    }
    @Override
    public List<Offer> loadData(int page)
    {
//        return ZobonApp.getContext().getDataManager().findCategoriesForPage(type,page * PAGE_SIZE, PAGE_SIZE,searchQuery);
        return null;
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
