package com.company.movieapp.service;

import com.company.movieapp.dto.MovieDetailsDto;
import com.company.movieapp.dto.converter.MovieDetailsDtoConverter;
import com.company.movieapp.dto.request.CreateMovieDetailsRequest;
import com.company.movieapp.dto.request.UpdateMovieDetailsRequest;
import com.company.movieapp.exception.MovieDetailsNotFoundException;
import com.company.movieapp.model.Movie;
import com.company.movieapp.model.MovieDetails;
import com.company.movieapp.repository.MovieDetailsRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MovieDetailsService {

    private final MovieDetailsRepository repository;
    private final MovieDetailsDtoConverter converter;
    private final MovieService movieService;

    public MovieDetailsService(MovieDetailsRepository repository, MovieDetailsDtoConverter converter, MovieService movieService) {
        this.repository = repository;
        this.converter = converter;
        this.movieService = movieService;
    }

    @CachePut(value = "movieDetails", key = "#request.movieId")
    public MovieDetailsDto createMovieDetails(final CreateMovieDetailsRequest request) {

        Movie movie = movieService.findMovieById(request.movieId());

        MovieDetails movieDetails = new MovieDetails(
                request.description(),
                request.durationMinutes(),
                request.imageUrl(),
                request.actors(),
                movie);
        return converter.convert(repository.save(movieDetails));
    }

    @Cacheable(value = "movieDetails", key = "#movieId")
    public MovieDetailsDto getMovieDetailsByMovieId(final String movieId) {
        return findMovieDetailsByMovieId(movieId)
                .map(converter::convert)
                .orElseThrow(() -> new MovieDetailsNotFoundException("Movie details not found for movie ID: " + movieId));

    }

    @CacheEvict(value = "movieDetails", allEntries = true)
    public MovieDetailsDto updateMovieDetails(final String movieId, final UpdateMovieDetailsRequest request) {
        MovieDetails movieDetails = findMovieDetailsByMovieId(movieId)
                .orElseThrow(() -> new MovieDetailsNotFoundException("Movie details not found for movie ID: " + movieId));

        MovieDetails updatedMovieDetails = new MovieDetails(
                movieDetails.getId(),
                request.description() != null ? request.description() : movieDetails.getDescription(),
                request.durationMinutes() != null ? request.durationMinutes() : movieDetails.getDurationMinutes(),
                request.imageUrl() != null ? request.imageUrl() : movieDetails.getImageUrl(),
                request.actors() != null ? request.actors() : movieDetails.getActors(),
                movieDetails.getMovie());

        return converter.convert(repository.save(updatedMovieDetails));
    }

    @CacheEvict(value = "movieDetails", allEntries = true)
    public void deleteMovieDetails(final String movieId) {
        findMovieDetailsByMovieId(movieId)
                .ifPresent(repository::delete);
    }

    private Optional<MovieDetails> findMovieDetailsByMovieId(final String movieId) {
        Movie movie = movieService.findMovieById(movieId);
        return repository.findByMovie(movie);
    }
}
