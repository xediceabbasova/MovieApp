package com.company.movieapp.service;

import com.company.movieapp.dto.MovieDto;
import com.company.movieapp.dto.converter.MovieDtoConverter;
import com.company.movieapp.dto.request.MovieRequest;
import com.company.movieapp.exception.MovieNotFoundException;
import com.company.movieapp.model.Movie;
import com.company.movieapp.repository.MovieRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieDtoConverter movieDtoConverter;

    public MovieService(MovieRepository movieRepository, MovieDtoConverter movieDtoConverter) {
        this.movieRepository = movieRepository;
        this.movieDtoConverter = movieDtoConverter;
    }

    @CachePut(value = "movies", key = "#result.movieId")
    public MovieDto createMovie(final MovieRequest request) {

        if (movieRepository.existsByTitleIgnoreCase(request.title())) {
            throw new RuntimeException("The movie already exists.");
        }

        Movie movie = new Movie(
                request.title(),
                request.director(),
                request.releaseYear(),
                request.genre());

        return movieDtoConverter.convert(movieRepository.save(movie));
    }

    @Cacheable(value = "movies", key = "#movieId")
    public MovieDto getMovieById(final String movieId) {
        return movieDtoConverter.convert(findMovieById(movieId));
    }

    @Cacheable(value = "movies")
    public List<MovieDto> getAllMovies() {
        return movieDtoConverter.convert(movieRepository.findAll());
    }

    @CacheEvict(value = "movies", allEntries = true)
    public MovieDto updateMovie(final String movieId, final MovieRequest request) {
        Movie movie = findMovieById(movieId);

        Movie updatedMovie = new Movie(
                movie.getId(),
                request.title() != null ? request.title() : movie.getTitle(),
                request.director() != null ? request.director() : movie.getDirector(),
                request.releaseYear() != null ? request.releaseYear() : movie.getReleaseYear(),
                request.genre() != null ? request.genre() : movie.getGenre()
        );

        return movieDtoConverter.convert(movieRepository.save(updatedMovie));
    }

    @CacheEvict(value = "movies", allEntries = true)
    public void deleteMovie(final String movieId) {
        findMovieById(movieId);
        movieRepository.deleteById(movieId);
    }

    protected Movie findMovieById(final String movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + movieId));

    }
}
