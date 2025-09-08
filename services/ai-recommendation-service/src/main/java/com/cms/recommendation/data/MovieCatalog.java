package com.cms.recommendation.data;

import com.cms.recommendation.dto.Movie;

import java.util.List;

public final class MovieCatalog {
    private MovieCatalog() {}
    public static final List<Movie> MOVIES = List.of(
            new Movie(1L, "Sky Warriors",     "Action",   "EN", "fighter jets, hero pilot, national threat"),
            new Movie(2L, "Monsoon Melodies", "Romance",  "HI", "rainy college love, soulful music"),
            new Movie(3L, "Quantum Heist",    "Sci-Fi",   "EN", "time travel, heist crew, paradox twists"),
            new Movie(4L, "Kolkata Crimes",   "Thriller", "HI", "detective, serial mystery, dark nights"),
            new Movie(5L, "Laugh Out Loud",   "Comedy",   "EN", "standup comic, road trip, misadventures"),
            new Movie(6L, "Chai & Chess",     "Drama",    "HI", "small town prodigy, championships, mentor"),
            new Movie(7L, "Dragon’s Feast",   "Fantasy",  "EN", "dragons, ancient prophecy, epic quest"),
            new Movie(8L, "Street Beats",     "Musical",  "HI", "dance battle, friends, Mumbai dreams")
    );
}
