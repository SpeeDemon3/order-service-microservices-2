package com.aruiz.microservices.order_services.service;

import com.aruiz.microservices.order_services.controller.dto.OrderRequest;

public interface OrderService {
    void placeOrder(OrderRequest orderRequest);
}
