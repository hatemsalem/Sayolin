package com.zobonapp.domain;

import java.util.UUID;

/**
 * Created by hasalem on 12/13/2017.
 */

public class Category
{
    private int pk;
    private UUID id;
    private String name;
    private String arName;
    private String enName;
    private String searchText;
    private int type;
    private int offers;
    private int entities;

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

    public String getArName()
    {
        return arName;
    }

    public void setArName(String arName)
    {
        this.arName = arName;
    }

    public String getEnName()
    {
        return enName;
    }

    public void setEnName(String enName)
    {
        this.enName = enName;
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

    public String getSearchText()
    {
        return searchText;
    }

    public void setSearchText(String searchText)
    {
        this.searchText = searchText;
    }
}
