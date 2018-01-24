package com.zobonapp.manager.impl;

import com.zobonapp.manager.DataManager;
import com.zobonapp.manager.ManagerRegistry;

/**
 * Created by hasalem on 11/26/2017.
 */

public class MockManagerRegistry implements ManagerRegistry
{
    private DataManager dataManager;

    public MockManagerRegistry()
    {
        this.dataManager = new MockDataManager();
    }

    @Override
    public DataManager getDataManager()
    {
        return dataManager;
    }
}
