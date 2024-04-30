package com.company.movieapp.controller;

import com.company.movieapp.dto.MovieDto;
import com.company.movieapp.dto.request.MovieRequest;
import com.company.movieapp.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Operation(
            method = "GET",
            summary = "Get all movies",
            description = "Returns a list of all movies.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(array = @ArraySchema(
                                    schema = @Schema(implementation = MovieDto.class)))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No movies found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @Operation(
            method = "GET",
            summary = "Get movie by ID",
            description = "Retrieves the details of a movie by its ID.",
            parameters = {
                    @Parameter(
                            name = "movieId",
                            description = "The ID of the movie to be retrieved",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MovieDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Movie not found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable @NotBlank(message = "Movie ID cannot be blank") String movieId) {
        return ResponseEntity.ok(movieService.getMovieById(movieId));
    }

    @Operation(
            method = "POST",
            summary = "Create a new movie",
            description = "Creates a new movie with the provided information.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MovieRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Movie created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MovieDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@Valid @RequestBody MovieRequest movieRequest) {
        return ResponseEntity.ok(movieService.createMovie(movieRequest));
    }

    @Operation(
            method = "PUT",
            summary = "Update an existing movie",
            description = "Updates an existing movie with the provided information.",
            parameters = {
                    @Parameter(
                            name = "movieId",
                            description = "The ID of the movie to be updated",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MovieRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Movie updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MovieDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Movie not found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/{movieId}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable @NotBlank(message = "Movie ID cannot be blank") String movieId,
                                                @Valid @RequestBody MovieRequest request) {
        return ResponseEntity.ok(movieService.updateMovie(movieId, request));
    }

    @Operation(
            method = "DELETE",
            summary = "Delete movie",
            description = "Deletes the movie with the specified ID.",
            parameters = {
                    @Parameter(
                            name = "movieId",
                            description = "The ID of the movie to be deleted",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Movie not found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable @NotBlank(message = "Movie ID cannot be blank") String movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.ok().build();
    }

}
