package com.zobonapp.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 3/24/2018.
 */

public interface RowMapper<T>
{
    T mapRow(Cursor cursor);
    ContentValues buildCV(HashMap<String,?> object);
    void populate(List<HashMap<String,?>> objects);
}
