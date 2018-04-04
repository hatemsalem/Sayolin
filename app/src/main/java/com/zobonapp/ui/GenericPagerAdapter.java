package com.zobonapp.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zobonapp.utils.QueryPreferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * Created by hasalem on 13/1/2018.
 */

public abstract class GenericPagerAdapter<VH extends ViewHolder<T>,T> extends RecyclerView.Adapter<VH>
{

    protected Vector<T> items =new Vector<>();
    protected String searchQuery;
    protected static String ARG_SEARCH_KEY="searchKey";
    protected static String ARG_ADAPTER_CLASS="adapterClass";
    protected final static int PAGE_SIZE=200;
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
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }
    public void add(T entity)
    {
        items.add(entity);
        notifyDataSetChanged();
    }
    public void  add(Collection<T> moreEntities)
    {
        items.addAll(moreEntities);
        notifyDataSetChanged();
    }
    public void clear()
    {
        items.clear();
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
        String searchKey=getSearchKey();
        if(searchKey!=null)
        {
            searchQuery= QueryPreferences.getSearchQuery(searchKey);
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


    public void refresh(T item)
    {
        int position= items.indexOf(item);
        notifyItemChanged(item,position);

     }
     protected void notifyItemChanged(T item,int position)
     {
         //Default implementation, do nothing
     }
}
