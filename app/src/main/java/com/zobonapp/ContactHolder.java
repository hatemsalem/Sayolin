package com.zobonapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zobonapp.domain.Contact;
import com.zobonapp.ui.ViewHolder;
import com.zobonapp.utils.ZobonApp;

/**
 * Created by hasalem on 24/12/2017.
 */

public class ContactHolder extends ViewHolder<Contact>
{
    private final static String TAG =ContactHolder.class.getSimpleName();
    protected TextView lblTitle;
    protected ImageView imgBanner;
    protected Contact contact;
    public ContactHolder(ViewGroup parent, @LayoutRes int layout)
    {

        super(parent, layout);
        lblTitle=itemView.findViewById(R.id.lblTitle);
        imgBanner=itemView.findViewById(R.id.contactImage);
    }

    @Override
    public void bind(Contact cont)
    {
        this.contact =cont;
        Uri uri=Uri.parse(contact.getUri());
        lblTitle.setText(uri.getSchemeSpecificPart());
        ZobonApp.getContext().getPicasso().load(R.drawable.img_running).error(R.drawable.notfoundimage).placeholder(R.drawable.placeholder   ).into(imgBanner);
    }
}
