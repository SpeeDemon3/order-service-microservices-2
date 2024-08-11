package com.aruiz.microservices.order_services.service.impl;

import com.aruiz.microservices.order_services.controller.dto.OrderRequest;
import com.aruiz.microservices.order_services.model.Order;
import com.aruiz.microservices.order_services.repository.OrderRepository;
import com.aruiz.microservices.order_services.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;

    @Override
    public void placeOrder(OrderRequest orderRequest) {
        log.info("Request values: {}", orderRequest);
        var order = mapToOrder(orderRequest);
        orderRepository.save(order);
    }

    private static Order mapToOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setSkuCode(orderRequest.skuCode());

        return order;
    }

}
