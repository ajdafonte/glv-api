package com.glovoapp.backender.orders.rest;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.glovoapp.backender.common.error.BackenderApiError;
import com.glovoapp.backender.common.error.BackenderApiException;
import com.glovoapp.backender.orders.domain.Order;
import com.glovoapp.backender.orders.service.OrderService;


/**
 * TODO - Improve data assertions and mock data
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest
{
    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void givenOrders_whenGetAllOrders_thenReturnAllOrders() throws Exception
    {
        // given
        final Order oneOrder = new Order()
            .withId("order-1")
            .withDescription("I want a pizza cut into very small slices");
        final List<Order> allOrders = Collections.singletonList(oneOrder);
        doReturn(allOrders).when(orderService).findAll();
//            given(orderService.findAll()).willReturn(allOrders);

        // when
        final ResultActions result = mvc.perform(get("/orders")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenExistentCourier_whenGetOrdersForCourier_thenReturnSetOfOrders() throws Exception
    {
        // given
        final Order oneOrder = new Order()
            .withId("order-1")
            .withDescription("I want a pizza cut into very small slices");

        final List<Order> allOrders = Collections.singletonList(oneOrder);

        doReturn(allOrders).when(orderService).findAllBy("courier-1");
//        given(orderService.findAllBy("courier-1")).willReturn(allOrders);

        // when
        final ResultActions result = mvc.perform(get("/orders/{courierId}", "courier-1")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenUnknownCourier_whenGetOrdersForCourier_thenReturnNotFoundError() throws Exception
    {
        // given
        final String unknownCourierId = "courier-2";
        doThrow(new BackenderApiException(BackenderApiError.UNKNOWN_RESOURCE, "Unknown resource"))
            .when(orderService)
            .findAllBy(unknownCourierId);
//        given(orderService.findAllBy()).willThrow(new BackenderApiException(BackenderApiError.UNKNOWN_RESOURCE, "Unknown resource"));

        // then
        final ResultActions result = mvc.perform(get("/orders/{courierId}", unknownCourierId)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        // then
        result.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
