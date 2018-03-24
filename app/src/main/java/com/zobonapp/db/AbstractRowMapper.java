package com.zobonapp.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 3/24/2018.
 */

public abstract class AbstractRowMapper<T> implements RowMapper<T>
{
    @Override
    public ContentValues buildCV(HashMap<String, ?> object)
    {
        return new ContentValues();
    }
}
