package com.cms.shared.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "booked_seat")
public class BookedSeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name="booking_id", nullable=false) private Long bookingId;

    @Column(name="seat_label", nullable=false, length=32) private String seatLabel;

    @CreationTimestamp
    @Column(name="created_at", nullable=false, insertable=false, updatable=false)
    private OffsetDateTime createdAt;

}
