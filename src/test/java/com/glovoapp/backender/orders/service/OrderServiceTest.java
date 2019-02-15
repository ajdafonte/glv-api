package com.glovoapp.backender.orders.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.glovoapp.backender.common.error.BackenderApiException;
import com.glovoapp.backender.orders.domain.Courier;
import com.glovoapp.backender.orders.domain.Location;
import com.glovoapp.backender.orders.domain.Order;
import com.glovoapp.backender.orders.domain.Vehicle;
import com.glovoapp.backender.orders.repo.CourierRepository;
import com.glovoapp.backender.orders.repo.OrderRepository;


/**
 * TODO - Refactor tests
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest
{
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CourierRepository courierRepository;

    private OrderService orderService;

    @BeforeEach
    public void setUp()
    {
        this.orderService = new OrderServiceImpl(orderRepository, courierRepository);
    }

    @Test
    public void givenOrders_whenFindAll_returnAllOrders()
    {
        // given
        final Order oneOrder = new Order()
            .withId("order-1")
            .withDescription("I want a pizza cut into very small slices");
        final Order otherOrder = new Order()
            .withId("order-2")
            .withDescription("I want a cake cut into very small slices");
        final List<Order> allOrders = Arrays.asList(oneOrder, otherOrder);
        Mockito.when(orderRepository.findAll()).thenReturn(allOrders);

        // when
        final List<Order> orders = orderService.findAll();

        // return
        Assertions.assertEquals(orders.size(), 2);
        Assertions.assertEquals(orders, allOrders);
    }

    @Test
    public void givenNoOrders_whenFindAll_returnEmptySetOfOrders()
    {
        // given
        Mockito.when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        final List<Order> orders = orderService.findAll();

        // return
        Assertions.assertTrue(orders.isEmpty());
    }

    @Test
    public void givenUnknownCourier_whenFindOrdersByCourier_returnNotFoundException()
    {
        // given
        final String unknownCourierId = "unknownCourier";
        Mockito.when(courierRepository.findById(unknownCourierId)).thenReturn(null);

        // when
        // return
        Assertions.assertThrows(BackenderApiException.class, () -> orderService.findAllBy(unknownCourierId));
    }

    @Test
    public void givenOrdersAndCourierWithBoxAndMotorcycle_whenFindOrdersByCourier_returnAvailableOrders()
    {
        // given
        final String courierId = "courierId";
        final Location courierLocation = new Location(41.411094, 2.136922); // av. tibidado
        final Courier courier = new Courier()
            .withId(courierId)
            .withBox(true)
            .withVehicle(Vehicle.MOTORCYCLE)
            .withLocation(courierLocation);
        final Order order2 = new Order()
            .withId("order-2")
            .withDescription("I want a cake")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.397889, 2.131156)); // les tres torres (0.5 m)
        final Order order1 = new Order()
            .withId("order-1")
            .withDescription("I want a hamburguer")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.382548, 2.185791)); // barceloneta (7 - 7,5)
        final Order order3 = new Order()
            .withId("order-3")
            .withDescription("I want a pizza cut into very small slices")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.448295, 2.249292)); // badalona (14 - 15 km)
        final Order order4 = new Order()
            .withId("order-4")
            .withDescription("I want bread")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.392690, 2.143239)); // francesc macia (tram) 2,5km
        final List<Order> allOrders = Arrays.asList(order1, order2, order3, order4);

        Mockito.when(courierRepository.findById(courierId)).thenReturn(courier);
        Mockito.when(orderRepository.findAll()).thenReturn(allOrders);

        // when
        final List<Order> orders = orderService.findAllBy(courierId);

        // return
        Assertions.assertEquals(allOrders.size(), orders.size());
        Assertions.assertEquals(allOrders, orders);
    }

    @Test
    public void givenOrdersAndCourierWithBoxAndBicycle_whenFindOrdersByCourier_returnAvailableOrders()
    {
        // given
        final String courierId = "courierId";
        final Location courierLocation = new Location(41.411094, 2.136922); // av. tibidado
        final Courier courier = new Courier()
            .withId(courierId)
            .withBox(true)
            .withVehicle(Vehicle.BICYCLE)
            .withLocation(courierLocation);
        final Order order2 = new Order()
            .withId("order-2")
            .withDescription("I want a cake")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.397889, 2.131156)); // les tres torres (0.5 m)
        final Order order1 = new Order()
            .withId("order-1")
            .withDescription("I want a hamburguer")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.382548, 2.185791)); // barceloneta (7 - 7,5)
        final Order order3 = new Order()
            .withId("order-3")
            .withDescription("I want a pizza cut into very small slices")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.448295, 2.249292)); // badalona (14 - 15 km)
        final Order order4 = new Order()
            .withId("order-4")
            .withDescription("I want bread")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.392690, 2.143239)); // francesc macia (tram) 2,5km
        final List<Order> allOrders = Arrays.asList(order1, order2, order3, order4);
        final List<Order> expectedOrders = Arrays.asList(order2, order4);
//        final List<Order> expectedOrders = allOrders;

        Mockito.when(courierRepository.findById(courierId)).thenReturn(courier);
        Mockito.when(orderRepository.findAll()).thenReturn(allOrders);

        // when
        final List<Order> orders = orderService.findAllBy(courierId);

        // return
        Assertions.assertEquals(expectedOrders.size(), orders.size());
        Assertions.assertEquals(expectedOrders, orders);
    }

    @Test
    public void givenOrdersAndCourierWithoutBoxAndMotorcycle_whenFindOrdersByCourier_returnAvailableOrders()
    {
// given
        final String courierId = "courierId";
        final Location courierLocation = new Location(41.411094, 2.136922); // av. tibidado
        final Courier courier = new Courier()
            .withId(courierId)
            .withBox(false)
            .withVehicle(Vehicle.MOTORCYCLE)
            .withLocation(courierLocation);
        final Order order2 = new Order()
            .withId("order-2")
            .withDescription("I want a cake")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.397889, 2.131156)); // les tres torres (0.5 m)
        final Order order1 = new Order()
            .withId("order-1")
            .withDescription("I want a hamburguer")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.382548, 2.185791)); // barceloneta (7 - 7,5)
        final Order order3 = new Order()
            .withId("order-3")
            .withDescription("I want a pizza cut into very small slices")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.448295, 2.249292)); // badalona (14 - 15 km)
        final Order order4 = new Order()
            .withId("order-4")
            .withDescription("I want bread")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.392690, 2.143239)); // francesc macia (tram) 2,5km
        final List<Order> allOrders = Arrays.asList(order1, order2, order3, order4);
        final List<Order> expectedOrders = Arrays.asList(order1, order4);

        Mockito.when(courierRepository.findById(courierId)).thenReturn(courier);
        Mockito.when(orderRepository.findAll()).thenReturn(allOrders);

        // when
        final List<Order> orders = orderService.findAllBy(courierId);

        // return
        Assertions.assertEquals(expectedOrders.size(), orders.size());
        Assertions.assertEquals(expectedOrders, orders);
    }

    @Test
    public void givenOrdersAndCourierWithoutBoxAndBicycle_whenFindOrdersByCourier_returnAvailableOrders()
    {
        // given
        final String courierId = "courierId";
        final Location courierLocation = new Location(41.411094, 2.136922); // av. tibidado
        final Courier courier = new Courier()
            .withId(courierId)
            .withBox(false)
            .withVehicle(Vehicle.BICYCLE)
            .withLocation(courierLocation);
        final Order order2 = new Order()
            .withId("order-2")
            .withDescription("I want a cake")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.397889, 2.131156)); // les tres torres (0.5 m)
        final Order order1 = new Order()
            .withId("order-1")
            .withDescription("I want a hamburguer")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.382548, 2.185791)); // barceloneta (7 - 7,5)
        final Order order3 = new Order()
            .withId("order-3")
            .withDescription("I want a pizza cut into very small slices")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.448295, 2.249292)); // badalona (14 - 15 km)
        final Order order4 = new Order()
            .withId("order-4")
            .withDescription("I want bread")
            .withPickup(new Location(41.399056, 2.119070)) // sarria
            .withDelivery(new Location(41.392690, 2.143239)); // francesc macia (tram) 2,5km
        final List<Order> allOrders = Arrays.asList(order1, order2, order3, order4);
//        final List<Order> expectedOrders = Arrays.asList(order1, order4);
        final List<Order> expectedOrders = Arrays.asList(order4);

        Mockito.when(courierRepository.findById(courierId)).thenReturn(courier);
        Mockito.when(orderRepository.findAll()).thenReturn(allOrders);

        // when
        final List<Order> orders = orderService.findAllBy(courierId);

        // return
        Assertions.assertEquals(expectedOrders.size(), orders.size());
        Assertions.assertEquals(expectedOrders, orders);
    }
}
