package com.cms.inventory.service;

import com.cms.inventory.dto.PartnerRequest;
import com.cms.inventory.dto.PartnerResponse;
import com.cms.inventory.repository.PartnerRepository;
import com.cms.shared.model.TenantEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;

    public List<PartnerResponse> get() {
        return partnerRepository.findAll()
                .stream()
                .map(PartnerResponse::fromEntity)
                .toList();
    }

    public PartnerResponse create(PartnerRequest request) {
        TenantEntity entity = new TenantEntity();
        entity.setName(request.name());
        entity = partnerRepository.save(entity);
        return PartnerResponse.fromEntity(entity);
    }

}
