package com.zobonapp.domain;

/**
 * Created by hasalem on 12/13/2017.
 */

public class Contact
{
    private int pk;
    private String id;
    private String arName;
    private String enName;

    public int getPk()
    {
        return pk;
    }

    public void setPk(int pk)
    {
        this.pk = pk;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getArName()
    {
        return arName;
    }

    public void setArName(String arName)
    {
        this.arName = arName;
    }

    public String getEnName()
    {
        return enName;
    }

    public void setEnName(String enName)
    {
        this.enName = enName;
    }

}
