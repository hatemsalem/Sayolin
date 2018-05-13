package com.zobonapp.gallery;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexvasilkov.android.commons.ui.Views;
import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.zobonapp.R;
import com.zobonapp.gallery.adapter.DefaultSettingsController;
import com.zobonapp.gallery.adapter.SettingsController;
import com.zobonapp.utils.ZobonApp;

import java.util.Locale;

class PagerAdapter extends RecyclePagerAdapter<PagerAdapter.ViewHolder>
{

    private final ViewPager viewPager;
    private final String baseUrl;
    private final int pages;
    private SettingsController settingsController=new DefaultSettingsController();

    PagerAdapter(ViewPager pager, int pages, String baseUrl)
    {
        this.viewPager = pager;
        this.pages = pages;
        this.baseUrl = baseUrl;
    }

    @Override
    public int getCount()
    {
        return pages;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup container)
    {
        ViewHolder holder = new ViewHolder(container);
        holder.image.getController().enableScrollInViewPager(viewPager);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        settingsController.apply(holder.image);
        Uri uri=Uri.parse(String.format(Locale.US,"%s%03d.%s",baseUrl,position+1,"jpg"));
//        int size=ZobonApp.calculateColumnWidth(1);
        ZobonApp.getPicasso()
                .load(uri)
//                .resize(500,0)
//                .centerInside()
                .error(R.drawable.notfoundimage)
                .placeholder(R.drawable.placeholder   )
                .into(holder.image);
        ;
        holder.lblPage.setText(ZobonApp.getContext().getResources().getString(R.string.pageCount,position+1,getCount()));

    }

    static GestureImageView getImageView(RecyclePagerAdapter.ViewHolder holder)
    {
        return ((ViewHolder) holder).image;
    }


    static class ViewHolder extends RecyclePagerAdapter.ViewHolder
    {
        final GestureImageView image;
        final TextView lblPage;

        ViewHolder(ViewGroup container)
        {
            super(Views.inflate(container, R.layout.cell_offer_page));
//            super(LayoutInflater.from(container.getContext()).inflate(R.layout.cell_offer_page,container));
//            super(new GestureImageView(container.getContext()));
//            image = (GestureImageView) itemView;
            image=itemView.findViewById(R.id.imgOfferPage);
            lblPage=itemView.findViewById(R.id.lblPage);
        }
    }

}
