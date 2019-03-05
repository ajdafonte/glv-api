package com.glovoapp.backender.orders.rest;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
import com.google.gson.Gson;


/**
 *
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest
{
    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @Test
    void givenOrders_whenGetAllOrders_thenReturnAllOrders() throws Exception
    {
        // given
        final Order oneOrder = new Order()
            .withId("order-1")
            .withDescription("I want a pizza cut into very small slices");
        final List<Order> allOrders = Collections.singletonList(oneOrder);
        doReturn(allOrders).when(orderService).findAll();
        final String expectedContent = generateSuccessBody(allOrders);

        // when
        final ResultActions result = mvc.perform(get("/orders")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.content().string(expectedContent));
        verify(orderService, times(1)).findAll();
        verifyNoMoreInteractions(orderService);
    }

    private String generateSuccessBody(final List<Order> orders)
    {
        return new Gson().toJson(orders);
    }

    @Test
    void givenExistentCourier_whenGetOrdersForCourier_thenReturnSetOfOrders() throws Exception
    {
        // given
        final Order oneOrder = new Order()
            .withId("order-1")
            .withDescription("I want a pizza cut into very small slices");
        final List<Order> allOrders = Collections.singletonList(oneOrder);
        final String expectedContent = generateSuccessBody(allOrders);
        final String targetCourier = "courier-1";
        doReturn(allOrders).when(orderService).findAllBy(targetCourier);

        // when
        final ResultActions result = mvc.perform(get("/orders/{courierId}", targetCourier)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        // then
        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.content().string(expectedContent));
        verify(orderService, times(1)).findAllBy(targetCourier);
        verifyNoMoreInteractions(orderService);
    }

    @Test
    void givenUnknownCourier_whenGetOrdersForCourier_thenReturnNotFoundError() throws Exception
    {
        // given
        final String unknownCourierId = "courier-2";
        doThrow(new BackenderApiException(BackenderApiError.UNKNOWN_RESOURCE, "Unknown resource"))
            .when(orderService)
            .findAllBy(unknownCourierId);

        // then
        final ResultActions result = mvc.perform(get("/orders/{courierId}", unknownCourierId)
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        // then
        result.andExpect(MockMvcResultMatchers.status().is4xxClientError());
        // TODO - validate content of body response ??
        verify(orderService, times(1)).findAllBy(unknownCourierId);
        verifyNoMoreInteractions(orderService);
    }
}
