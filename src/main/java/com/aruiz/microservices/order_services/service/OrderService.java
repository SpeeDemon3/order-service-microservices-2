package com.aruiz.microservices.order_services.service;

import com.aruiz.microservices.order_services.controller.dto.OrderRequest;
import com.aruiz.microservices.order_services.controller.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    void placeOrder(OrderRequest orderRequest);
    List<OrderResponse> findAllOrders();
    OrderResponse findOrderById(Long id);
}
