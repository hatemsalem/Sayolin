package com.zobonapp.domain;

import android.net.Uri;

import java.util.UUID;

/**
 * Created by hasalem on 11/16/2017.
 */

public class BusinessEntity
{
    private int pk;
    private UUID id;
    private String name;
    private String desc;
    private String arName;
    private String arDesc;
    private String enName;
    private String enDesc;
    private boolean favorite;
    private int offers;
    private UUID contactId;
    private Uri contact;

    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getArName()
    {
        return arName;
    }

    public void setArName(String arName)
    {
        this.arName = arName;
    }

    public String getArDesc()
    {
        return arDesc;
    }

    public void setArDesc(String arDesc)
    {
        this.arDesc = arDesc;
    }

    public String getEnName()
    {
        return enName;
    }

    public void setEnName(String enName)
    {
        this.enName = enName;
    }

    public String getEnDesc()
    {
        return enDesc;
    }

    public void setEnDesc(String enDesc)
    {
        this.enDesc = enDesc;
    }


    public boolean isFavorite()
    {
        return favorite;
    }

    public void setFavorite(boolean favorite)
    {
        this.favorite = favorite;
    }

    public int getOffers()
    {
        return offers;
    }

    public void setOffers(int offers)
    {
        this.offers = offers;
    }

    public UUID getContactId()
    {
        return contactId;
    }

    public void setContactId(UUID contactId)
    {
        this.contactId = contactId;
    }

    public Uri getContact()
    {
        return contact;
    }

    public void setContact(Uri contact)
    {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusinessEntity that = (BusinessEntity) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode()
    {
        return getId().hashCode();
    }
}
