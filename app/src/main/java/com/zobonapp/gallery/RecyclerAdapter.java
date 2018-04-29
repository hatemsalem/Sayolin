package com.zobonapp.gallery;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alexvasilkov.android.commons.ui.Views;
import com.zobonapp.R;
import com.zobonapp.utils.ZobonApp;

import java.util.Locale;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    private int counts;
    private String baseUrl;
    private final OnPaintingClickListener listener;

    RecyclerAdapter(int counts, String baseUrl, OnPaintingClickListener listener)
    {
        this.counts = counts;
        this.baseUrl = baseUrl;
        this.listener = listener;
    }

    @Override
    public int getItemCount()
    {
        return counts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ViewHolder holder = new ViewHolder(parent);
        holder.itemView.setOnClickListener(this::onItemClick);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        // Storing item position for click handler
        holder.itemView.setTag(R.id.tag_item, position);
        Uri uri=Uri.parse(String.format(Locale.US,"%s%03d.%s",baseUrl,position+1,"jpg"));
        ZobonApp.getPicasso()
                .load(uri)
                .resize(ZobonApp.calculateColumnWidth(2),0)
                .centerInside()
                .error(R.drawable.notfoundimage)
                .placeholder(R.drawable.placeholder   )
                .into(holder.image);

    }

    private void onItemClick(@NonNull View view)
    {
        int pos = (Integer) view.getTag(R.id.tag_item);
        listener.onPaintingClick(pos);
    }

    static ImageView getImageView(RecyclerView.ViewHolder holder)
    {
        return ((ViewHolder) holder).image;
    }


    static class ViewHolder extends RecyclerView.ViewHolder
    {
        final ImageView image;

        ViewHolder(ViewGroup parent)
        {
            super(Views.inflate(parent, R.layout.list_image_item));
            image = itemView.findViewById(R.id.list_image);
        }
    }

    interface OnPaintingClickListener
    {
        void onPaintingClick(int position);
    }

}
