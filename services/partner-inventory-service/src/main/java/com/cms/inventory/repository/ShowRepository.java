package com.cms.inventory.repository;

import com.cms.shared.model.ShowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowRepository extends JpaRepository<ShowEntity, Long> {
    List<ShowEntity> findByIdAndScreen_Theatre_CityAndStartAtGreaterThanEqualAndStartAtLessThan(
            Long showId, String city, LocalDateTime start, LocalDateTime end
    );
}
