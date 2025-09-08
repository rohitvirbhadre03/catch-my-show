package com.cms.shared.model;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="screen")
public class ScreenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "theatre_id")
    private TheatreEntity theatre;

    @Column(nullable=false)
    private String name;

    @Type(JsonType.class)
    @Column(name = "seat_layout", columnDefinition = "json", nullable = false)
    private JsonNode seatLayout;

    @Column(name="seat_count")
    private Integer seatCount;

    @CreationTimestamp
    @Column(name="created_at", nullable=false, insertable=false, updatable=false)
    private OffsetDateTime createdAt;
}
