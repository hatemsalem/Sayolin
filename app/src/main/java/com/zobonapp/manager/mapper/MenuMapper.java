package com.zobonapp.manager.mapper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zobonapp.db.AbstractRowMapper;
import com.zobonapp.db.DatabaseHelper;
import com.zobonapp.db.DbSchema;
import com.zobonapp.db.RowMapper;
import com.zobonapp.domain.Menu;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.zobonapp.db.DbSchema.*;

/**
 * Created by Admin on 3/24/2018.
 */

public class MenuMapper extends AbstractRowMapper<Menu>

{
    @Override
    public Menu mapRow(Cursor cursor)
    {
        Menu menu=new Menu();
        menu.setId(UUID.fromString(cursor.getString(cursor.getColumnIndex(OfferTable.Cols.ID))));
        menu.setName(cursor.getString(cursor.getColumnIndex(L10NCols.NAME)));
        menu.setPages(cursor.getInt(cursor.getColumnIndex(OfferTable.Cols.PAGES)));
        return menu;
    }

    @Override
    public ContentValues buildCV(HashMap<String, ?> object)
    {
        ContentValues cv= super.buildCV(object);
        cv.put(MenutTable.Cols.ID,(String)object.get(MenutTable.Cols.ID));
        cv.put(MenutTable.Cols.AR_NAME,(String)object.get(MenutTable.Cols.AR_NAME));
        cv.put(MenutTable.Cols.EN_NAME,(String)object.get(MenutTable.Cols.EN_NAME));
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
            String itemId=(String)object.get(MenutTable.Cols.ID);


            database.insertWithOnConflict(MenutTable.NAME,null,cv,SQLiteDatabase.CONFLICT_REPLACE);


            List<String> categories=(List<String>) object.get("categories");
            database.delete(DbSchema.ItemCategoryTable.NAME,"itemId=?",new String[]{itemId});

            for (String categoryId:categories)
            {
                ContentValues catValues=new ContentValues();
                catValues.put(ItemCategoryTable.Cols.ITEM_ID,itemId);
                catValues.put(ItemCategoryTable.Cols.CATEGORY_ID,categoryId);
                database.insert(ItemCategoryTable.NAME,null,catValues);
            }

        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }
}
