package com.glovoapp.backender.orders.service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glovoapp.backender.common.error.BackenderApiError;
import com.glovoapp.backender.common.error.BackenderApiException;
import com.glovoapp.backender.orders.domain.Courier;
import com.glovoapp.backender.orders.domain.Order;
import com.glovoapp.backender.orders.domain.Vehicle;
import com.glovoapp.backender.orders.repo.CourierRepository;
import com.glovoapp.backender.orders.repo.OrderRepository;
import com.glovoapp.backender.orders.util.DistanceCalculator;


/**
 *
 */
@Service
public class OrderServiceImpl implements OrderService
{
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final String[] reservedWords = {"pizza", "cake", "flamingo"};

    @Autowired
    public OrderServiceImpl(final OrderRepository orderRepository, final CourierRepository courierRepository)
    {
        this.orderRepository = orderRepository;
        this.courierRepository = courierRepository;
    }

    @Override
    public List<Order> findAll()
    {
        return orderRepository.findAll();
    }

    private boolean stringContainsItemFromList(final String inputStr, final String[] items)
    {
        return Arrays.stream(items).anyMatch(inputStr::contains);
    }

    @Override
    public List<Order> findAllBy(final String courierId)
    {
        final Courier courier = courierRepository.findById(courierId);

        if (courier == null)
        {
            throw new BackenderApiException(BackenderApiError.UNKNOWN_RESOURCE, "The courier id does not exist.");
        }

        final List<Order> allOrders = orderRepository.findAll();

        // first version of predicate 1
//        final Predicate<Order> predicate = o -> {
//            if (!courier.getBox())
//            {
//                return !stringContainsItemFromList(o.getDescription(), reservedWords);
//            }
//            return true;
//        };

        // filtering
        final Predicate<Order> predicate1 =
            o -> courier.getBox() || !stringContainsItemFromList(o.getDescription(), reservedWords);
        final Predicate<Order> predicate2 =
            o -> {
                final double loc1 = DistanceCalculator.calculateDistance(courier.getLocation(), o.getPickup());
                final double loc2 = DistanceCalculator.calculateDistance(o.getPickup(), o.getDelivery());
                final double finalDistance = loc1 + loc2;
                return (courier.getVehicle().equals(Vehicle.MOTORCYCLE) ||
                    courier.getVehicle().equals(Vehicle.ELECTRIC_SCOOTER)) || finalDistance <= 5;
            };

        return allOrders.stream()
            .filter(predicate1)
            .filter(predicate2)
            .collect(Collectors.toList());

        // TODO - Add sorting

    }
}
