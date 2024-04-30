package com.company.movieapp.repository;

import com.company.movieapp.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    boolean existsByTitleIgnoreCase(String title);
}
