package com.cms.recommendation.dto;

public record Recommendation(Long movieId, String title, double score, String reason) {
}
