package com.cms.payment.dto;

import com.cms.shared.types.PaymentMethod;

public record PaymentRequest(
        Long bookingId,
        Integer amount,
        String currency,
        PaymentMethod method,
        Card card,
        Upi upi,
        String idempotencyKey
) {
    public record Card(String number, String expMonth, String expYear, String cvv) {
    }

    public record Upi(String upi) {
    }
}
