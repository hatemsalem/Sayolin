package com.zobonapp.ui.hotline;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zobonapp.R;
import com.zobonapp.domain.BusinessEntity;
import com.zobonapp.ui.ViewHolder;
import com.zobonapp.utils.ZobonApp;

/**
 * Created by hasalem on 24/12/2017.
 */

public class ItemHolder extends ViewHolder<BusinessEntity> implements View.OnClickListener
{
    private final static String TAG =ItemHolder.class.getSimpleName();
    protected Button btnCall;
    protected TextView lblName;
    protected TextView lblOffers;
    protected TextView lblFavorite;
    protected ImageView imgLogo;
    protected BusinessEntity entity;
    public ItemHolder(ViewGroup parent, @LayoutRes int layout)
    {

        super(parent, layout);

        btnCall=itemView.findViewById(R.id.btnCall);
        btnCall.setOnClickListener(this);
        lblName=itemView.findViewById(R.id.lblName);
        lblOffers=itemView.findViewById(R.id.lblOffers);
        imgLogo=itemView.findViewById(R.id.imgLogo);
        lblFavorite =itemView.findViewById(R.id.lblFavorite);
        lblFavorite.setOnClickListener(this);
    }

    @Override
    public void bind(BusinessEntity businessEntity)
    {
        this.entity=businessEntity;
        btnCall.setText(entity.getHotline());
        lblName.setText(entity.getName());
        lblOffers.setText(entity.getEnDesc());
        lblFavorite.setText(String.valueOf(entity.isFavorite()));
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
                dialIntent.setData(Uri.parse("tel:"+btnCall.getText().toString()));
                if (dialIntent.resolveActivity(ZobonApp.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(dialIntent);
                } else {
                    //TODO: to inform the user about this issue
                    Log.e(TAG, "Can't resolve app for ACTION_DIAL Intent.");
                }
            case R.id.lblFavorite:
                entity.setFavorite(!entity.isFavorite());
                ZobonApp.getContext().getDataManager().updateBusinessItem(entity);
                lblFavorite.setText(String.valueOf(entity.isFavorite()));

        }

    }
}
