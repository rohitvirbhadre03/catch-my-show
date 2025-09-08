package com.cms.inventory.dto;

import java.time.LocalDateTime;

public record ShowRequest(Long screenId, LocalDateTime startAt, LocalDateTime endAt) {
}
