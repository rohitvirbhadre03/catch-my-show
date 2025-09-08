package com.cms.shared.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "performance")
public class ShowEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "screen_id")
    private ScreenEntity screen;

    @Column(name="start_at", nullable=false)
    private LocalDateTime startAt;

    @Column(name="end_at")
    private LocalDateTime endAt;

    @CreationTimestamp
    @Column(name="created_at", nullable=false, insertable=false, updatable=false)
    private LocalDateTime createdAt;
}
