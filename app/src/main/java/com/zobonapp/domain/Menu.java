package com.zobonapp.domain;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hasalem on 12/13/2017.
 */

public class Menu extends AbstractItem
{
    private UUID entityId;
    private int pages;



    public UUID getEntityId()
    {
        return entityId;
    }

    public void setEntityId(UUID entityId)
    {
        this.entityId = entityId;
    }


    public int getPages()
    {
        return pages;
    }

    public void setPages(int pages)
    {
        this.pages = pages;
    }

}
