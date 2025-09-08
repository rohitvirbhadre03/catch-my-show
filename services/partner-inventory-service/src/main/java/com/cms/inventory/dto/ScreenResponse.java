package com.cms.inventory.dto;

import com.cms.shared.model.ScreenEntity;
import com.fasterxml.jackson.databind.JsonNode;

public record ScreenResponse(
        Long id,
        Long theatreId,
        String name,
        JsonNode seatLayout,
        String createdAt) {
    public static ScreenResponse fromEntity(ScreenEntity entity) {
        return new ScreenResponse(
                entity.getId(),
                entity.getTheatre().getId(),
                entity.getName(),
                entity.getSeatLayout(),
                entity.getCreatedAt().toString()
        );
    }
}
