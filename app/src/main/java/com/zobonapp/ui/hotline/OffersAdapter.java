package com.zobonapp.ui.hotline;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zobonapp.R;
import com.zobonapp.domain.Category;
import com.zobonapp.domain.Offer;
import com.zobonapp.manager.OffersChangeEvent;
import com.zobonapp.ui.GenericPagerAdapter;
import com.zobonapp.utils.ZobonApp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by hasalem on 14/1/2018.
 */

public class OffersAdapter extends GenericPagerAdapter<OfferHolder,Offer>
{
    private static final String ARG_CATEGORY="offer";
    private String categoryId;

    public static Bundle newArguments(String categoryId,String searchKey)
    {
        Bundle arguments=new Bundle();
        arguments.putString(ARG_CATEGORY,categoryId);
        arguments.putString(ARG_SEARCH_KEY,searchKey);
        arguments.putString(ARG_ADAPTER_CLASS,OffersAdapter.class.getName());
        arguments.putInt(ARGS_COLUMNS_COUNT,2);
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
        return ZobonApp.getDataManager().findOffersForPage(page*PAGE_SIZE,PAGE_SIZE,searchQuery,categoryId);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        if(getArguments()!=null)
        {
            categoryId=getArguments().getString(ARG_CATEGORY);
        }
    }

    @Override
    protected void notifyItemChanged(Offer item, int position)
    {
        super.notifyItemChanged(item, position);

    }
    @Override
    public void onStart()
    {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop()
    {
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInitialized(OffersChangeEvent event)
    {
//        refresh(event.getItem());
//        clear();
//        add(loadData(0));
//        notifyItemInserted(0);

        Toast.makeText(ZobonApp.getContext(),ZobonApp.getContext().getString(R.string.newOffersNotification),Toast.LENGTH_LONG).show();
    }
}
