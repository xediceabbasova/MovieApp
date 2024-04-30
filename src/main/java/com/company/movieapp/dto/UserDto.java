package com.company.movieapp.dto;

import java.util.Set;

public record UserDto(
        String mail,
        Set<MovieDto> movies

) {
}
