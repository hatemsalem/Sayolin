package com.zobonapp.domain;

import java.util.UUID;

/**
 * Created by hasalem on 12/13/2017.
 */

public class Category extends AbstractItem
{
    private int type;
    private int offers;
    private int entities;




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

}
