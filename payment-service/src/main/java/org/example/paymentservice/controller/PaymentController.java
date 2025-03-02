package org.example.paymentservice.controller;

import org.example.paymentservice.entity.Payment;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.example.paymentservice.service.PaymentService;

@RestController
@AllArgsConstructor
@RequestMapping("payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public Payment processPayment(@RequestBody Payment payment) {
        return paymentService.processPayment(payment);
    }

    @GetMapping("/{orderId}")
    public Payment findPaymentByOrderId(@PathVariable long orderId) {
        return paymentService.findPaymentByOrderId(orderId);
    }
}