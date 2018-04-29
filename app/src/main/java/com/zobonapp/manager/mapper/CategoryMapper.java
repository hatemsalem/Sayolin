package com.zobonapp.manager.mapper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zobonapp.R;
import com.zobonapp.db.AbstractRowMapper;
import com.zobonapp.db.DatabaseHelper;
import com.zobonapp.db.DbSchema;
import com.zobonapp.domain.Category;
import com.zobonapp.utils.ZobonApp;

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
        category.setType(cursor.getInt(cursor.getColumnIndex(DbSchema.CategoryTable.Cols.TYPE)));
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
        cv.put(DbSchema.ItemCols.RANK,Integer.parseInt(object.get(DbSchema.BusinessEntityTable.Cols.RANK).toString()));
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


            long i=database.insertWithOnConflict(DbSchema.CategoryTable.NAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
            i++;

        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }
    @Override
    public Category findItemById(String id)
    {
        String query= ZobonApp.getContext().getResources().getString(R.string.sql_findCategory);
        return queryOne(query,id);
    }

    @Override
    public List<Category> findItems(int offset, int limit,TYPE queryType, String... searchQuery)
    {
        String query= ZobonApp.getContext().getResources().getString(R.string.sql_findCategories);
        String arName=searchQuery[0];
        String enName=searchQuery[0];
        String keywords=searchQuery[0];
        String type=searchQuery[1];
        return queryAll(query,enName,arName,keywords,type,String.valueOf(offset),String.valueOf(limit));
    }
}
