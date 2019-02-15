package com.glovoapp.backender.orders.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.glovoapp.backender.orders.domain.Location;
import com.glovoapp.backender.orders.domain.Order;


class OrderRepositoryTest
{
    @Test
    void findAll()
    {
        final List<Order> orders = new OrderRepository().findAll();

        assertFalse(orders.isEmpty());

        final Order firstOrder = orders.get(0);

        final Order expected = new Order()
            .withId("order-1")
            .withDescription("I want a pizza cut into very small slices")
            .withFood(true)
            .withVip(false)
            .withPickup(new Location(41.3965463, 2.1963997))
            .withDelivery(new Location(41.407834, 2.1675979));

        assertEquals(expected, firstOrder);
    }
}