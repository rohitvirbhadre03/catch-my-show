package com.cms.recommendation.service;

import com.cms.recommendation.data.EmbeddingIndex;
import com.cms.recommendation.dto.RecommendationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
public class RecommendationService {

    private final EmbeddingIndex index;

    @Value("${recs.limit:5}")
    private final int defaultLimit;

    public RecommendationService(EmbeddingIndex index,
                                 @Value("${recs.limit:5}") int defaultLimit) {
        this.index = index;
        this.defaultLimit = defaultLimit;
    }

    private static final String[] GENRES = {"Action", "Romance", "Sci-Fi", "Thriller", "Comedy", "Drama", "Fantasy", "Musical"};
    private static final String[] LANGS = {"EN", "HI"};

    public RecommendationResponse recommend(Long userId, String city) {
        if (userId == null || city == null || city.isBlank()) {
            throw new IllegalArgumentException("userId and city are required");
        }

        int g = Math.floorMod(userId.intValue(), GENRES.length);
        int l = Math.floorMod((int) (userId / 3), LANGS.length);

        String profile = """
                User lives in %s. Prefers %s movies in %s. Enjoys %s storytelling.
                """.formatted(
                city,
                GENRES[g],
                LANGS[l],
                (g % 2 == 0 ? "fast-paced, high-stakes" : "emotional, character-driven")
        ).trim();

        var items = index.topMatches(profile, defaultLimit);
        return new RecommendationResponse(items);
    }
}

