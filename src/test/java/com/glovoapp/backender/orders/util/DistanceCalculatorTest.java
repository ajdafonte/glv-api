package com.glovoapp.backender.orders.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.glovoapp.backender.orders.domain.Location;


class DistanceCalculatorTest
{
    @Test
    public void smokeTest()
    {
        final Location francescMacia = new Location(41.3925603, 2.1418532);
        final Location placaCatalunya = new Location(41.3870194, 2.1678584);

        // More or less 2km from Francesc Macia to Placa Catalunya
        assertEquals(2.0, DistanceCalculator.calculateDistance(francescMacia, placaCatalunya), 0.5);
    }

}