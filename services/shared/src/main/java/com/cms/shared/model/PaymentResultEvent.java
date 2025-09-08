package com.cms.shared.model;

public record PaymentResultEvent(
        int version,
        Long bookingId,
        String paymentId,
        String provider,
        String status,
        String message
) {}
