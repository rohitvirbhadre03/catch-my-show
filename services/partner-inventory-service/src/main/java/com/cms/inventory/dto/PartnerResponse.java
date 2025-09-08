package com.cms.inventory.dto;

import com.cms.shared.model.TenantEntity;

public record PartnerResponse(
        Long id,
        String name,
        String status,
        String createdAt,
        String updatedAt) {
    public static PartnerResponse fromEntity(TenantEntity entity) {
        return new PartnerResponse(
                entity.getId(),
                entity.getName(),
                entity.getStatus().toString(),
                entity.getCreatedAt().toString(),
                entity.getUpdatedAt().toString()
        );
    }
}
