package com.cms.inventory.repository;

import com.cms.shared.model.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<TenantEntity, Long> {
}
