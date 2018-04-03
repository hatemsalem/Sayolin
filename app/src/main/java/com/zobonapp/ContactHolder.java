package com.zobonapp;

import android.net.Uri;
import android.support.annotation.LayoutRes;
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
    protected TextView lblName;
    protected Button btnAction;
    protected Contact contact;
    public ContactHolder(ViewGroup parent, @LayoutRes int layout)
    {

        super(parent, layout);
        lblName =itemView.findViewById(R.id.lblName);
        btnAction=itemView.findViewById(R.id.btnAction);
    }

    @Override
    public void bind(Contact cont)
    {
        this.contact =cont;
        Uri uri=Uri.parse(contact.getUri());
//        lblName.setText(uri.getSchemeSpecificPart());
        lblName.setText(cont.getName());
        btnAction.setText(uri.getScheme());
//        ZobonApp.getContext().getPicasso().load(R.drawable.img_running).error(R.drawable.notfoundimage).placeholder(R.drawable.placeholder   ).into(imgBanner);
    }
}
