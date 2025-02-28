package org.example.paymentservice.service;

import org.example.paymentservice.entity.Payment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.example.paymentservice.repository.PaymentRepository;

import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Payment processPayment(Payment payment) {
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setStatus(isPaymentSuccessful());
        return paymentRepository.save(payment);
    }

    private String isPaymentSuccessful() {
        return new Random().nextBoolean() ? "SUCCESS" : "FAILED";
    }
}