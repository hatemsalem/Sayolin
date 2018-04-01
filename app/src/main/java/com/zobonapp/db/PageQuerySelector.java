package com.zobonapp.db;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Admin on 3/25/2018.
 */

public interface PageQuerySelector <T>
{
    enum TYPE
    {
        BY_FAVORITE,BY_CATEGORY
    }
    List<T> findItems(int offset, int limit, TYPE queryType,String...searchQuery);
    T findItemById(String id);
}
