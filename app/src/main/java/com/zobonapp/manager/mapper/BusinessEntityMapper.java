package com.zobonapp.manager.mapper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.zobonapp.db.AbstractRowMapper;
import com.zobonapp.db.DatabaseHelper;
import com.zobonapp.db.DbSchema;
import com.zobonapp.db.RowMapper;
import com.zobonapp.domain.BusinessEntity;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
//        entity.setRank(cursor.getInt(cursor.getColumnIndex(DbSchema.BusinessEntityTable.Cols.RANK)));
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
        cv.put(DbSchema.BusinessEntityTable.Cols.RANK,(String)object.get(DbSchema.BusinessEntityTable.Cols.RANK));
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

            database.insertWithOnConflict(DbSchema.BusinessEntityTable.NAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
            database.delete(DbSchema.ContactTable.NAME,"itemId=?",new String[]{itemId});
            List<String> categories=(List<String>) object.get("categories");

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
}
