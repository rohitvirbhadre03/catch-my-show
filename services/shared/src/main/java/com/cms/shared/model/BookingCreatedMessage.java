package com.cms.shared.model;

import java.time.LocalDateTime;
import java.util.List;

public record BookingCreatedMessage(
        Long bookingId,
        Long userId,
        Long showId,
        List<String> seats,
        Integer total,
        LocalDateTime createdAt
) {
}
