package com.glovoapp.backender.orders.service;

import java.util.List;

import com.glovoapp.backender.orders.domain.Order;


/**
 *
 */
public interface OrderService
{
    /**
     * @return
     */
    List<Order> findAll();

    /**
     * @param courierId
     * @return
     */
    List<Order> findAllBy(String courierId);
}
