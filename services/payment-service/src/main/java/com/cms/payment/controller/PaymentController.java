package com.cms.payment.controller;

import com.cms.payment.dto.PaymentRequest;
import com.cms.payment.dto.PaymentResponse;
import com.cms.payment.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(
            @RequestBody PaymentRequest req) {

        var out = paymentService.pay(req);
        var uri = java.net.URI.create("/api/v1/payments/" + out.paymentId());
        return ResponseEntity.created(uri).body(out);
    }
}
