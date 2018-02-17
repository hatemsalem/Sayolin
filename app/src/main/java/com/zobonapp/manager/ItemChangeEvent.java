package com.zobonapp.manager;

import com.zobonapp.domain.BusinessEntity;

/**
 * Created by hasalem on 12/11/2017.
 */

public class ItemChangeEvent
{
    private BusinessEntity item;

    public ItemChangeEvent(BusinessEntity item)
    {
        this.item = item;
    }

    public BusinessEntity getItem()
    {
        return item;
    }
}
