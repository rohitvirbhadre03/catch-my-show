package com.cms.booking.dto;

import com.cms.shared.model.BookingEntity;

public record BookingResponse(
        Long id,
        String status,
        Long showId,
        Long userId,
        Integer total
) {
    public static BookingResponse fromEntity(BookingEntity entity) {
        return new BookingResponse(
                entity.getId(),
                entity.getStatus().toString(),
                entity.getShowId(),
                entity.getUserId(),
                entity.getTotal()
        );
    }
}
