package com.cms.inventory.service;

import com.cms.inventory.dto.TheatreRequest;
import com.cms.inventory.dto.TheatreResponse;
import com.cms.inventory.repository.PartnerRepository;
import com.cms.inventory.repository.TheatreRepository;
import com.cms.shared.model.TenantEntity;
import com.cms.shared.model.TheatreEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TheatreService {

    private final TheatreRepository theatreRepository;
    private final PartnerRepository partnerRepository;

    public List<TheatreResponse> get() {
        return theatreRepository.findAll()
                .stream()
                .map(TheatreResponse::fromEntity)
                .toList();
    }

    public TheatreResponse create(TheatreRequest request) {
        Optional<TenantEntity> tenantEntity = partnerRepository.findById(request.partnerId());
        if (tenantEntity.isEmpty()) throw new IllegalArgumentException("Invalid tenant ID");
        TheatreEntity entity = new TheatreEntity();
        entity.setPartnerId(request.partnerId());
        entity.setName(request.name());
        entity.setCity(request.city());
        entity.setAddress(request.address());
        entity = theatreRepository.save(entity);
        return TheatreResponse.fromEntity(entity);
    }
}
