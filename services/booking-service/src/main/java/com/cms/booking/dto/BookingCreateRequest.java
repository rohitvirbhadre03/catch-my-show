package com.cms.booking.dto;

import java.util.List;

public record BookingCreateRequest(
        Long showId,
        List<String> seats,
        Integer subtotal,
        String discountCode,
        Integer discount,
        Integer taxesFees,
        Integer total,
        Long userId,
        Integer ttlSeconds
) {
}
