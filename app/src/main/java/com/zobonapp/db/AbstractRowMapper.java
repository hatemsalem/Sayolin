package com.zobonapp.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<T> findItemsForPage(int offset, int limit, Map<String, ?> args)
    {
        return null;
    }
}
