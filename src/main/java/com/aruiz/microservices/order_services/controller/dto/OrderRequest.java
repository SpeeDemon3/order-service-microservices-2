package com.aruiz.microservices.order_services.controller.dto;


import java.math.BigDecimal;

public record OrderRequest(Long id, String skuCode, BigDecimal price, Integer quantity) {
}
