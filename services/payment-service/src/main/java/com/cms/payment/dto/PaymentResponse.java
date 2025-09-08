package com.cms.payment.dto;

import com.cms.shared.types.PaymentStatus;

public record PaymentResponse(
        String paymentId,
        PaymentStatus status,
        String provider,
        String message
) {
}
