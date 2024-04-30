package com.company.movieapp.dto.converter;

import com.company.movieapp.dto.MovieDto;
import com.company.movieapp.model.Movie;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieDtoConverter {

    public MovieDto convert(Movie from) {
        return new MovieDto(
                from.getId(),
                from.getTitle(),
                from.getDirector(),
                from.getReleaseYear(),
                from.getGenre()
        );
    }

    public List<MovieDto> convert(List<Movie> from) {
        return from.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }


}
