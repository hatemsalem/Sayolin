package com.zobonapp.manager;

/**
 * Created by hasalem on 12/11/2017.
 */

public class InitializationEvent
{
    public enum Status{PARSING_COMPLETED,DB_10Percent,DB_25Percent,DB_50Percent,DB_75Percent,COMPLETED,FAILED};
    private Status status;

    public InitializationEvent(Status status)
    {
        this.status = status;
    }


    public Status getStatus()
    {
        return status;
    }
}
