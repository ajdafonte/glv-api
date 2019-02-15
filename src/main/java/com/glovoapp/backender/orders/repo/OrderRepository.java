package com.glovoapp.backender.orders.repo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.glovoapp.backender.orders.domain.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


@Component
public class OrderRepository
{
    private static final String ORDERS_FILE = "/orders.json";
    private static final List<Order> orders;

    static
    {
        try (final Reader reader = new InputStreamReader(OrderRepository.class.getResourceAsStream(ORDERS_FILE)))
        {
            final Type type = new TypeToken<List<Order>>()
            {
            }.getType();
            orders = new Gson().fromJson(reader, type);
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public List<Order> findAll()
    {
        return new ArrayList<>(orders);
    }
}
