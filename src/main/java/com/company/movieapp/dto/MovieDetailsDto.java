package com.company.movieapp.dto;

import java.util.List;

public record MovieDetailsDto(
        String description,
        Integer durationMinutes,
        String imageUrl,
        List<String> actors

) {
}
