package com.zobonapp.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zobonapp.R;
import com.zobonapp.utils.QueryPreferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hasalem on 13/1/2018.
 */

public abstract class GenericPagerAdapter<VH extends ViewHolder<T>,T> extends RecyclerView.Adapter<VH>
{

    private ArrayList<T> entities=new ArrayList<>();
    protected String searchQuery;
    protected static String ARG_SEARCH_KEY="searchKey";
    protected static String ARG_ADAPTER_CLASS="adapterClass";
    protected final static int PAGE_SIZE=50;
    private Bundle arguments;
    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    public List<T> loadData(int page)
    {
        //default Implementation
        return new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(VH holder, int position)
    {
        holder.bind(entities.get(position));
    }

    @Override
    public int getItemCount()
    {
        return entities.size();
    }
    public void add(T entity)
    {
        entities.add(entity);
        notifyDataSetChanged();
    }
    public void  add(Collection<T> moreEntities)
    {
        entities.addAll(moreEntities);
        notifyDataSetChanged();
    }
    public void clear()
    {
        entities.clear();
        notifyDataSetChanged();
    }

    public Bundle getArguments()
    {
        return arguments;
    }

    public void setArguments(Bundle arguments)
    {
        this.arguments = arguments;
    }
    public  void onCreate()
    {
        Bundle args=getArguments();
        if(args!=null)
        {
            searchQuery= QueryPreferences.getSearchQuery(args.getString(ARG_SEARCH_KEY));
        }
    }

    public String getSearchKey()
    {
        return  getArguments()==null?null:getArguments().getString(ARG_SEARCH_KEY);
    }
    public void setSearchQuery(String searchQuery)
    {
        this.searchQuery = searchQuery;
    }


}