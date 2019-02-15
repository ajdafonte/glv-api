package com.glovoapp.backender.orders.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.glovoapp.backender.orders.domain.Courier;
import com.glovoapp.backender.orders.domain.Location;
import com.glovoapp.backender.orders.domain.Vehicle;


class CourierRepositoryTest
{
    @Test
    void findOneExisting()
    {
        final Courier courier = new CourierRepository().findById("courier-1");
        final Courier expected = new Courier()
            .withId("courier-1")
            .withBox(true)
            .withName("Manolo Escobar")
            .withVehicle(Vehicle.MOTORCYCLE)
            .withLocation(new Location(41.3965463, 2.1963997));

        assertEquals(expected, courier);
    }

    @Test
    void findOneNotExisting()
    {
        final Courier courier = new CourierRepository().findById("bad-courier-id");
        assertNull(courier);
    }

    @Test
    void findAll()
    {
        final List<Courier> all = new CourierRepository().findAll();
        assertFalse(all.isEmpty());
    }
}