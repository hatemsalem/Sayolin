package com.zobonapp.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Admin on 3/24/2018.
 */

public abstract class AbstractRowMapper<T> implements RowMapper<T>,PageQuerySelector<T>
{
    @Override
    public ContentValues buildCV(HashMap<String, ?> object)
    {
        return new ContentValues();
    }

    @Override
    public List<T> findItems(int offset, int limit, TYPE queryType,String ...searchQuery)
    {
        return null;
    }

    @Override
    public T findItemById(String id)
    {
        return null;
    }

    protected List<T> queryAll(String query, String ...queryArgs)
    {
        Cursor cursor=DatabaseHelper.getInstance().getReadableDatabase().rawQuery(query, queryArgs);
        List<T> items=new ArrayList<>();
        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                items.add(mapRow(cursor));
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
        return items;

    }
    protected  T queryOne(String query,String...queryArgs)
    {
        List<T> results=queryAll(query,queryArgs);
        T item=null;
        if(results.size()>0)
            item=results.get(0);
        return item;
    }
}
