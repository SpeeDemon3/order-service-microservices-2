package com.aruiz.microservices.order_services.controller;

import com.aruiz.microservices.order_services.controller.dto.OrderRequest;
import com.aruiz.microservices.order_services.controller.dto.OrderResponse;
import com.aruiz.microservices.order_services.service.impl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        orderService.placeOrder(orderRequest);
        return "Order Placed Successfully";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> findAllOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse findOrderById(@PathVariable Long id) {
        return orderService.findOrderById(id);
    }


}
