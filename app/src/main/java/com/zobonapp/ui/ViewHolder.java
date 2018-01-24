package com.zobonapp.ui;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zobonapp.R;

/**
 * Created by hasalem on 13/1/2018.
 */

public abstract class ViewHolder<T> extends RecyclerView.ViewHolder
{
    public ViewHolder(ViewGroup parent,@LayoutRes int layout)
    {
        super(LayoutInflater.from(parent.getContext()).inflate(layout,parent,false));

    }
    public abstract void bind(T t);

}
