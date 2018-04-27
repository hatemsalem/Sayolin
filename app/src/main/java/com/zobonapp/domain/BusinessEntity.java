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
    private String webSite;
    private String fbPage;

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

    public String getWebSite()
    {
        return webSite;
    }

    public void setWebSite(String webSite)
    {
        this.webSite = webSite;
    }

    public String getFbPage()
    {
        return fbPage;
    }

    public void setFbPage(String fbPage)
    {
        this.fbPage = fbPage;
    }
}
