package com.zobonapp.utils;

import com.zobonapp.domain.BusinessEntity;
import com.zobonapp.domain.Category;
import com.zobonapp.domain.Contact;
import com.zobonapp.domain.Offer;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by hasalem on 11/27/2017.
 */

public class DataCollection
{
    private Vector<Integer> vector;
    private HashMap<UUID,Vector<UUID>> map;
    private HashMap<UUID,Vector<UUID>> itemsCategories;
    private List<BusinessEntity> entities;
    private List<Category> categories;
    private List<Contact> contacts;
    private List<Offer> offers;
    public List<BusinessEntity> getEntities()
    {
        return entities;
    }

    public List<Category> getCategories()
    {
        return categories;
    }

    public List<Contact> getContacts()
    {
        return contacts;
    }

    public List<Offer> getOffers()
    {
        return offers;
    }

    public Vector<Integer> getVector()
    {
        return vector;
    }

    public HashMap<UUID, Vector<UUID>> getMap()
    {
        return map;
    }

    public HashMap<UUID, Vector<UUID>> getItemsCategories()
    {
        return itemsCategories;
    }
}
