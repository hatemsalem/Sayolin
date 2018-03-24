package com.zobonapp.manager.mapper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zobonapp.db.AbstractRowMapper;
import com.zobonapp.db.DatabaseHelper;
import com.zobonapp.db.DbSchema;
import com.zobonapp.db.RowMapper;
import com.zobonapp.domain.Category;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Admin on 3/24/2018.
 */

public class CategoryMapper extends AbstractRowMapper<Category>
{

    @Override
    public Category mapRow(Cursor cursor)
    {
        Category category=new Category();
        category.setId(UUID.fromString(cursor.getString(cursor.getColumnIndex(DbSchema.CategoryTable.Cols.ID))));
        category.setName(cursor.getString(cursor.getColumnIndex(DbSchema.L10NCols.NAME)));
        category.setEntities(cursor.getInt(cursor.getColumnIndex(DbSchema.CategoryTable.Cols.ENTITIES)));
        category.setOffers(cursor.getInt(cursor.getColumnIndex(DbSchema.CategoryTable.Cols.OFFERS)));
        return category;
    }

    @Override
    public ContentValues buildCV(HashMap<String, ?> object)
    {
        ContentValues cv= super.buildCV(object);
        cv.put(DbSchema.CategoryTable.Cols.ID,(String)object.get(DbSchema.CategoryTable.Cols.ID));
        cv.put(DbSchema.CategoryTable.Cols.AR_NAME,(String)object.get(DbSchema.CategoryTable.Cols.AR_NAME));
        cv.put(DbSchema.CategoryTable.Cols.EN_NAME,(String)object.get(DbSchema.CategoryTable.Cols.EN_NAME));
        cv.put(DbSchema.CategoryTable.Cols.TYPE,((Double) object.get(DbSchema.CategoryTable.Cols.TYPE)).intValue());
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


            database.insertWithOnConflict(DbSchema.CategoryTable.NAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);

        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }
}
