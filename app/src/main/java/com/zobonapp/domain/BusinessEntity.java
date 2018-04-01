package com.zobonapp.domain;

import android.net.Uri;

import java.util.UUID;

/**
 * Created by hasalem on 11/16/2017.
 */

public class BusinessEntity extends  AbstractItem
{
    private int offers;
    private Uri contact;

    public int getOffers()
    {
        return offers;
    }

    public void setOffers(int offers)
    {
        this.offers = offers;
    }

    public Uri getContact()
    {
        return contact;
    }

    public void setContact(Uri contact)
    {
        this.contact = contact;
    }





}
