package com.glovoapp.backender.orders.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.glovoapp.backender.common.BackenderApiConstants;
import com.glovoapp.backender.orders.service.OrderService;


/**
 *
 */
@RestController
@RequestMapping(value = BackenderApiConstants.ORDERS_RESOURCE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController
{
    private final OrderService orderService;

    @Autowired
    public OrderController(final OrderService orderService)
    {
        this.orderService = orderService;
    }

    @GetMapping
    List<OrderVM> orders()
    {
        return orderService.findAll()
            .stream()
            .map(order -> new OrderVM(order.getId(), order.getDescription()))
            .collect(Collectors.toList());
    }

    @GetMapping(value = BackenderApiConstants.ORDERS_COURIER_ID_PATH_PARAM)
    List<OrderVM> ordersAvailableForCourier(@PathVariable final String courierId)
    {
        return orderService.findAllBy(courierId)
            .stream()
            .map(order -> new OrderVM(order.getId(), order.getDescription()))
            .collect(Collectors.toList());
    }
}
