package com.cms.inventory.dto;

import com.cms.shared.model.ScreenEntity;
import com.cms.shared.model.ShowEntity;
import com.fasterxml.jackson.databind.JsonNode;

public record ShowResponse(
        Long id,
        Long screenId,
        String startAt,
        String endAt,
        String createdAt) {
    public static ShowResponse fromEntity(ShowEntity entity) {
        return new ShowResponse(
                entity.getId(),
                entity.getScreen().getId(),
                entity.getStartAt().toString(),
                entity.getEndAt().toString(),
                entity.getCreatedAt().toString()
        );
    }
}
