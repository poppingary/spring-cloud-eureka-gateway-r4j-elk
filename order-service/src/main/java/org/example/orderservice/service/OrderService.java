package org.example.orderservice.service;

import org.example.orderservice.dto.Payment;
import org.example.orderservice.dto.TransactionRequest;
import org.example.orderservice.dto.TransactionResponse;
import org.example.orderservice.entity.Order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.example.orderservice.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    @Value("${microservices.payment-service.endpoints.url}")
    private String PAYMENT_SERVICE_URL;

    public ResponseEntity<TransactionResponse> saveOrder(TransactionRequest transactionRequest) {
        Order order = orderRepository.save(transactionRequest.getOrder());
        return processPayment(order);
    }

    private ResponseEntity<TransactionResponse> processPayment(Order order) {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setOrder(order);
        transactionResponse.setAmount(order.getPrice() * order.getQuantity());

        try {
            Payment payment = new Payment();
            payment.setOrderId(order.getId());
            payment.setAmount(order.getPrice() * order.getQuantity());
            Payment paymentResponse = restTemplate.postForObject(
                    PAYMENT_SERVICE_URL, payment, Payment.class
            );

            if (paymentResponse != null) {
                transactionResponse.setTransactionId(paymentResponse.getTransactionId());
                if ("SUCCESS".equalsIgnoreCase(paymentResponse.getStatus())) {
                    transactionResponse.setMessage("Payment processed successfully");
                } else {
                    transactionResponse.setMessage("Payment processing failed");
                }
            }
        } catch (RestClientException ex) {
            transactionResponse.setMessage("Payment processing error: " + ex.getMessage());
        }

        return ResponseEntity.ok(transactionResponse);
    }
}