package org.example.orderservice.controller;

import org.example.orderservice.dto.TransactionRequest;
import org.example.orderservice.dto.TransactionResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.orderservice.service.OrderService;

@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    // Endpoint for POST requests to save an order
    @PostMapping
    public ResponseEntity<TransactionResponse> saveOrder(@RequestBody TransactionRequest transactionRequest) {
        return orderService.saveOrder(transactionRequest);
    }

    // Optional: Endpoint to test connectivity via GET
    @GetMapping
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("Order endpoint is up and running.");
    }
}