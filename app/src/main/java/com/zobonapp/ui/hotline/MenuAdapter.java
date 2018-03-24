package com.zobonapp.ui.hotline;

import android.os.Bundle;
import android.view.ViewGroup;

import com.zobonapp.R;
import com.zobonapp.domain.Menu;
import com.zobonapp.domain.Offer;
import com.zobonapp.ui.GenericPagerAdapter;
import com.zobonapp.utils.ZobonApp;

import java.util.List;

/**
 * Created by hasalem on 14/1/2018.
 */

public class MenuAdapter extends GenericPagerAdapter<MenuHolder,Menu>
{
    private static final String ARG_CATEGORY_TYPE="categoryType";
    private int type;

    public static Bundle newArguments(int type)
    {
        Bundle arguments=new Bundle();
        arguments.putInt(ARG_CATEGORY_TYPE,type);
        arguments.putString(ARG_ADAPTER_CLASS,MenuAdapter.class.getName());
        return arguments;
    }
    @Override
    public MenuHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new MenuHolder(parent, R.layout.cell_menu);
    }
    @Override
    public List<Menu> loadData(int page)
    {
        return ZobonApp.getContext().getDataManager().findMenusForPage(page*PAGE_SIZE,PAGE_SIZE,searchQuery,null);
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
