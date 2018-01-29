package com.zobonapp.domain;

import android.net.Uri;

import java.util.UUID;

/**
 * Created by hasalem on 12/13/2017.
 */

public class Contact
{
    private int pk;
    private UUID id;
    private UUID itemId;
    private String uri;

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

    public UUID getItemId()
    {
        return itemId;
    }

    public void setItemId(UUID itemId)
    {
        this.itemId = itemId;
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }
}
