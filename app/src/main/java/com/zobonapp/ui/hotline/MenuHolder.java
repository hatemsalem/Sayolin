package com.zobonapp.ui.hotline;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zobonapp.R;
import com.zobonapp.domain.Menu;
import com.zobonapp.ui.ViewHolder;
import com.zobonapp.utils.ZobonApp;

/**
 * Created by hasalem on 14/1/2018.
 */

public class MenuHolder extends ViewHolder<Menu> implements View.OnClickListener
{
    private final static String TAG=MenuHolder.class.getSimpleName();
    protected TextView lblName;
    protected TextView lblPages;
    protected ImageView imgItem;
    protected Menu menu;
    public MenuHolder(ViewGroup parent, int layout)
    {
        super(parent, layout);
        itemView.setOnClickListener(this);
        lblName=itemView.findViewById(R.id.lblName);
        lblPages=itemView.findViewById(R.id.lblPages);
        imgItem =itemView.findViewById(R.id.imgItem);
    }

    @Override
    public void bind(Menu menu)
    {
        this.menu =menu;
        lblName.setText(menu.getName());
        lblPages.setText("pages:"+menu.getPages());
        Uri uri=Uri.parse("https://s3.amazonaws.com/static.zobonapp.com/menu/"+menu.getId().toString()+".webp");
        ZobonApp.getContext().getPicasso().load(uri).error(R.drawable.notfoundimage).placeholder(R.drawable.placeholder   ).into(imgItem);
    }

    @Override
    public void onClick(View v)
    {
        ItemsActivity.start(v.getContext(), menu.getId().toString());

    }
}
