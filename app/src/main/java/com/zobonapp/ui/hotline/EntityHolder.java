package com.zobonapp.ui.hotline;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zobonapp.EntityDetailsActivity;
import com.zobonapp.R;
import com.zobonapp.domain.BusinessEntity;
import com.zobonapp.ui.ViewHolder;
import com.zobonapp.utils.ZobonApp;

/**
 * Created by hasalem on 24/12/2017.
 */

public class EntityHolder extends ViewHolder<BusinessEntity> implements View.OnClickListener,View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener
{
    private final static String TAG =EntityHolder.class.getSimpleName();
//    protected Button imgCall;
    protected ImageView imgCall;
    protected ImageView imgShare;
    protected TextView lblName;
    protected TextView lblHotline;

    protected ImageView imgOffers;
    protected ImageView imgLogo;
    protected ImageView imgFavorite;
    protected BusinessEntity entity;
    public EntityHolder(ViewGroup parent, @LayoutRes int layout)
    {

        super(parent, layout);
        lblHotline=itemView.findViewById(R.id.lblHotline);
        lblHotline.setOnClickListener(this);

        imgCall =itemView.findViewById(R.id.imgCall);
        imgCall.setOnClickListener(this);
        imgCall.setOnCreateContextMenuListener(this);

        imgOffers =itemView.findViewById(R.id.imgOffers);
        imgOffers.setOnClickListener(this);

        imgLogo=itemView.findViewById(R.id.imgLogo);
        imgLogo.setOnClickListener(this);

        imgFavorite=itemView.findViewById(R.id.imgFavorite);
        imgFavorite.setOnClickListener(this);

        imgShare=itemView.findViewById(R.id.imgShare);
        imgShare.setOnClickListener(this);

        lblName=itemView.findViewById(R.id.lblName);

    }

    @Override
    public void bind(BusinessEntity businessEntity)
    {
        this.entity=businessEntity;
        String hotline=entity.getContact().getSchemeSpecificPart();
        lblHotline.setText(hotline);
        lblName.setText(entity.getName());
//        imgOffers.setText(entity.getEnDesc());
//        imgOffers.setText("5");
        if(entity.isFavorite())

        {
            imgFavorite.setImageDrawable(ZobonApp.getContext().getResources().getDrawable(R.drawable.fav_on));
        }
        else
        {
            imgFavorite.setImageDrawable(ZobonApp.getContext().getResources().getDrawable(R.drawable.fav_off));
        }
        if(entity.getOffers()>0)
            imgOffers.setVisibility(View.VISIBLE);
        else
            imgOffers.setVisibility(View.INVISIBLE);
        final Uri  uri=Uri.parse(ZobonApp.getAssetPath(entity.getId().toString()));
        ZobonApp.getPicasso().load(uri)
                .error(R.drawable.notfoundimage)
                .placeholder(R.drawable.placeholder   ).into(imgLogo);
//        ZobonApp.getContext().getPicasso().load(R.drawable.placeholder).into(imgLogo);


//        ZobonApp.getContext().getPicasso()
//                .load(uri)
//                .networkPolicy(NetworkPolicy.OFFLINE)
//                .into(imgItem, new Callback() {
//                    @Override
//                    public void onSuccess() { }
//
//                    @Override
//                    public void onError() {
//                        // Try again online if cache failed
//                        ZobonApp.getContext().getPicasso()
//                                .load(uri).error(R.drawable.notfoundimage).placeholder(R.drawable.placeholder   )
//                                .into(imgItem);
//                    }
//                });

    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imgCall:
            case R.id.lblHotline:
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
                ZobonApp.getDataManager().updateBusinessItem(entity);
//                if(entity.isFavorite())
//                {
//                    imgFavorite.setImageDrawable(ZobonApp.getContext().getResources().getDrawable(R.drawable.ic_favorite_48dp));
//                }
//                else
//                {
//                    imgFavorite.setImageDrawable(ZobonApp.getContext().getResources().getDrawable(R.drawable.ic_favorite_border_48dp));
//                }
                break;
            case R.id.imgShare:
                Toast.makeText(ZobonApp.getContext(),"Share...",Toast.LENGTH_SHORT).show();
                break;
            case R.id.imgOffers:
                Toast.makeText(ZobonApp.getContext(),ZobonApp.getContext().getResources().getQuantityString(R.plurals.offersCount,entity.getOffers(),entity.getName(),entity.getOffers()),Toast.LENGTH_SHORT).show();
                break;

            default:
                EntityDetailsActivity.start(v.getContext(),entity.getId().toString());
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
