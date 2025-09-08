package com.cms.payment.service;

import com.cms.payment.dto.PaymentContext;
import com.cms.payment.dto.PaymentRequest;
import com.cms.payment.dto.PaymentResponse;
import com.cms.payment.gateway.PaymentProcessorFactory;
import com.cms.shared.types.PaymentStatus;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {

    private final PaymentProcessorFactory factory;

    @CircuitBreaker(name = "payment", fallbackMethod = "fallback")
    public PaymentResponse pay(PaymentRequest req) {
        var ctx = new PaymentContext(req);
        var processor = factory.get(req.method());
        return processor.process(ctx);
    }

    private PaymentResponse fallback(PaymentRequest req, String idempotencyHeader, Throwable cause) {
        return new PaymentResponse(
                "pay_fail_" + java.util.UUID.randomUUID(),
                PaymentStatus.FAILED,
                "DUMMY",
                "Payment temporarily unavailable"
        );
    }
}
