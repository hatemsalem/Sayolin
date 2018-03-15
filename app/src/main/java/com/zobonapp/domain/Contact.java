package com.zobonapp.domain;

import android.net.Uri;

import java.util.UUID;

/**
 * Created by hasalem on 12/13/2017.
 */

public class Contact
{
    private UUID id;
    private UUID itemId;
    private String uri;

    private String name;

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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
