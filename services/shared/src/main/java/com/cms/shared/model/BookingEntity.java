package com.cms.shared.model;

import com.cms.shared.types.BookingStatus;
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
@Table(name = "booking")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "show_id", nullable = false)
    private Long showId;

    private Long userId;

    @Column(nullable = false) private Integer subtotal;
    private String discountCode;

    @Column(nullable = false) private
    @Builder.Default
    Integer discount = 0;

    @Column(name = "taxes_fees", nullable = false)
    @Builder.Default
    private Integer taxesFees = 0;

    @Column(nullable = false)
    private Integer total;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false) private BookingStatus status = BookingStatus.PENDING_PAYMENT;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

}
