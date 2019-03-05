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
import com.glovoapp.backender.orders.domain.Order;
import com.glovoapp.backender.orders.domain.Vehicle;
import com.glovoapp.backender.orders.repo.CourierRepository;
import com.glovoapp.backender.orders.repo.OrderRepository;


/**
 *
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest
{
    private static final Order MOCK_ORDER1;
    private static final Order MOCK_ORDER2;
    private static final Order MOCK_ORDER3;
    private static final Order MOCK_ORDER4;

    static
    {
        // distance between pickup and delivery - (7 - 7,5)
        MOCK_ORDER1 = generateOrder("order-1", "I want a hamburguer", LocationData.SARRIA, LocationData.BARCELONETA);
        // distance between pickup and delivery - (0.5 m)
        MOCK_ORDER2 = generateOrder("order-2", "I want a cake", LocationData.SARRIA, LocationData.LES_TRES_TORRES);
        // distance between pickup and delivery - (14 - 15 km)
        MOCK_ORDER3 = generateOrder("order-3", "I want a pizza cut into very small slices", LocationData.SARRIA, LocationData.BADALONA);
        // distance between pickup and delivery - (0.5 m)
        MOCK_ORDER4 = generateOrder("order-4", "I want bread", LocationData.SARRIA, LocationData.FRANCESC_MACIA);
    }

    private static Order generateOrder(final String id, final String description, final LocationData pickup, final LocationData delivery)
    {
        return new Order()
            .withId(id)
            .withDescription(description)
            .withPickup(pickup.getLocation())
            .withDelivery(delivery.getLocation());
    }

    private static Courier generateCourier(final String id, final LocationData current, final boolean hasBox, final Vehicle vehicle)
    {
        return new Courier()
            .withId(id)
            .withBox(hasBox)
            .withVehicle(vehicle)
            .withLocation(current.getLocation());
    }

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CourierRepository courierRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp()
    {
        this.orderService = new OrderServiceImpl(orderRepository, courierRepository);
    }

    @Test
    void givenOrders_whenFindAll_returnAllOrders()
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
    void givenNoOrders_whenFindAll_returnEmptySetOfOrders()
    {
        // given
        Mockito.when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        final List<Order> orders = orderService.findAll();

        // return
        Assertions.assertTrue(orders.isEmpty());
    }

    @Test
    void givenUnknownCourier_whenFindOrdersByCourier_returnNotFoundException()
    {
        // given
        final String unknownCourierId = "unknownCourier";
        Mockito.when(courierRepository.findById(unknownCourierId)).thenReturn(null);

        // when
        // return
        Assertions.assertThrows(BackenderApiException.class, () -> orderService.findAllBy(unknownCourierId));
    }

    @Test
    void givenOrdersAndCourierWithBoxAndMotorcycle_whenFindOrdersByCourier_returnAvailableOrders()
    {
        // given
        final String courierId = "courierId";
        final Courier courier = generateCourier(courierId, LocationData.AV_TIBIDABO, true, Vehicle.MOTORCYCLE);
        final List<Order> allOrders = Arrays.asList(MOCK_ORDER1, MOCK_ORDER2, MOCK_ORDER3, MOCK_ORDER4);

        Mockito.when(courierRepository.findById(courierId)).thenReturn(courier);
        Mockito.when(orderRepository.findAll()).thenReturn(allOrders);

        // when
        final List<Order> orders = orderService.findAllBy(courierId);

        // return
        Assertions.assertEquals(allOrders.size(), orders.size());
        Assertions.assertEquals(allOrders, orders);
    }

    @Test
    void givenOrdersAndCourierWithBoxAndBicycle_whenFindOrdersByCourier_returnAvailableOrders()
    {
        // given
        final String courierId = "courierId";
        final Courier courier = generateCourier(courierId, LocationData.AV_TIBIDABO, true, Vehicle.BICYCLE);
        final List<Order> allOrders = Arrays.asList(MOCK_ORDER1, MOCK_ORDER2, MOCK_ORDER3, MOCK_ORDER4);
        final List<Order> expectedOrders = Arrays.asList(MOCK_ORDER2, MOCK_ORDER4);
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
    void givenOrdersAndCourierWithoutBoxAndMotorcycle_whenFindOrdersByCourier_returnAvailableOrders()
    {
        // given
        final String courierId = "courierId";
        final Courier courier = generateCourier(courierId, LocationData.AV_TIBIDABO, false, Vehicle.MOTORCYCLE);
        final List<Order> allOrders = Arrays.asList(MOCK_ORDER1, MOCK_ORDER2, MOCK_ORDER3, MOCK_ORDER4);
        final List<Order> expectedOrders = Arrays.asList(MOCK_ORDER1, MOCK_ORDER4);

        Mockito.when(courierRepository.findById(courierId)).thenReturn(courier);
        Mockito.when(orderRepository.findAll()).thenReturn(allOrders);

        // when
        final List<Order> orders = orderService.findAllBy(courierId);

        // return
        Assertions.assertEquals(expectedOrders.size(), orders.size());
        Assertions.assertEquals(expectedOrders, orders);
    }

    @Test
    void givenOrdersAndCourierWithoutBoxAndBicycle_whenFindOrdersByCourier_returnAvailableOrders()
    {
        // given
        final String courierId = "courierId";
        final Courier courier = generateCourier(courierId, LocationData.AV_TIBIDABO, false, Vehicle.BICYCLE);
        final List<Order> allOrders = Arrays.asList(MOCK_ORDER1, MOCK_ORDER2, MOCK_ORDER3, MOCK_ORDER4);
        final List<Order> expectedOrders = Collections.singletonList(MOCK_ORDER4);
//        final List<Order> expectedOrders = Arrays.asList(order1, order4);

        Mockito.when(courierRepository.findById(courierId)).thenReturn(courier);
        Mockito.when(orderRepository.findAll()).thenReturn(allOrders);

        // when
        final List<Order> orders = orderService.findAllBy(courierId);

        // return
        Assertions.assertEquals(expectedOrders.size(), orders.size());
        Assertions.assertEquals(expectedOrders, orders);
    }
}
