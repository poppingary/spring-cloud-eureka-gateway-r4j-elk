package org.example.gatewayservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/fallback")
public class FallbackController {
    @RequestMapping("/order")
    public Mono<String> orderServiceFallBack() {
        return Mono.just("Order service is currently unavailable. Please try again later.");
    }
    @RequestMapping("/payment")
    public Mono<String> paymentServiceFallBack() {
        return Mono.just("Payment service is currently unavailable. Please try again later.");
    }
}