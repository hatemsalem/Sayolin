package com.zobonapp.manager.mapper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zobonapp.R;
import com.zobonapp.db.AbstractRowMapper;
import com.zobonapp.db.DatabaseHelper;
import com.zobonapp.db.DbSchema;
import com.zobonapp.db.PageQuerySelector;
import com.zobonapp.db.RowMapper;
import com.zobonapp.domain.Offer;
import com.zobonapp.utils.ZobonApp;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by Admin on 3/24/2018.
 */

public class OfferMapper extends AbstractRowMapper<Offer>
{
    @Override
    public Offer mapRow(Cursor cursor)
    {
        Offer offer=new Offer();
        offer.setId(UUID.fromString(cursor.getString(cursor.getColumnIndex(DbSchema.OfferTable.Cols.ID))));
        offer.setName(cursor.getString(cursor.getColumnIndex(DbSchema.L10NCols.NAME)));
        offer.setPages(cursor.getInt(cursor.getColumnIndex(DbSchema.OfferTable.Cols.PAGES)));
        return offer;
    }

    @Override
    public ContentValues buildCV(HashMap<String, ?> object)
    {
        ContentValues cv= super.buildCV(object);

        cv.put(DbSchema.OfferTable.Cols.ID,(String)object.get(DbSchema.OfferTable.Cols.ID));
        cv.put(DbSchema.OfferTable.Cols.AR_NAME,(String)object.get(DbSchema.OfferTable.Cols.AR_NAME));
        cv.put(DbSchema.OfferTable.Cols.EN_NAME,(String)object.get(DbSchema.OfferTable.Cols.EN_NAME));

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
            String itemId=(String)object.get(DbSchema.OfferTable.Cols.ID);
            database.insertWithOnConflict(DbSchema.OfferTable.NAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);
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
    public List<Offer> findItems(int offset, int limit, TYPE queryType, String... searchQuery)
    {
        String qry=searchQuery[0];
        List<String> queryArgs=new Vector<>();
        if(qry==null)
            qry="";
        qry= "%"+qry+"%";
        queryArgs.add(qry);
        queryArgs.add(qry);
        queryArgs.add(String.valueOf(offset));
        queryArgs.add(String.valueOf(limit));
        String query= ZobonApp.getContext().getResources().getString(R.string.sql_findOfferss);


        return queryAll(query,queryArgs.toArray(new String[]{}));
    }
}
