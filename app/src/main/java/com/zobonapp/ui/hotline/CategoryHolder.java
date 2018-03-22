package com.zobonapp.ui.hotline;

import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    protected TextView lblOffers;
    protected ImageView imgLogo;
    protected Category category;
    public CategoryHolder(ViewGroup parent, int layout)
    {
        super(parent, layout);
        itemView.setOnClickListener(this);
        lblName=itemView.findViewById(R.id.lblName);
        lblEntities=itemView.findViewById(R.id.lblEntities);
        lblOffers=itemView.findViewById(R.id.lblOffers);
        imgLogo=itemView.findViewById(R.id.imgLogo);
    }

    @Override
    public void bind(Category category)
    {
        this.category=category;
        lblName.setText(category.getName());
        lblEntities.setText(category.getEntities()+" item");
        lblOffers.setText(category.getOffers()+" offer");
        Uri uri=Uri.parse("https://s3.amazonaws.com/static.zobonapp.com/offer/"+category.getId().toString()+".webp");
        ZobonApp.getContext().getPicasso().load(uri).error(R.drawable.notfoundimage).placeholder(R.drawable.placeholder   ).into(imgLogo);
    }

    @Override
    public void onClick(View v)
    {
        ItemsActivity.start(v.getContext(),category.getId().toString());

    }
}
