package com.company.movieapp.dto.request;

import com.company.movieapp.model.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovieRequest(
        @NotBlank(message = "Title cannot be blank")
        String title,
        @NotBlank(message = "Director cannot be blank")
        String director,
        @NotNull(message = "Release year cannot be null")
        @Positive(message = "Release year must be a positive number")
        Integer releaseYear,
        @NotNull(message = "Genre cannot be null")
        Genre genre
) {
}
