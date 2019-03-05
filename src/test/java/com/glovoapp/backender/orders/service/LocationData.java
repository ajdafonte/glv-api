package com.glovoapp.backender.orders.service;

import com.glovoapp.backender.orders.domain.Location;


/**
 *
 */
public enum LocationData
{
    AV_TIBIDABO(1, new Location(41.411094, 2.136922)),
    SARRIA(2, new Location(41.399056, 2.119070)),
    LES_TRES_TORRES(3, new Location(41.397889, 2.131156)),
    BARCELONETA(4, new Location(41.382548, 2.185791)),
    BADALONA(5, new Location(41.448295, 2.249292)),
    FRANCESC_MACIA(6, new Location(41.392690, 2.143239));

    private final int id;
    private final Location location;

    LocationData(final int id, final Location location)
    {
        this.id = id;
        this.location = location;
    }

    public Location getLocation()
    {
        return location;
    }}
