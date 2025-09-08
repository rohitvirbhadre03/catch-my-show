package com.cms.shared.model;

import com.cms.shared.types.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment")
public class PaymentEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="partner_id", nullable=false)
    private Long partnerId;

    @Column(name="booking_id", nullable=false)
    private Long bookingId;

    @Column(nullable=false)
    private String provider;

    @Column(name="amount", nullable=false)
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable=false) private PaymentStatus status = PaymentStatus.PENDING;

    @CreationTimestamp
    @Column(name="created_at", nullable=false, insertable=false, updatable=false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", nullable=false, insertable=false, updatable=false)
    private OffsetDateTime updatedAt;
}
