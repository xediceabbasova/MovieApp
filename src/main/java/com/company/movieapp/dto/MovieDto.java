package com.company.movieapp.dto;

import com.company.movieapp.model.Genre;

public record MovieDto(
        String movieId,
        String title,
        String director,
        Integer releaseYear,
        Genre genre
) {
}
