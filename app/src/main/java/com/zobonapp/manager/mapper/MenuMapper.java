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
import com.zobonapp.domain.Menu;
import com.zobonapp.utils.ZobonApp;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

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
    @Override
    public List<Menu> findItems(int offset, int limit, TYPE queryType, String... searchQuery)
    {

        List<String> queryArgs=new Vector<>();
        String qry=searchQuery[0];
        if(qry==null)
            qry="";
        qry= "%"+qry+"%";
        queryArgs.add(qry);
        queryArgs.add(qry);
        queryArgs.add(String.valueOf(offset));
        queryArgs.add(String.valueOf(limit));
        //TODO: order by should be handled for consistent results.

        String query= ZobonApp.getContext().getResources().getString(R.string.sql_findMenus,offset,limit);

        return queryAll(query,queryArgs.toArray(new String[]{}));
    }

}
