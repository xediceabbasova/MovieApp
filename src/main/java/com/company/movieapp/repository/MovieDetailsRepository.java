package com.company.movieapp.repository;

import com.company.movieapp.model.Movie;
import com.company.movieapp.model.MovieDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieDetailsRepository extends JpaRepository<MovieDetails, String> {
    Optional<MovieDetails> findByMovie(Movie movie);
}