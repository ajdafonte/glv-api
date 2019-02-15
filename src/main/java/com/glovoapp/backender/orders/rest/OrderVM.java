package com.glovoapp.backender.orders.rest;

/**
 * To be used for exposing order information through the API
 */
public class OrderVM
{
    private final String id;
    private final String description;

    public OrderVM(final String id, final String description)
    {
        this.id = id;
        this.description = description;
    }

    public String getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }
}
