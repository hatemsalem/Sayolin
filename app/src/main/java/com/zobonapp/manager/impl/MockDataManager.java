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
import com.zobonapp.db.RowMapper;
import com.zobonapp.domain.BusinessEntity;
import com.zobonapp.domain.Category;
import com.zobonapp.domain.Contact;
import com.zobonapp.domain.Menu;
import com.zobonapp.domain.Offer;
import com.zobonapp.manager.DataManager;
import com.zobonapp.manager.ItemChangeEvent;
import com.zobonapp.manager.mapper.BusinessEntityMapper;
import com.zobonapp.manager.mapper.CategoryMapper;
import com.zobonapp.manager.mapper.ContactMapper;
import com.zobonapp.manager.mapper.MenuMapper;
import com.zobonapp.manager.mapper.OfferMapper;
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
    private CategoryMapper categoryMapper=new CategoryMapper();
    private ContactMapper contactMapper=new ContactMapper();
    private BusinessEntityMapper entityMapper=new BusinessEntityMapper();
    private MenuMapper menuMapper=new MenuMapper();
    private OfferMapper offerMapper=new OfferMapper();
    private <T>List<T> queryExecutor(String query,String[] queryArgs, RowMapper<T> mapper)
    {
        Cursor cursor=DatabaseHelper.getInstance().getReadableDatabase().rawQuery(query, queryArgs);
        List<T> items=new ArrayList<>();
        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                items.add(mapper.mapRow(cursor));
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
        return items;

    }
    @Override
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
        return queryExecutor(query,queryArgs.toArray(new String[]{}),entityMapper);
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
        return queryExecutor(query,queryArgs.toArray(new String[]{}),entityMapper);

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

        return queryExecutor(query,queryArgs.toArray(new String[]{}),offerMapper);
    }

    @Override
    public List<Menu> findMenusForPage(int offset, int limit, String searchQuery, String categoryId)
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
            query= ZobonApp.getContext().getResources().getString(R.string.sql_findMenus,offset,limit);
        }
        else
        {
            query= ZobonApp.getContext().getResources().getString(R.string.sql_findBusinessEntitiesInCategory,offset,limit);
            queryArgs.add(categoryId);
        }

        return queryExecutor(query,queryArgs.toArray(new String[]{}),menuMapper);
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

        return queryExecutor(query,queryArgs.toArray(new String[]{}),categoryMapper);
    }

    @Override
    public List<Category> findOffersCategoriesForPage(int type, int offset, int limit, String searchQuery)
    {
        String query=null;
        Vector<String> queryArgs=new Vector<>();
        if(searchQuery==null)
            searchQuery="";
        searchQuery= "%"+searchQuery+"%";
        queryArgs.add(searchQuery);
        queryArgs.add(searchQuery);
        queryArgs.add(searchQuery);

        query= ZobonApp.getContext().getResources().getString(R.string.sql_findOffersCategories,offset,limit,type);

        return queryExecutor(query,queryArgs.toArray(new String[]{}),categoryMapper);
    }

    @Override
    public List<Category> findFavoriteCategoriesForPage(int type, int offset, int limit, String searchQuery)
    {
        String query=null;
        Vector<String> queryArgs=new Vector<>();
        if(searchQuery==null)
            searchQuery="";
        searchQuery= "%"+searchQuery+"%";
//        queryArgs.add(searchQuery);
//        queryArgs.add(searchQuery);
//        queryArgs.add(searchQuery);

        query= ZobonApp.getContext().getResources().getString(R.string.sql_findFavoriteCategories,offset,limit,type);

        return queryExecutor(query,queryArgs.toArray(new String[]{}),categoryMapper);
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
        return queryExecutor(query,queryArgs.toArray(new String[]{}),contactMapper);
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
                database.insert(ItemCategoryTable.NAME,null,values);

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
            item=entityMapper.mapRow(cursor);
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
            item=categoryMapper.mapRow(cursor);
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
        categoryMapper.populate(objects);
    }

    @Override
    public void populateOffers(List<HashMap<String, ?>> objects)
    {
        offerMapper.populate(objects);
    }

    @Override
    public void populateMenus(List<HashMap<String, ?>> objects)
    {
        menuMapper.populate(objects);
    }

    @Override
    public void populateBusinessEntities(List<HashMap<String, ?>> objects)
    {
        entityMapper.populate(objects);
    }
    @Override
    public void populateContacts(List<HashMap<String, ?>> objects)
    {
        contactMapper.populate(objects);
    }
}
