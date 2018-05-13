package com.zobonapp.manager;

import com.zobonapp.domain.AbstractItem;
import com.zobonapp.domain.BusinessEntity;

/**
 * Created by hasalem on 12/11/2017.
 */

public class ItemChangeEvent<T extends AbstractItem>
{
    private T item;

    public ItemChangeEvent(T item)
    {
        this.item = item;
    }

    public T getItem()
    {
        return item;
    }
}
