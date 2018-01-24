package com.zobonapp.manager.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.zobonapp.R;
import com.zobonapp.utils.QueryPreferences;
import com.zobonapp.db.DbSchema;
import com.zobonapp.db.DatabaseHelper;
import com.zobonapp.domain.BusinessEntity;
import com.zobonapp.domain.Category;
import com.zobonapp.manager.BusinessEntitiesLoadedEvent;
import com.zobonapp.manager.DataManager;
import com.zobonapp.utils.ZobonApp;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import static com.zobonapp.db.DbSchema.*;

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
    public List<Category> findCategoriesForPage(int type,int offset, int limit)
    {
        String query=null;
        Vector<String> queryArgs=new Vector<>();
        String searchQuery=null;
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
    public void updateBusinessEntities(List<BusinessEntity> entities)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();

        database.beginTransaction();
        for(BusinessEntity entity:entities)
        {

            long i=database.insert(DbSchema.BusinessEntityTable.NAME,null,getContentValues(entity));
            Log.d("UpdateService","No. of inserted records="+i);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    @Override
    public void updateItemCategoryRelations(HashMap<UUID, Vector<UUID>> relation)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();

        database.beginTransaction();
        for(Map.Entry<UUID,Vector<UUID>> entry:relation.entrySet())
        {
            UUID item=entry.getKey();
            for(UUID category:entry.getValue())
            {
                long i=database.insert(DbSchema.ItemCategoryTable.NAME,null,getContentValues(item,category));
                Log.d("UpdateService","No. of inserted records="+i);
            }


        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    @Override
    public void updateBusinessItem(BusinessEntity entity)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();
        database.beginTransaction();
        database.execSQL("update businessentity set favorite=? where _id=?",new Object[]{entity.isFavorite(),entity.getPk()});

        database.setTransactionSuccessful();
        database.endTransaction();
    }

    @Override
    public void updateCategories(List<Category> categories)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();

        database.beginTransaction();
        for(Category category:categories)
        {

            long i=database.insert(DbSchema.CategoryTable.NAME,null,getContentValues(category));
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    @Override
    public void updateEntityTags(List<Category> tags)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();

        for(Category tag:tags)
        {
            long i=database.insert(CategoryTable.NAME,null,getContentValues(tag));
            Log.d("Database","No. of inserted records="+i);
        }
    }


    private ContentValues getContentValues(BusinessEntity entity)
    {
        ContentValues values=new ContentValues();
        values.put(BusinessEntityTable.Cols.ID,entity.getId().toString());
        values.put(BusinessEntityTable.Cols.EN_NAME,entity.getEnName());
        values.put(BusinessEntityTable.Cols.EN_DESC,entity.getEnDesc());
        values.put(BusinessEntityTable.Cols.AR_NAME,entity.getArName());
        values.put(BusinessEntityTable.Cols.AR_DESC,entity.getArDesc());
        values.put(BusinessEntityTable.Cols.HOTLINE,entity.getHotline());
        values.put(BusinessEntityTable.Cols.OFFERS,entity.getOffers());
        values.put(BusinessEntityTable.Cols.FAVORITE,entity.isFavorite());
        return values;
    }
    private ContentValues getContentValues(UUID item,UUID category)
    {
        ContentValues values=new ContentValues();
        values.put(ItemCategoryTable.Cols.ITEM_ID,item.toString());
        values.put(ItemCategoryTable.Cols.CATEGORY_ID,category.toString());
        return values;
    }
    private ContentValues getContentValues(Category tag)
    {
        ContentValues values=new ContentValues();
        values.put(CategoryTable.Cols.ID, tag.getId().toString());
        values.put(CategoryTable.Cols.EN_NAME,tag.getEnName());
        values.put(CategoryTable.Cols.AR_NAME,tag.getArName());
        values.put(CategoryTable.Cols.TYPE,tag.getType());
        values.put(CategoryTable.Cols.ENTITIES,tag.getEntities());
        values.put(CategoryTable.Cols.OFFERS,tag.getOffers());
        return values;
    }
    private BusinessEntity getBusinessEntity(Cursor cursor)
    {
        BusinessEntity entity=new BusinessEntity();
        entity.setPk(cursor.getInt(cursor.getColumnIndex("_id")));
        entity.setId(UUID.fromString(cursor.getString(cursor.getColumnIndex(BusinessEntityTable.Cols.ID))));
        entity.setName(cursor.getString(cursor.getColumnIndex(BusinessEntityTable.Cols.NAME)));
        entity.setDesc(cursor.getString(cursor.getColumnIndex(BusinessEntityTable.Cols.DESC)));
        entity.setHotline(cursor.getString(cursor.getColumnIndex(BusinessEntityTable.Cols.HOTLINE)));
        int fav=cursor.getInt(cursor.getColumnIndex(BusinessEntityTable.Cols.FAVORITE));
        entity.setFavorite(fav!=0);
        return entity;
    }
    private Category getCategory(Cursor cursor)
    {
        Category category=new Category();
        category.setPk(cursor.getInt(cursor.getColumnIndex("_id")));
        category.setId(UUID.fromString(cursor.getString(cursor.getColumnIndex(CategoryTable.Cols.ID))));
        category.setName(cursor.getString(cursor.getColumnIndex(CategoryTable.Cols.NAME)));
        category.setEntities(cursor.getInt(cursor.getColumnIndex(CategoryTable.Cols.ENTITIES)));
        return category;
    }


}
