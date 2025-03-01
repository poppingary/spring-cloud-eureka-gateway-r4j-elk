package org.example.orderservice.service;

import org.example.orderservice.dto.Payment;
import org.example.orderservice.dto.TransactionRequest;
import org.example.orderservice.dto.TransactionResponse;
import org.example.orderservice.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.example.orderservice.repository.OrderRepository;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

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
                    "http://PAYMENT-SERVICE/payment", payment, Payment.class
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