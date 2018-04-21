package com.zobonapp;

import android.os.Bundle;
import android.view.ViewGroup;

import com.zobonapp.domain.Contact;
import com.zobonapp.ui.GenericPagerAdapter;
import com.zobonapp.ui.ViewHolder;
import com.zobonapp.utils.ZobonApp;

import java.util.List;

/**
 * Created by hasalem on 27/1/2018.
 */

public class ContactsAdapter extends GenericPagerAdapter<ViewHolder<Contact>,Contact>
{
    private static String ARG_ITEM="item";
    private static final int TEL_VIEW_TYPE=0;
    private static final int GEO_VIEW_TYPE=1;



    private String itemId;

    public static Bundle newArguments(String itemId, String searchKey)
    {
        Bundle arguments=new Bundle();
        arguments.putString(ARG_ITEM,itemId);
        arguments.putString(ARG_SEARCH_KEY,searchKey);
        arguments.putString(ARG_ADAPTER_CLASS,Contact.class.getName());
        return arguments;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        switch (viewType)
        {
            case GEO_VIEW_TYPE:
                return new GeoContactHolder(parent,R.layout.cell_geo_contact);
            case TEL_VIEW_TYPE:
            default:
                return new TelContactHolder(parent,R.layout.cell_tel_contact);

        }

    }

    @Override
    public int getItemViewType(int position)
    {
        switch (items.get(position).getUri().substring(0,3).toLowerCase())
        {
            case "geo":
                return GEO_VIEW_TYPE;

            case "tel":
            default:
                return TEL_VIEW_TYPE;
        }
    }

    @Override
    public List loadData(int page)
    {

       return ZobonApp.getDataManager().findContactsForItem(itemId,searchQuery);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Bundle args=getArguments();
        if(args!=null)
        {
            itemId=args.getString(ARG_ITEM);
        }
    }
}
