package com.cms.inventory.dto;

import com.cms.shared.model.TheatreEntity;

public record TheatreResponse(
        Long id,
        String name,
        Long partnerId,
        String city,
        String address,
        String status,
        String createdAt) {
    public static TheatreResponse fromEntity(TheatreEntity entity) {
        return new TheatreResponse(
                entity.getId(),
                entity.getName(),
                entity.getPartnerId(),
                entity.getCity(),
                entity.getAddress(),
                entity.getStatus().toString(),
                entity.getCreatedAt().toString()
        );
    }
}
