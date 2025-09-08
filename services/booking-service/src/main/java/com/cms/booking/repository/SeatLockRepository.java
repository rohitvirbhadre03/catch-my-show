package com.cms.booking.repository;

import com.cms.shared.model.SeatLockEntity;
import com.cms.shared.types.SeatLockStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface SeatLockRepository extends JpaRepository<SeatLockEntity, Long> {

    @Query("""
             select l from SeatLockEntity l
            where l.showId = :showId
              and l.seatLabel in :labels
              and l.status = :held
              and l.holdUntil > :now
            """)
    List<SeatLockEntity> findActiveLocks(@Param("showId") Long showId,
                                         @Param("labels") List<String> labels,
                                         @Param("held") String held,
                                         @Param("now") OffsetDateTime now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
             update SeatLockEntity l
              set l.status = :expired
            where l.status = :held
              and l.holdUntil <= :now
            """)
    void expireHolds(@Param("held") String held,
                     @Param("expired") String expired,
                     @Param("now") OffsetDateTime now);
}
