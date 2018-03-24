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
        return new ContactHolder(parent,R.layout.cell_contact);
    }

    @Override
    public List loadData(int page)
    {
       return ZobonApp.getContext().getDataManager().findContactsForItem(itemId);
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
