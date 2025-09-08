package com.cms.shared.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "theatre")
public class TheatreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "partner_id", nullable = false)
    private Long partnerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 128)
    private String city;

    @Column(length = 512)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    @Builder.Default
    private TenantEntity.Status status = TenantEntity.Status.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;
}

