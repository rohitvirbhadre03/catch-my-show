package com.cms.payment.gateway;

import com.cms.payment.dto.PaymentContext;
import com.cms.payment.dto.PaymentResponse;
import com.cms.shared.types.PaymentMethod;

public interface PaymentProcessor {
    PaymentMethod method();
    PaymentResponse process(PaymentContext ctx);
}
