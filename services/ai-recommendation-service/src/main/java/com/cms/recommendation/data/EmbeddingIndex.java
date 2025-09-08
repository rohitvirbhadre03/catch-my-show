package com.cms.recommendation.data;

import com.cms.recommendation.dto.Movie;
import com.cms.recommendation.dto.Recommendation;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.ai.embedding.EmbeddingModel;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EmbeddingIndex {

    private final EmbeddingModel embeddingModel;

    public EmbeddingIndex(EmbeddingModel embeddingModel) { this.embeddingModel = embeddingModel; }

    private final Map<Long, float[]> movieVecs = new HashMap<>();
    private final Map<Long, Movie> movies = new HashMap<>();

    @PostConstruct
    void init() {
        var texts = MovieCatalog.MOVIES.stream().map(this::movieText).toList();

        // Spring AI returns List<float[]>
        List<float[]> vectors = embeddingModel.embed(texts);  // <- batch embed

        for (int i = 0; i < MovieCatalog.MOVIES.size(); i++) {
            var m = MovieCatalog.MOVIES.get(i);
            movies.put(m.id(), m);
            movieVecs.put(m.id(), normalize(vectors.get(i)));
        }
    }

    public List<Recommendation> topMatches(String userProfileText, int limit) {
        float[] query = normalize(embeddingModel.embed(userProfileText)); // <- single embed
        return movies.values().stream()
                .map(m -> {
                    float sim = cosine(query, movieVecs.get(m.id()));
                    String reason = reasonFor(m, userProfileText, sim);
                    return new Recommendation(m.id(), m.title(), round2(sim), reason);
                })
                .sorted(Comparator.comparingDouble(Recommendation::score).reversed())
                .limit(limit)
                .toList();
    }

    private String movieText(Movie m) { return "%s | %s | %s | %s".formatted(m.title(), m.genre(), m.language(), m.description()); }

    private static float[] normalize(float[] v) {
        double n = 0; for (float x : v) n += x * x;
        if (n == 0) return v;
        float inv = (float) (1.0 / Math.sqrt(n));
        for (int i = 0; i < v.length; i++) v[i] *= inv;
        return v;
    }

    private static float cosine(float[] a, float[] b) {
        int len = Math.min(a.length, b.length);
        double dot = 0; for (int i = 0; i < len; i++) dot += (double) a[i] * b[i];
        return (float) dot; // cosine since inputs are normalized
    }

    private static double round2(double d) { return Math.round(d * 100.0) / 100.0; }

    private static String reasonFor(Movie m, String q, double sim) {
        var ql = q.toLowerCase();
        if (ql.contains(m.genre().toLowerCase())) return "genre match";
        if (ql.contains(m.language().toLowerCase())) return "language match";
        return sim > 0.8 ? "very similar" : "similar";
    }
}
