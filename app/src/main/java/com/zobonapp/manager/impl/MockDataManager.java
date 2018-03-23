package com.zobonapp.manager.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.zobonapp.R;
import com.zobonapp.db.DatabaseHelper;
import com.zobonapp.db.DbSchema;
import com.zobonapp.db.DbSchema.L10NCols;
import com.zobonapp.domain.BusinessEntity;
import com.zobonapp.domain.Category;
import com.zobonapp.domain.Contact;
import com.zobonapp.domain.Offer;
import com.zobonapp.manager.DataManager;
import com.zobonapp.manager.ItemChangeEvent;
import com.zobonapp.utils.ZobonApp;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import static com.zobonapp.db.DbSchema.*;
import static com.zobonapp.db.DbSchema.BusinessEntityTable;
import static com.zobonapp.db.DbSchema.CategoryTable;
import static com.zobonapp.db.DbSchema.ContactTable;
import static com.zobonapp.db.DbSchema.ItemCategoryTable;

/**
 * Created by hasalem on 11/26/2017.
 */

public class MockDataManager implements DataManager
{
    public  List<BusinessEntity> findBusinessEntitiesForPage(int offset,int limit,String searchQuery,String categoryId)
    {
        String query=null;
        Vector<String> queryArgs=new Vector<>();
        if(searchQuery==null)
            searchQuery="";
        searchQuery= "%"+searchQuery+"%";
        queryArgs.add(searchQuery);
        queryArgs.add(searchQuery);
        //TODO: order by should be handled for consistent results.
        if(TextUtils.isEmpty(categoryId))
        {
            query= ZobonApp.getContext().getResources().getString(R.string.sql_findBusinessEntities,offset,limit);
        }
        else
        {
            query= ZobonApp.getContext().getResources().getString(R.string.sql_findBusinessEntitiesInCategory,offset,limit);
            queryArgs.add(categoryId);
        }

        Cursor cursor=DatabaseHelper.getInstance().getReadableDatabase().rawQuery(query, queryArgs.toArray(new String[]{}));
        List<BusinessEntity> entities=new ArrayList<>();
        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                entities.add(getBusinessEntity(cursor));
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
        return entities;

    }

    @Override
    public List<BusinessEntity> findFavoriteEntitiesForPage(int offset, int limit, String searchQuery, String categoryId)
    {
        String query=null;
        Vector<String> queryArgs=new Vector<>();
        if(searchQuery==null)
            searchQuery="";
        searchQuery= "%"+searchQuery+"%";
        queryArgs.add(searchQuery);
        queryArgs.add(searchQuery);
        //TODO: order by should be handled for consistent results.
        if(TextUtils.isEmpty(categoryId))
        {
            query= ZobonApp.getContext().getResources().getString(R.string.sql_findFavoriteBusinessEntities,offset,limit);
        }
        else
        {
            query= ZobonApp.getContext().getResources().getString(R.string.sql_findBusinessEntitiesInCategory,offset,limit);
            queryArgs.add(categoryId);
        }

        Cursor cursor=DatabaseHelper.getInstance().getReadableDatabase().rawQuery(query, queryArgs.toArray(new String[]{}));
        List<BusinessEntity> entities=new ArrayList<>();
        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                entities.add(getBusinessEntity(cursor));
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
        return entities;
    }

    @Override
    public List<Offer> findOffersForPage(int offset, int limit, String searchQuery, String categoryId)
    {
        String query=null;
        Vector<String> queryArgs=new Vector<>();
        if(searchQuery==null)
            searchQuery="";
        searchQuery= "%"+searchQuery+"%";
        queryArgs.add(searchQuery);
        queryArgs.add(searchQuery);
        //TODO: order by should be handled for consistent results.
        if(TextUtils.isEmpty(categoryId))
        {
            query= ZobonApp.getContext().getResources().getString(R.string.sql_findOfferss,offset,limit);
        }
        else
        {
            query= ZobonApp.getContext().getResources().getString(R.string.sql_findBusinessEntitiesInCategory,offset,limit);
            queryArgs.add(categoryId);
        }

        Cursor cursor=DatabaseHelper.getInstance().getReadableDatabase().rawQuery(query, queryArgs.toArray(new String[]{}));
        List<Offer> offers=new ArrayList<>();
        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                offers.add(getOffer(cursor));
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
        return offers;
    }



    @Override
    public List<Category> findCategoriesForPage(int type,int offset, int limit,String searchQuery)
    {
        String query=null;
        Vector<String> queryArgs=new Vector<>();
        if(searchQuery==null)
            searchQuery="";
        searchQuery= "%"+searchQuery+"%";
        queryArgs.add(searchQuery);
        queryArgs.add(searchQuery);
        queryArgs.add(searchQuery);

        query= ZobonApp.getContext().getResources().getString(R.string.sql_findCategories,offset,limit,type);

        Cursor cursor=DatabaseHelper.getInstance().getReadableDatabase().rawQuery(query,queryArgs.toArray(new String[]{}));
        List<Category> entities=new ArrayList<>();
        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                entities.add(getCategory(cursor));
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
        return entities;
    }

    @Override
    public List<Contact> findContactsForItem(String itemId)
    {
        String query=null;
        Vector<String> queryArgs=new Vector<>();
//        String searchQuery=null;
//        if(searchQuery==null)
//            searchQuery="";
//        searchQuery= "%"+searchQuery+"%";
//        queryArgs.add(searchQuery);
//        queryArgs.add(searchQuery);
//        queryArgs.add(searchQuery);
        queryArgs.add(itemId);
        query= ZobonApp.getContext().getResources().getString(R.string.sql_findContactsForItem);
        Cursor cursor=DatabaseHelper.getInstance().getReadableDatabase().rawQuery(query,queryArgs.toArray(new String[]{}));
        List<Contact> entities=new ArrayList<>();
        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                entities.add(getContact(cursor));
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
        return entities;
    }

    @Override
    public void deleteItems(List<String> items)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();
        database.beginTransaction();
        for (String item:items)
        {
            database.delete(ContactTable.NAME,"itemId=?",new String[]{item});
            database.delete(BusinessEntityTable.NAME,"id=?",new String[]{item});
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    @Override
    public void deleteCategories(List<String> categories)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();
        database.beginTransaction();
        for (String category:categories)
        {
            database.delete(CategoryTable.NAME,"id=?",new String[]{category});
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    @Override
    public void populateItemCategoryRelations(HashMap<String, Vector<String>> objects)
    {

        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();
        database.beginTransaction();
        for(String item:objects.keySet())
        {
            for (String category:objects.get(item))
            {
                ContentValues values=new ContentValues();
                values.put(ItemCategoryTable.Cols.ITEM_ID,item);
                values.put(ItemCategoryTable.Cols.CATEGORY_ID,category);
                long i=database.insert(ItemCategoryTable.NAME,null,values);
                Log.d("DataManager","inserted records:"+i);
            }
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    @Override
    public BusinessEntity findBusinessItemById(String id)
    {
        SQLiteDatabase database=DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor=database.rawQuery(ZobonApp.getContext().getResources().getString(R.string.sql_findBusinessEntity),new String[]{id});
        BusinessEntity item=null;
        if(cursor.moveToFirst()==true)
        {
            item=getBusinessEntity(cursor);
        }
        return item;
    }

    @Override
    public Category findCategoryById(String id)
    {
        SQLiteDatabase database=DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor=database.rawQuery(ZobonApp.getContext().getResources().getString(R.string.sql_findCategory),new String[]{id});
        Category item=null;
        if(cursor.moveToFirst()==true)
        {
            item=getCategory(cursor);
        }
        return item;
    }

    @Override
    public void updateBusinessItem(BusinessEntity entity)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();
        database.beginTransaction();
        database.execSQL("update businessentity set favorite=? where id=?",new Object[]{entity.isFavorite(),entity.getId()});

        database.setTransactionSuccessful();
        database.endTransaction();
        EventBus.getDefault().post(new ItemChangeEvent(entity));
    }


    @Override
    public void populateCategories(List<HashMap<String,?>> objects)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();
        database.beginTransaction();
        for(HashMap<String,?> object:objects)
        {
            ContentValues values=new ContentValues();
            values.put(CategoryTable.Cols.ID,(String)object.get(CategoryTable.Cols.ID));
            values.put(CategoryTable.Cols.AR_NAME,(String)object.get(CategoryTable.Cols.AR_NAME));
            values.put(CategoryTable.Cols.EN_NAME,(String)object.get(CategoryTable.Cols.EN_NAME));
            values.put(CategoryTable.Cols.TYPE,((Double) object.get(CategoryTable.Cols.TYPE)).intValue());

            long i=database.insertWithOnConflict(CategoryTable.NAME,null,values,SQLiteDatabase.CONFLICT_REPLACE);
            Log.d("DataManager","inserted records:"+i);
        }
        database.setTransactionSuccessful();
        database.endTransaction();

    }

    @Override
    public void populateOffers(List<HashMap<String, ?>> objects)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();
        database.beginTransaction();
        for(HashMap<String,?> object:objects)
        {
            ContentValues values=new ContentValues();
            String itemId=(String)object.get(OfferTable.Cols.ID);
            values.put(OfferTable.Cols.ID,itemId);
            values.put(OfferTable.Cols.AR_NAME,(String)object.get(OfferTable.Cols.AR_NAME));
            values.put(OfferTable.Cols.EN_NAME,(String)object.get(OfferTable.Cols.EN_NAME));


            long i=database.insertWithOnConflict(OfferTable.NAME,null,values,SQLiteDatabase.CONFLICT_IGNORE);
            if(i<=0)
                database.update(OfferTable.NAME,values,"id=?",new String[]{itemId});


            List<String> categories=(List<String>) object.get("categories");

            for (String categoryId:categories)
            {
                ContentValues catValues=new ContentValues();
                catValues.put(ItemCategoryTable.Cols.ITEM_ID,itemId);
                catValues.put(ItemCategoryTable.Cols.CATEGORY_ID,categoryId);
                i=database.insert(ItemCategoryTable.NAME,null,catValues);
                Log.d("DataManager","inserted records:"+i);
            }

        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    @Override
    public void populateMenus(List<HashMap<String, ?>> objects)
    {

    }

    @Override
    public void populateBusinessEntities(List<HashMap<String, ?>> objects)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();
        database.beginTransaction();
        for(HashMap<String,?> object:objects)
        {
            ContentValues values=new ContentValues();
            String itemId=(String)object.get(CategoryTable.Cols.ID);
            values.put(BusinessEntityTable.Cols.ID,itemId);
            values.put(BusinessEntityTable.Cols.AR_NAME,(String)object.get(CategoryTable.Cols.AR_NAME));
            values.put(BusinessEntityTable.Cols.EN_NAME,(String)object.get(CategoryTable.Cols.EN_NAME));
            values.put(BusinessEntityTable.Cols.DEFAULT_CONTACT,(String)object.get(BusinessEntityTable.Cols.DEFAULT_CONTACT));

            long i=database.insertWithOnConflict(BusinessEntityTable.NAME,null,values,SQLiteDatabase.CONFLICT_IGNORE);
            if(i<=0)
                database.update(BusinessEntityTable.NAME,values,"id=?",new String[]{itemId});


            database.delete(ContactTable.NAME,"itemId=?",new String[]{itemId});
            List<String> categories=(List<String>) object.get("categories");

                for (String categoryId:categories)
                {
                    ContentValues catValues=new ContentValues();
                    catValues.put(ItemCategoryTable.Cols.ITEM_ID,itemId);
                    catValues.put(ItemCategoryTable.Cols.CATEGORY_ID,categoryId);
                    i=database.insert(ItemCategoryTable.NAME,null,catValues);
                    Log.d("DataManager","inserted records:"+i);
                }

            Log.d("DataManager","inserted records:"+i);
        }
        database.setTransactionSuccessful();
        database.endTransaction();


    }
    @Override
    public void populateContacts(List<HashMap<String, ?>> objects)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();
        database.beginTransaction();
        for(HashMap<String,?> object:objects)
        {
            ContentValues values=new ContentValues();
            values.put(ContactTable.Cols.ID,(String)object.get(ContactTable.Cols.ID));
            values.put(ContactTable.Cols.URI,(String)object.get(ContactTable.Cols.URI));
            values.put(ContactTable.Cols.ITEM_ID,(String)object.get(ContactTable.Cols.ITEM_ID));
            values.put(ContactTable.Cols.AR_NAME,(String)object.get(ContactTable.Cols.AR_NAME));
            values.put(ContactTable.Cols.EN_NAME,(String)object.get(ContactTable.Cols.EN_NAME));

            long i=database.insertWithOnConflict(ContactTable.NAME,null,values,SQLiteDatabase.CONFLICT_REPLACE);
            Log.d("DataManager","inserted records:"+i);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }



    private Offer getOffer(Cursor cursor)
    {
        Offer offer=new Offer();
        offer.setId(UUID.fromString(cursor.getString(cursor.getColumnIndex(OfferTable.Cols.ID))));
        offer.setName(cursor.getString(cursor.getColumnIndex(L10NCols.NAME)));
        offer.setPages(cursor.getInt(cursor.getColumnIndex(OfferTable.Cols.PAGES)));
        return offer;
    }

    private BusinessEntity getBusinessEntity(Cursor cursor)
    {
        BusinessEntity entity=new BusinessEntity();
        entity.setId(UUID.fromString(cursor.getString(cursor.getColumnIndex(BusinessEntityTable.Cols.ID))));
        entity.setName(cursor.getString(cursor.getColumnIndex(L10NCols.NAME)));
        entity.setDesc(cursor.getString(cursor.getColumnIndex(L10NCols.DESC)));
        int fav=cursor.getInt(cursor.getColumnIndex(BusinessEntityTable.Cols.FAVORITE));
        entity.setFavorite(fav!=0);
        entity.setContact(Uri.parse(cursor.getString(cursor.getColumnIndex(BusinessEntityTable.Cols.URI))));
        return entity;
    }
    private Category getCategory(Cursor cursor)
    {
        Category category=new Category();
        category.setId(UUID.fromString(cursor.getString(cursor.getColumnIndex(CategoryTable.Cols.ID))));
        category.setName(cursor.getString(cursor.getColumnIndex(L10NCols.NAME)));
        category.setEntities(cursor.getInt(cursor.getColumnIndex(CategoryTable.Cols.ENTITIES)));
        category.setOffers(cursor.getInt(cursor.getColumnIndex(CategoryTable.Cols.OFFERS)));
        return category;
    }

    private Contact getContact(Cursor cursor)
    {
        Contact contact=new Contact();
        contact.setId(UUID.fromString(cursor.getString(cursor.getColumnIndex(ContactTable.Cols.ID))));
        contact.setUri(cursor.getString(cursor.getColumnIndex(ContactTable.Cols.URI)));
        contact.setName(cursor.getString(cursor.getColumnIndex(L10NCols.NAME)));
        return contact;
    }

}
