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


    private HashMap<String, Vector<String>> itemsCategories = new HashMap<>();
    private List<HashMap<String, ?>> entities = new Vector<>();
    private List<HashMap<String, ?>> categories = new Vector<>();

    private Vector<String> deletedEntities=new Vector<>();
    private Vector<String> deletedCategories=new Vector<>();


    private List<HashMap<String, ?>> contacts = new Vector<>();

    private List<Offer> offers;

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
}
