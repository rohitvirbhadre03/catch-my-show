package com.cms.payment.gateway;


import com.cms.payment.dto.PaymentContext;
import com.cms.payment.dto.PaymentResponse;
import com.cms.shared.types.PaymentMethod;
import com.cms.shared.types.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class CardPaymentProcessor implements PaymentProcessor {

    public PaymentMethod method() {
        return PaymentMethod.CARD;
    }

    public PaymentResponse process(PaymentContext ctx) {
        var txId = "pay_" + java.util.UUID.randomUUID();
        return new PaymentResponse(txId, PaymentStatus.SUCCEEDED, "DUMMY", "Card payment accepted (dummy)");
    }
}