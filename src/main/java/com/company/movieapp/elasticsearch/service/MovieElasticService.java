//package com.company.movieapp.elasticsearch.service;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.elasticsearch._types.query_dsl.Query;
//import co.elastic.clients.elasticsearch.core.SearchResponse;
//import co.elastic.clients.elasticsearch.core.search.Hit;
//import com.company.movieapp.elasticsearch.dto.SearchRequestDto;
//import com.company.movieapp.elasticsearch.model.MovieDocument;
//import com.company.movieapp.elasticsearch.repository.MovieElasticRepository;
//import com.company.movieapp.elasticsearch.util.ESUtil;
//import com.company.movieapp.model.Movie;
//import com.company.movieapp.model.MovieDetails;
//import com.company.movieapp.repository.MovieDetailsRepository;
//import com.company.movieapp.repository.MovieRepository;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//import java.util.function.Supplier;
//import java.util.stream.Collectors;
//
//@Service
//public class MovieElasticService {
//
//    private final MovieElasticRepository movieElasticRepository;
//    private final MovieRepository movieRepository;
//    private final MovieDetailsRepository movieDetailsRepository;
//    private final ElasticsearchClient elasticsearchClient;
//
//    public MovieElasticService(MovieElasticRepository movieElasticRepository, MovieRepository movieRepository, MovieDetailsRepository movieDetailsRepository, ElasticsearchClient elasticsearchClient) {
//        this.movieElasticRepository = movieElasticRepository;
//        this.movieRepository = movieRepository;
//        this.movieDetailsRepository = movieDetailsRepository;
//        this.elasticsearchClient = elasticsearchClient;
//    }
//
//    public void syncMovies() {
//        List<MovieDocument> movieDocuments = movieRepository.findAll().stream()
//                .map(this::convertToMovieDocument)
//                .toList();
//        movieElasticRepository.saveAll(movieDocuments);
//    }
//
//    public List<MovieDocument> getAllMoviesFromAllIndexes() {
//        SearchResponse<MovieDocument> response;
//        try {
//            Query query = ESUtil.createMatchAllQuery();
//            response = elasticsearchClient.search(q -> q.query(query).size(1000), MovieDocument.class);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return extractMovieDocumentFromResponse(response);
//    }
//
//    public List<MovieDocument> searchMoviesByFieldAndValue(SearchRequestDto request) {
//        SearchResponse<MovieDocument> response;
//        try {
//            Supplier<Query> querySupplier = ESUtil.buildQueryForFieldAndValue(request.fieldName().get(0), request.searchValue().get(0));
//            response = elasticsearchClient.search(q -> q.index("movies")
//                    .query(querySupplier.get()), MovieDocument.class);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return extractMovieDocumentFromResponse(response);
//    }
//
//    public List<MovieDocument> boolQueryFieldAndValue(SearchRequestDto request) {
//        SearchResponse<MovieDocument> response;
//        try {
//            Supplier<Query> querySupplier = ESUtil.createBoolQuery(request);
//            response = elasticsearchClient.search(q -> q.index("movies")
//                    .query(querySupplier.get()), MovieDocument.class);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return extractMovieDocumentFromResponse(response);
//    }
//
//    public List<MovieDocument> searchMoviesByReleaseYearRange(int startYear, int endYear) {
//        SearchResponse<MovieDocument> response;
//        try {
//            Supplier<Query> querySupplier = ESUtil.createRangeQuery(startYear, endYear);
//            response = elasticsearchClient.search(q -> q.index("movies")
//                    .query(querySupplier.get()), MovieDocument.class);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return extractMovieDocumentFromResponse(response);
//    }
//
//    public Set<String> autoSuggestMoviesByTitle(String title) {
//        SearchResponse<MovieDocument> response;
//        try {
//            Query autoSuggestQuery = ESUtil.buildAutoSuggestQuery(title);
//            response = elasticsearchClient.search(q -> q.index("movies")
//                    .query(autoSuggestQuery), MovieDocument.class);
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return extractMovieTitlesFromResponse(response);
//
//    }
//
//    private MovieDocument convertToMovieDocument(Movie movie) {
//        Optional<MovieDetails> movieDetails = movieDetailsRepository.findByMovie(movie);
//        List<String> actors = movieDetails.map(MovieDetails::getActors).orElse(Collections.emptyList());
//        return new MovieDocument(
//                movie.getId(),
//                movie.getTitle(),
//                movie.getDirector(),
//                movie.getReleaseYear(),
//                movie.getGenre(),
//                actors
//        );
//    }
//
//    private List<MovieDocument> extractMovieDocumentFromResponse(SearchResponse<MovieDocument> response) {
//        return response
//                .hits()
//                .hits()
//                .stream()
//                .map(Hit::source)
//                .collect(Collectors.toList());
//    }
//
//    private Set<String> extractMovieTitlesFromResponse(SearchResponse<MovieDocument> response) {
//        return response
//                .hits()
//                .hits()
//                .stream()
//                .map(Hit::source)
//                .map(MovieDocument::getTitle)
//                .collect(Collectors.toSet());
//    }
//}
