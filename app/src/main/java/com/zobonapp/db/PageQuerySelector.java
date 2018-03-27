package com.zobonapp.db;

import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 3/25/2018.
 */

public interface PageQuerySelector <T>
{
    List<T> findItemsForPage(int offset, int limit, Map<String,?> args);
}
