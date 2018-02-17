package com.zobonapp.ui.hotline;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zobonapp.ItemDetailsActivity;
import com.zobonapp.R;
import com.zobonapp.domain.BusinessEntity;
import com.zobonapp.ui.ViewHolder;
import com.zobonapp.utils.ZobonApp;

import org.w3c.dom.Text;

/**
 * Created by hasalem on 24/12/2017.
 */

public class ItemHolder extends ViewHolder<BusinessEntity> implements View.OnClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener
{
    private final static String TAG =ItemHolder.class.getSimpleName();
//    protected Button btnCall;
    protected TextView btnCall;
    protected TextView lblName;

    protected TextView lblOffers;
    protected ImageView imgLogo;
    protected ImageView imgFavorite;
    protected BusinessEntity entity;
    public ItemHolder(ViewGroup parent, @LayoutRes int layout)
    {

        super(parent, layout);
        btnCall=itemView.findViewById(R.id.btnCall);
        btnCall.setOnClickListener(this);
        lblName=itemView.findViewById(R.id.lblName);
        lblName.setOnClickListener(this);
        lblOffers=itemView.findViewById(R.id.lblOffers);
        imgLogo=itemView.findViewById(R.id.imgLogo);
        imgLogo.setOnClickListener(this);
        imgFavorite=itemView.findViewById(R.id.imgFavorite);
        imgFavorite.setOnClickListener(this);
        btnCall.setOnCreateContextMenuListener(this);

    }

    @Override
    public void bind(BusinessEntity businessEntity)
    {
        this.entity=businessEntity;
        btnCall.setText(entity.getContact().getSchemeSpecificPart());
        lblName.setText(entity.getName());
//        lblOffers.setText(entity.getEnDesc());
        lblOffers.setText("5");
        if(entity.isFavorite())

        {
            imgFavorite.setImageDrawable(ZobonApp.getContext().getResources().getDrawable(R.drawable.ic_favorite_48dp));
        }
        else
        {
            imgFavorite.setImageDrawable(ZobonApp.getContext().getResources().getDrawable(R.drawable.ic_favorite_border_48dp));
        }
        final Uri  uri=Uri.parse("https://s3.amazonaws.com/static.zobonapp.com/initial/"+entity.getId().toString()+".webp");
        ZobonApp.getContext().getPicasso().load(uri).error(R.drawable.notfoundimage).placeholder(R.drawable.placeholder   ).into(imgLogo);

//        ZobonApp.getContext().getPicasso()
//                .load(uri)
//                .networkPolicy(NetworkPolicy.OFFLINE)
//                .into(imgLogo, new Callback() {
//                    @Override
//                    public void onSuccess() { }
//
//                    @Override
//                    public void onError() {
//                        // Try again online if cache failed
//                        ZobonApp.getContext().getPicasso()
//                                .load(uri).error(R.drawable.notfoundimage).placeholder(R.drawable.placeholder   )
//                                .into(imgLogo);
//                    }
//                });
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnCall:
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(entity.getContact());
                if (dialIntent.resolveActivity(ZobonApp.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(dialIntent);
                } else {
                    //TODO: to inform the user about this issue
                    Log.e(TAG, "Can't resolve app for ACTION_DIAL Intent.");
                }
                break;
            case R.id.imgFavorite:
                entity.setFavorite(!entity.isFavorite());
                ZobonApp.getContext().getDataManager().updateBusinessItem(entity);
//                if(entity.isFavorite())
//                {
//                    imgFavorite.setImageDrawable(ZobonApp.getContext().getResources().getDrawable(R.drawable.ic_favorite_48dp));
//                }
//                else
//                {
//                    imgFavorite.setImageDrawable(ZobonApp.getContext().getResources().getDrawable(R.drawable.ic_favorite_border_48dp));
//                }
                break;
            default:
                ItemDetailsActivity.start(v.getContext(),entity.getId().toString());
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        menu.setHeaderTitle("Select the action");
        menu.add(0,v.getId(),0,"Call").setOnMenuItemClickListener(this);
        menu.add(0,v.getId(),0,"SMS").setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        Toast.makeText(ZobonApp.getContext(),item.getTitle()+" "+entity.getName(),Toast.LENGTH_LONG).show();
        return true;
    }
}
