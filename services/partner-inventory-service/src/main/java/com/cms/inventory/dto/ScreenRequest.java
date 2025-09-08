package com.cms.inventory.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record ScreenRequest(Long theatreId, Long partnerId, String name, JsonNode seatLayout) {
}
