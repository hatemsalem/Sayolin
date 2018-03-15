package com.zobonapp.domain;

import java.util.UUID;

/**
 * Created by hasalem on 12/13/2017.
 */

public class Category
{
    private UUID id;
    private String name;
    private String keywords;
    private int type;
    private int offers;
    private int entities;


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


    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getOffers()
    {
        return offers;
    }

    public void setOffers(int offers)
    {
        this.offers = offers;
    }

    public int getEntities()
    {
        return entities;
    }

    public void setEntities(int entities)
    {
        this.entities = entities;
    }

    public String getKeywords()
    {
        return keywords;
    }

    public void setKeywords(String keywords)
    {
        this.keywords = keywords;
    }
}
