package com.glovoapp.backender.orders.domain;

import java.util.Objects;


public class Location
{
    private final Double lat;
    private final Double lon;

    public Location(final Double lat, final Double lon)
    {
        this.lat = lat;
        this.lon = lon;
    }

    public Double getLat()
    {
        return lat;
    }

    public Double getLon()
    {
        return lon;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        final Location location = (Location) o;
        return Objects.equals(lat, location.lat) &&
            Objects.equals(lon, location.lon);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(lat, lon);
    }

    @Override
    public String toString()
    {
        return "Location{" +
            "lat=" + lat +
            ", lon=" + lon +
            '}';
    }
}
