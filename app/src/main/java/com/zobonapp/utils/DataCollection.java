package com.zobonapp.utils;

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
    private HashMap<UUID, Vector<UUID>> map;

    private int steps=0;
    private long latestUpdate=0;

    private String type="";
    private HashMap<String, Vector<String>> itemsCategories = new HashMap<>();
    private List<HashMap<String, ?>> entities = new Vector<>();
    private List<HashMap<String, ?>> categories = new Vector<>();
    private List<HashMap<String, ?>> offers = new Vector<>();
    private List<HashMap<String, ?>> menus = new Vector<>();

    private Vector<String> deletedEntities=new Vector<>();
    private Vector<String> deletedCategories=new Vector<>();
    private Vector<String> deletedOffers=new Vector<>();


    private List<HashMap<String, ?>> contacts = new Vector<>();


    public List<HashMap<String, ?>> getEntities()
    {
        return entities;
    }

    public List<HashMap<String, ?>> getCategories()
    {
        return categories;
    }

    public List<HashMap<String, ?>> getContacts()
    {
        return contacts;
    }

    public List<HashMap<String, ?>> getOffers()
    {
        return offers;
    }
    public List<HashMap<String, ?>> getMenus()
    {
        return menus;
    }

    public Vector<Integer> getVector()
    {
        return vector;
    }

    public HashMap<UUID, Vector<UUID>> getMap()
    {
        return map;
    }

    public HashMap<String, Vector<String>> getItemsCategories()
    {
        return itemsCategories;
    }


    public int getSteps()
    {
        return steps;
    }


    public long getLatestUpdate()
    {
        return latestUpdate;
    }

    public Vector<String> getDeletedEntities()
    {
        return deletedEntities;
    }

    public Vector<String> getDeletedCategories()
    {
        return deletedCategories;
    }

    public String getType()
    {
        return type;
    }

    public Vector<String> getDeletedOffers()
    {
        return deletedOffers;
    }
}
