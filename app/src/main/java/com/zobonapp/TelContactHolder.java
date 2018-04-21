package com.zobonapp;

import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zobonapp.domain.Contact;
import com.zobonapp.ui.ViewHolder;
import com.zobonapp.utils.ZobonApp;

/**
 * Created by hasalem on 24/12/2017.
 */

public class TelContactHolder extends ViewHolder<Contact> implements View.OnClickListener
{
    private final static String TAG =TelContactHolder.class.getSimpleName();
    protected TextView lblName;
    protected TextView lblTel;
    protected ImageView btnAction;
    protected Contact contact;
    protected Uri uri;
    public TelContactHolder(ViewGroup parent, @LayoutRes int layout)
    {

        super(parent, layout);
        lblName =itemView.findViewById(R.id.lblName);
        lblTel=itemView.findViewById(R.id.lblTel);
        btnAction=itemView.findViewById(R.id.btnAction);
        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(Contact contact)
    {
        this.contact =contact;
        uri=Uri.parse(contact.getUri());
        lblTel.setText(uri.getSchemeSpecificPart());
        lblName.setText(contact.getName());

    }

    @Override
    public void onClick(View v)
    {
        ZobonApp.getIntentHelper().dial(uri);
    }
}
