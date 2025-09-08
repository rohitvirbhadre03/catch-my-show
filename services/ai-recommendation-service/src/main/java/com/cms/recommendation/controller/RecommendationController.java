package com.cms.recommendation.controller;

import com.cms.recommendation.dto.RecommendationResponse;
import com.cms.recommendation.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService service;

    @GetMapping
    public RecommendationResponse get(@RequestParam Long userId, @RequestParam String city) {
        return service.recommend(userId, city);
    }
}

