package com.company.movieapp.dto.converter;

import com.company.movieapp.dto.UserDto;
import com.company.movieapp.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDtoConverter {

    private final MovieDtoConverter converter;

    public UserDtoConverter(MovieDtoConverter converter) {
        this.converter = converter;
    }

    public UserDto convert(User from) {
        return new UserDto(
                from.getMail(),
                from.getMovies()
                        .stream()
                        .map(converter::convert)
                        .collect(Collectors.toSet())
        );
    }

    public List<UserDto> convert(List<User> from) {
        return from.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
