package com.zobonapp.ui.hotline;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zobonapp.utils.ZobonApp;

/**
 * Created by Admin on 4/22/2018.
 */

public class OffersFragment extends ItemsFragment
{
    @Override
    public RecyclerView.LayoutManager getLayoutManager()
    {
        if(super.getLayoutManager()==null)
            setLayoutManager(new GridLayoutManager(getContext(), ZobonApp.calculateColumns(300)));
        return super.getLayoutManager();
    }
}
