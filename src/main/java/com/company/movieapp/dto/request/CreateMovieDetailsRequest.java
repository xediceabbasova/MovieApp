package com.company.movieapp.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateMovieDetailsRequest(
        String description,
        Integer durationMinutes,
        String imageUrl,
        List<String> actors,
        @NotBlank(message = "Movie ID cannot be blank")
        String movieId
) {
}
