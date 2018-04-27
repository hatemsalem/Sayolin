package com.zobonapp.manager.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.zobonapp.R;
import com.zobonapp.db.DatabaseHelper;
import com.zobonapp.db.DbSchema;
import com.zobonapp.db.DbSchema.OfferTable;
import com.zobonapp.db.PageQuerySelector;
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
import java.util.Vector;

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
        if(categoryId==null)
            return entityMapper.findItems(offset,limit, PageQuerySelector.TYPE.BY_FAVORITE,"0",searchQuery);
        return entityMapper.findItems(offset,limit, PageQuerySelector.TYPE.BY_CATEGORY,categoryId,searchQuery);


    }

    @Override
    public List<BusinessEntity> findFavoriteEntitiesForPage(int offset, int limit, String searchQuery, String categoryId)
    {


        return entityMapper.findItems(offset,limit, PageQuerySelector.TYPE.BY_FAVORITE,"1",searchQuery);

    }
    @Override
    public List<Category> findCategoriesForPage(int type,int offset, int limit,String searchQuery)
    {
        if(searchQuery==null)
            searchQuery="";
        searchQuery= "%"+searchQuery+"%";
        return categoryMapper.findItems(offset,limit,null,searchQuery,String.valueOf(type));
    }
    @Override
    public List<Offer> findOffersForPage(int offset, int limit, String searchQuery, String categoryId)
    {
        if(categoryId==null)
            return offerMapper.findItems(offset,limit,PageQuerySelector.TYPE.BY_FAVORITE,categoryId,searchQuery);
        return offerMapper.findItems(offset,limit,PageQuerySelector.TYPE.BY_CATEGORY,categoryId,searchQuery);


    }

    @Override
    public List<Menu> findMenusForPage(int offset, int limit, String searchQuery, String categoryId)
    {
        return menuMapper.findItems(offset,limit,null,searchQuery);
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
    public List<Contact> findContactsForItem(String itemId,String searchQuery)
    {
        if(searchQuery==null)
            searchQuery="";
        searchQuery= "%"+searchQuery+"%";
        return contactMapper.findItems(0,0,null,itemId,searchQuery);
    }

    @Override
    public void deleteItems(List<String> items)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();
        database.beginTransaction();
        for (String item:items)
        {
            database.delete(ContactTable.NAME,"entityId=?",new String[]{item});
            database.delete(BusinessEntityTable.NAME,"id=?",new String[]{item});
            database.delete(DbSchema.ItemCategoryTable.NAME,"itemId=?",new String[]{item});
            database.delete(OfferTable.NAME,"entityId=?",new String[]{item});
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
            database.delete(DbSchema.ItemCategoryTable.NAME,"categoryId=?",new String[]{category});
            database.delete(CategoryTable.NAME,"id=?",new String[]{category});

        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    @Override
    public void deleteOffers(List<String> deletedOffers)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();
        database.beginTransaction();
        for (String offer:deletedOffers)
        {
            //TODO: decrease no. of offers in BusinessEntity
            database.execSQL("update businessentity set offers=offers-1 where id=(select entityId from offer where id=?)",new Object[]{offer});
            database.delete(OfferTable.NAME,"id=?",new String[]{offer});
            database.delete(DbSchema.ItemCategoryTable.NAME,"itemId=?",new String[]{offer});

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
        return entityMapper.findItemById(id);
    }

    @Override
    public Category findCategoryById(String id)
    {
        return categoryMapper.findItemById(id);
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

    @Override
    public void resetData()
    {
        //TODO: to be implemented
    }
}
