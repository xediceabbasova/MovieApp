//package com.company.movieapp.elasticsearch.controller;
//
//import com.company.movieapp.elasticsearch.dto.SearchRequestDto;
//import com.company.movieapp.elasticsearch.model.MovieDocument;
//import com.company.movieapp.elasticsearch.service.MovieElasticService;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Set;
//
//@RestController
//@RequestMapping("/api/movies")
//public class MovieESController {
//
//    private final MovieElasticService movieElasticService;
//
//    public MovieESController(MovieElasticService movieElasticService) {
//        this.movieElasticService = movieElasticService;
//    }
//
//    @PostMapping("/sync")
//    public void syncMovies() {
//        movieElasticService.syncMovies();
//    }
//
//    @GetMapping("/allIndexes")
//    public List<MovieDocument> getAllMoviesFromAllIndexes() {
//        return movieElasticService.getAllMoviesFromAllIndexes();
//    }
//
//    @GetMapping("/search")
//    public List<MovieDocument> searchMoviesByFieldAndValue(@RequestBody SearchRequestDto requestDto) {
//        return movieElasticService.searchMoviesByFieldAndValue(requestDto);
//    }
//
//    @GetMapping("/boolQuery")
//    public List<MovieDocument> boolQuery(@RequestBody SearchRequestDto requestDto) {
//        return movieElasticService.boolQueryFieldAndValue(requestDto);
//    }
//
//    @GetMapping("/search/{startYear}/{endYear}")
//    public List<MovieDocument> searchMoviesByYearRange(@PathVariable int startYear, @PathVariable int endYear) {
//        return movieElasticService.searchMoviesByReleaseYearRange(startYear, endYear);
//    }
//
//    @GetMapping("/autoSuggest/{title}")
//    public Set<String> autoSuggestMoviesByTitle(@PathVariable String title) {
//        return movieElasticService.autoSuggestMoviesByTitle(title);
//    }
//}
