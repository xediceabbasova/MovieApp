package com.company.movieapp.elasticsearch.dto;

import java.util.List;

public record SearchRequestDto(
        List<String> fieldName,
        List<String> searchValue
) {
}
