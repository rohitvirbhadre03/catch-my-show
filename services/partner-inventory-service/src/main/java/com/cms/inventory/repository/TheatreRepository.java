package com.cms.inventory.repository;

import com.cms.shared.model.TheatreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TheatreRepository extends JpaRepository<TheatreEntity, Long> {

    Optional<TheatreEntity> findByPartnerIdAndId(Long partnerId, Long id);
}
