package com.zobonapp.manager.mapper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.zobonapp.R;
import com.zobonapp.db.AbstractRowMapper;
import com.zobonapp.db.DatabaseHelper;
import com.zobonapp.db.DbSchema;
import com.zobonapp.domain.BusinessEntity;
import com.zobonapp.utils.ZobonApp;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by Admin on 3/24/2018.
 */

public class BusinessEntityMapper extends AbstractRowMapper<BusinessEntity>
{

    @Override
    public BusinessEntity mapRow(Cursor cursor)
    {
        BusinessEntity entity=new BusinessEntity();
        entity.setId(UUID.fromString(cursor.getString(cursor.getColumnIndex(DbSchema.BusinessEntityTable.Cols.ID))));
        entity.setName(cursor.getString(cursor.getColumnIndex(DbSchema.L10NCols.NAME)));
        entity.setDesc(cursor.getString(cursor.getColumnIndex(DbSchema.L10NCols.DESC)));
        int fav=cursor.getInt(cursor.getColumnIndex(DbSchema.BusinessEntityTable.Cols.FAVORITE));
        entity.setFavorite(fav!=0);
        entity.setContact(Uri.parse(cursor.getString(cursor.getColumnIndex(DbSchema.BusinessEntityTable.Cols.URI))));
        entity.setOffers(cursor.getInt(cursor.getColumnIndex(DbSchema.BusinessEntityTable.Cols.OFFERS)));
        entity.setWebSite(cursor.getString(cursor.getColumnIndex(DbSchema.BusinessEntityTable.Cols.WEB_SITE)));
        entity.setFbPage(cursor.getString(cursor.getColumnIndex(DbSchema.BusinessEntityTable.Cols.FB_PAGE)));
        return entity;
    }

    @Override
    public ContentValues buildCV(HashMap<String, ?> object)
    {
        ContentValues cv= super.buildCV(object);
        cv.put(DbSchema.BusinessEntityTable.Cols.ID,(String)object.get(DbSchema.CategoryTable.Cols.ID));
        cv.put(DbSchema.BusinessEntityTable.Cols.AR_NAME,(String)object.get(DbSchema.CategoryTable.Cols.AR_NAME));
        cv.put(DbSchema.BusinessEntityTable.Cols.EN_NAME,(String)object.get(DbSchema.CategoryTable.Cols.EN_NAME));
        cv.put(DbSchema.BusinessEntityTable.Cols.DEFAULT_CONTACT,(String)object.get(DbSchema.BusinessEntityTable.Cols.DEFAULT_CONTACT));
        cv.put(DbSchema.ItemCols.RANK,(String)object.get(DbSchema.BusinessEntityTable.Cols.RANK));
        cv.put(DbSchema.BusinessEntityTable.Cols.WEB_SITE,(String)object.get(DbSchema.BusinessEntityTable.Cols.WEB_SITE));
        cv.put(DbSchema.BusinessEntityTable.Cols.FB_PAGE,(String)object.get(DbSchema.BusinessEntityTable.Cols.FB_PAGE));
        return cv;
    }

    @Override
    public void populate(List<HashMap<String, ?>> objects)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();
        database.beginTransaction();
        for(HashMap<String,?> object:objects)
        {
            ContentValues cv=buildCV(object);
            String itemId=(String)object.get(DbSchema.BusinessEntityTable.Cols.ID);

            long result= database.insertWithOnConflict(DbSchema.BusinessEntityTable.NAME,null,cv,SQLiteDatabase.CONFLICT_IGNORE);
            if(result<0)
            {
                BusinessEntity entity=findItemById(itemId);
                if(entity!=null)
                    cv.put(DbSchema.BusinessEntityTable.Cols.FAVORITE,entity.isFavorite()?"1":"0");
                result =database.insertWithOnConflict(DbSchema.BusinessEntityTable.NAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
            }

            database.delete(DbSchema.ContactTable.NAME,"entityId=?",new String[]{itemId});
            List<String> categories=(List<String>) object.get("categories");
            database.delete(DbSchema.ItemCategoryTable.NAME,"itemId=?",new String[]{itemId});
            for (String categoryId:categories)
            {
                ContentValues catValues=new ContentValues();
                catValues.put(DbSchema.ItemCategoryTable.Cols.ITEM_ID,itemId);
                catValues.put(DbSchema.ItemCategoryTable.Cols.CATEGORY_ID,categoryId);
                database.insert(DbSchema.ItemCategoryTable.NAME,null,catValues);
            }

        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    @Override
    public List<BusinessEntity> findItems(int offset, int limit,TYPE queryType, String... searchQuery)
    {

        List<String> varArgs=new Vector<>();

        String qry=searchQuery[1];
        if(qry==null)
            qry="";
        qry="%"+qry+"%";
        varArgs.add(qry); //enName
        varArgs.add(qry); //arName
        varArgs.add(qry);//tel

        String query="";
        switch (queryType)
        {
            case BY_FAVORITE:

                varArgs.add(searchQuery[0]); //favorite
                query= ZobonApp.getContext().getResources().getString(R.string.sql_findBusinessEntities);
                break;
            case BY_CATEGORY:
                varArgs.add(searchQuery[0]);//categoryid
                query=ZobonApp.getContext().getResources().getString(R.string.sql_findBusinessEntitiesInCategory);
                break;
        }
        varArgs.add(String.valueOf(offset));
        varArgs.add(String.valueOf(limit));
        return queryAll(query,varArgs.toArray(new String[0]));
    }

    @Override
    public BusinessEntity findItemById(String id)
    {
        String query= ZobonApp.getContext().getResources().getString(R.string.sql_findBusinessEntity);
        return queryOne(query,id);
    }
}
