package com.zobonapp.manager;

import com.zobonapp.domain.BusinessEntity;

import java.util.List;

/**
 * Created by hasalem on 11/26/2017.
 */

public class BusinessEntitiesLoadedEvent
{
    private List<BusinessEntity> entities;

    public BusinessEntitiesLoadedEvent(List<BusinessEntity> entities)
    {
        this.entities = entities;
    }

    public List<BusinessEntity> getEntities()
    {
        return entities;
    }
}
