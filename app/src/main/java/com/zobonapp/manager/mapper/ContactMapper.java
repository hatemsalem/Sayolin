package com.zobonapp.manager.mapper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zobonapp.db.AbstractRowMapper;
import com.zobonapp.db.DatabaseHelper;
import com.zobonapp.db.DbSchema;
import com.zobonapp.db.RowMapper;
import com.zobonapp.domain.Contact;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Admin on 3/24/2018.
 */

public class ContactMapper extends AbstractRowMapper<Contact>
{
    @Override
    public Contact mapRow(Cursor cursor)
    {
        Contact contact=new Contact();
        contact.setId(UUID.fromString(cursor.getString(cursor.getColumnIndex(DbSchema.ContactTable.Cols.ID))));
        contact.setUri(cursor.getString(cursor.getColumnIndex(DbSchema.ContactTable.Cols.URI)));
        contact.setName(cursor.getString(cursor.getColumnIndex(DbSchema.L10NCols.NAME)));
        return contact;
    }

    @Override
    public ContentValues buildCV(HashMap<String, ?> object)
    {
        ContentValues cv= super.buildCV(object);
        cv.put(DbSchema.ContactTable.Cols.ID,(String)object.get(DbSchema.ContactTable.Cols.ID));
        cv.put(DbSchema.ContactTable.Cols.URI,(String)object.get(DbSchema.ContactTable.Cols.URI));
        cv.put(DbSchema.ContactTable.Cols.ITEM_ID,(String)object.get(DbSchema.ContactTable.Cols.ITEM_ID));
        cv.put(DbSchema.ContactTable.Cols.AR_NAME,(String)object.get(DbSchema.ContactTable.Cols.AR_NAME));
        cv.put(DbSchema.ContactTable.Cols.EN_NAME,(String)object.get(DbSchema.ContactTable.Cols.EN_NAME));


        return cv;
    }

    @Override
    public void populate(List<HashMap<String, ?>> objects)
    {
        SQLiteDatabase database= DatabaseHelper.getInstance().getWritableDatabase();
        database.beginTransaction();
        for(HashMap<String,?> object:objects)
        {
            ContentValues values=buildCV(object);

            database.insertWithOnConflict(DbSchema.ContactTable.NAME,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }
}