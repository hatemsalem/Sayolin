package com.zobonapp;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zobonapp.domain.BusinessEntity;
import com.zobonapp.domain.Contact;
import com.zobonapp.ui.ViewHolder;
import com.zobonapp.utils.ZobonApp;

/**
 * Created by hasalem on 24/12/2017.
 */

public class GeoContactHolder extends ViewHolder<Contact> implements View.OnClickListener
{
    private final static String TAG =GeoContactHolder.class.getSimpleName();
    protected TextView lblName;
    protected ImageButton btnAction;
    protected Contact contact;
    public GeoContactHolder(ViewGroup parent, @LayoutRes int layout)
    {

        super(parent, layout);
        lblName =itemView.findViewById(R.id.lblName);
        btnAction=itemView.findViewById(R.id.btnAction);
        btnAction.setOnClickListener(this);
    }

    @Override
    public void bind(Contact contact)
    {
        this.contact =contact;
//        lblName.setText(uri.getSchemeSpecificPart());
        lblName.setText(contact.getName());
    }

    @Override
    public void onClick(View v)
    {
        BusinessEntity entity=ZobonApp.getContext().getDataManager().findBusinessItemById(contact.getItemId().toString());
        ZobonApp.getIntentHelper().showMap(entity.getName(),contact.getName());
    }
}
