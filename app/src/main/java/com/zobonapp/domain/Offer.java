package com.zobonapp.domain;

import java.util.Date;
import java.util.UUID;

/**
 * Created by hasalem on 12/13/2017.
 */

public class Offer extends AbstractItem
{
    private UUID entityId;
    private Date startDate;
    private Date endDate;
    private int pages;



    public UUID getEntityId()
    {
        return entityId;
    }

    public void setEntityId(UUID entityId)
    {
        this.entityId = entityId;
    }


    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
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
