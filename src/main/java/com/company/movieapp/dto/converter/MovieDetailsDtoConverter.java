package com.company.movieapp.dto.converter;

import com.company.movieapp.dto.MovieDetailsDto;
import com.company.movieapp.model.MovieDetails;
import org.springframework.stereotype.Component;

@Component
public class MovieDetailsDtoConverter {

    public MovieDetailsDto convert(MovieDetails from){
        return new MovieDetailsDto(
                from.getDescription(),
                from.getDurationMinutes(),
                from.getImageUrl(),
                from.getActors()
        );
    }

}
