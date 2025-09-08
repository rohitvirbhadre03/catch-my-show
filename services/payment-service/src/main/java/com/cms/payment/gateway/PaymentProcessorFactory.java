package com.cms.payment.gateway;

import com.cms.shared.types.PaymentMethod;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PaymentProcessorFactory {

    private final Map<PaymentMethod, PaymentProcessor> byMethod;

    public PaymentProcessorFactory(List<PaymentProcessor> processors) {
        this.byMethod = processors.stream()
                .collect(Collectors.toUnmodifiableMap(PaymentProcessor::method, p -> p));
    }

    public PaymentProcessor get(PaymentMethod method) {
        var p = byMethod.get(method);
        if (p == null) throw new IllegalArgumentException("Unsupported method: " + method);
        return p;
    }
}
