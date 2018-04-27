package com.zobonapp.ui.hotline;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zobonapp.BuildConfig;
import com.zobonapp.R;
import com.zobonapp.domain.Offer;
import com.zobonapp.gallery.GalleryActivity2;
import com.zobonapp.gallery.GalleryActivity3;
import com.zobonapp.ui.ViewHolder;
import com.zobonapp.utils.ZobonApp;

/**
 * Created by hasalem on 14/1/2018.
 */

public class OfferHolder extends ViewHolder<Offer> implements View.OnClickListener
{
    private final static String TAG=OfferHolder.class.getSimpleName();
    protected TextView lblName;
    protected TextView lblPages;
    protected ImageView imgItem;
    protected Offer offer;
    public OfferHolder(ViewGroup parent, int layout)
    {
        super(parent, layout);
        itemView.setOnClickListener(this);
        lblName=itemView.findViewById(R.id.lblName);
        lblPages=itemView.findViewById(R.id.lblPages);
        imgItem =itemView.findViewById(R.id.imgItem);
    }

    @Override
    public void bind(Offer offer)
    {
        this.offer =offer;
        lblName.setText(offer.getName());
        lblPages.setText("pages:"+offer.getPages());
        Uri uri=Uri.parse(BuildConfig.BASE_URL+"/resources/offers/"+offer.getId().toString()+"/thumbnail.jpg");
        int size=ZobonApp.calculateColumnWidth(2);
        ZobonApp.getPicasso()
                .load(uri)
                .resize(ZobonApp.calculateColumnWidth(2),0)
//                .fit()
                .centerInside()
                .error(R.drawable.notfoundimage)
                .placeholder(R.drawable.placeholder   )
                .into(imgItem);
    }

    @Override
    public void onClick(View v)
    {
//        GalleryActivity.start(v.getContext(),BuildConfig.BASE_URL+"/resources/offers/"+offer.getId()+"/",offer.getPages(),"jpg");
        Context ctx=v.getContext();
//        GalleryActivity2.start(ctx,BuildConfig.BASE_URL+"/resources/offers/"+offer.getId()+"/",offer.getName(),offer.getPages(),"jpg");
        GalleryActivity3.start(ctx,BuildConfig.BASE_URL+"/resources/offers/"+offer.getId()+"/",offer.getName(),offer.getPages(),"jpg");
    }

}
