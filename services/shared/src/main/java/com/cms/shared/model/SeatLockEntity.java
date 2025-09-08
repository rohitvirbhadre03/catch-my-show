package com.cms.shared.model;

import com.cms.shared.types.SeatLockStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "seat_lock")
public class SeatLockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "show_id", nullable = false)
    private Long showId;

    @Column(name = "seat_label", nullable = false, length = 32)
    private String seatLabel;

    @Column(name = "hold_from", nullable = false)
    private OffsetDateTime holdFrom;

    @Column(name = "hold_until", nullable = false)
    private OffsetDateTime holdUntil;

    @Column(name = "status", nullable = false, length = 32)
    @Builder.Default
    private String status = SeatLockStatus.HELD.name();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "timestamptz DEFAULT now()")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false,
            columnDefinition = "timestamptz DEFAULT now()")
    private OffsetDateTime updatedAt;

}
