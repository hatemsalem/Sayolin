package com.zobonapp.ui.hotline;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zobonapp.R;
import com.zobonapp.domain.Category;
import com.zobonapp.ui.ViewHolder;
import com.zobonapp.utils.ZobonApp;

/**
 * Created by hasalem on 14/1/2018.
 */

public class CategoryHolder extends ViewHolder<Category> implements View.OnClickListener
{
    private final static String TAG=CategoryHolder.class.getSimpleName();
    protected TextView lblName;
    protected TextView lblEntities;
    protected ImageView imgLogo;
    protected Category category;
    public CategoryHolder(ViewGroup parent, int layout)
    {
        super(parent, layout);
        itemView.setOnClickListener(this);
        lblName=itemView.findViewById(R.id.lblName);
        lblEntities=itemView.findViewById(R.id.lblEntities);
        imgLogo=itemView.findViewById(R.id.imgLogo);
    }

    @Override
    public void bind(Category category)
    {
        this.category=category;
        lblName.setText(category.getName());
        lblEntities.setText(itemView.getResources().getQuantityString(R.plurals.itemsCount,category.getEntities(),category.getEntities()));
        final Uri  uri=Uri.parse(ZobonApp.getAssetPath(category.getId().toString()));
        ZobonApp.getPicasso().load(uri)
                .error(R.drawable.notfoundimage)
                .placeholder(R.drawable.placeholder   ).into(imgLogo);
    }

    @Override
    public void onClick(View v)
    {
        switch (category.getType())
        {
            case 1001:
                EntitiesActivity.start(v.getContext(),category.getId().toString());
                break;
            case 3001:
                OffersActivity.start(v.getContext(),category.getId().toString());
                break;
        }


    }
}
