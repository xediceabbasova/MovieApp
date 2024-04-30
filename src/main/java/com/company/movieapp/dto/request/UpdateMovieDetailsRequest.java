package com.company.movieapp.dto.request;

import java.util.List;

public record UpdateMovieDetailsRequest(
        String description,
        Integer durationMinutes,
        String imageUrl,
        List<String> actors
) {
}
