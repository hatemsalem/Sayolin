package com.zobonapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zobonapp.utils.ZobonApp;

import static com.zobonapp.db.DbSchema.*;
import static com.zobonapp.db.DbSchema.SCHEMA_NAME;
import static com.zobonapp.db.DbSchema.VERSION;

/**
 * Created by hasalem on 11/26/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{
    private static DatabaseHelper instance;
    public synchronized static DatabaseHelper getInstance()
    {
        if(instance==null)
        {
            instance=new DatabaseHelper(ZobonApp.getContext());
        }
        return instance;
    }
    private DatabaseHelper(Context context)
    {
        super(context, SCHEMA_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(BusinessEntityTable.getCreateStatement());
        db.execSQL(CategoryTable.getCreateStatement());
        db.execSQL(ContactTable.getCreateStatement());
        db.execSQL(OfferTable.getCreateStatement());
        db.execSQL(ItemCategoryTable.getCreateStatement());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("Drop table if exists "+ BusinessEntityTable.NAME);
        db.execSQL("Drop table if exists "+ CategoryTable.NAME);
        db.execSQL("Drop table if exists "+ ContactTable.NAME);
        db.execSQL("Drop table if exists "+ OfferTable.NAME);
        db.execSQL("Drop table if exists "+ ItemCategoryTable.NAME);

        onCreate(db);
    }
}
