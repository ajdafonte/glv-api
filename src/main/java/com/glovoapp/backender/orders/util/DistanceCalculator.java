package com.glovoapp.backender.orders.util;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import com.glovoapp.backender.orders.domain.Location;


/**
 * Shamelessly copied from https://github.com/jasonwinn/haversine
 */
public class DistanceCalculator
{

    private static final int EARTH_RADIUS = 6371;

    /**
     * Returns distance between two locations in kilometers
     */
    public static double calculateDistance(final Location start, final Location end)
    {
        final double deltaLat = toRadians((end.getLat() - start.getLat()));
        final double deltaLong = toRadians((end.getLon() - start.getLon()));

        final double startLat = toRadians(start.getLat());
        final double endLat = toRadians(end.getLat());

        final double a = haversin(deltaLat) + cos(startLat) * cos(endLat) * haversin(deltaLong);
        final double c = 2 * atan2(sqrt(a), sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private static double haversin(final double val)
    {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
