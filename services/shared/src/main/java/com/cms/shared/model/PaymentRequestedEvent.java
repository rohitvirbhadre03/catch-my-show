package com.cms.shared.model;

public record PaymentRequestedEvent(
        int version,
        Long bookingId,
        Long userId,
        Integer amount,
        String currency
) {}