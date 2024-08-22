package com.aruiz.microservices.order_services.service.impl;

import com.aruiz.microservices.order_services.client.InventoryClient;
import com.aruiz.microservices.order_services.controller.dto.OrderRequest;
import com.aruiz.microservices.order_services.controller.dto.OrderResponse;
import com.aruiz.microservices.order_services.model.Order;
import com.aruiz.microservices.order_services.repository.OrderRepository;
import com.aruiz.microservices.order_services.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;


    @Override
    public void placeOrder(OrderRequest orderRequest) {
        log.info("Request values: {}", orderRequest);
        boolean inStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (inStock) {
            var order = mapToOrder(orderRequest);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Product with Skucode " + orderRequest.skuCode() + " is not in stock");
        }

    }

    @Override
    public List<OrderResponse> findAllOrders() {

        Optional<List<Order>> optionalOrders = Optional.of(orderRepository.findAll());

        if (!optionalOrders.isEmpty()) {
            List<OrderResponse> orderResponseList = new ArrayList<>();

            for (Order order : optionalOrders.get()) {
                orderResponseList.add(mapToOrderResponse(order));
            }

            return orderResponseList;
        }

        log.error("Orders not found.");
        return null;
    }

    @Override
    public OrderResponse findOrderById(Long id) {

        Optional<Order> optionalOrder = orderRepository.findById(id);

        if (!optionalOrder.isEmpty()) {

            return mapToOrderResponse(optionalOrder.get());

        }

        return null;
    }

    private static Order mapToOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setSkuCode(orderRequest.skuCode());

        return order;
    }

    private static OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getSkuCode(),
                order.getPrice(),
                order.getQuantity()
        );

        return response;
    }

}
