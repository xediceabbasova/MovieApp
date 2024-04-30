package com.company.movieapp.controller;

import com.company.movieapp.dto.MovieDetailsDto;
import com.company.movieapp.dto.request.CreateMovieDetailsRequest;
import com.company.movieapp.dto.request.UpdateMovieDetailsRequest;
import com.company.movieapp.service.MovieDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/movies/details")
public class MovieDetailsController {

    private final MovieDetailsService movieDetailsService;

    public MovieDetailsController(MovieDetailsService movieDetailsService) {
        this.movieDetailsService = movieDetailsService;
    }

    @Operation(
            method = "POST",
            summary = "Create movie details",
            description = "Creates details for a movie.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateMovieDetailsRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Movie details created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MovieDetailsDto.class)
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
    public ResponseEntity<MovieDetailsDto> createMovieDetails(@Valid @RequestBody CreateMovieDetailsRequest request) {
        return ResponseEntity.ok(movieDetailsService.createMovieDetails(request));
    }

    @Operation(
            method = "GET",
            summary = "Get movie details by ID",
            description = "Retrieves the details of a movie by its ID.",
            parameters = {
                    @Parameter(
                            name = "movieId",
                            description = "The ID of the movie",
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
                                    schema = @Schema(implementation = MovieDetailsDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Movie details not found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDetailsDto> getMovieDetailsById(@PathVariable @NotBlank(message = "Movie ID cannot be blank") String movieId) {
        return ResponseEntity.ok(movieDetailsService.getMovieDetailsByMovieId(movieId));
    }

    @Operation(
            method = "PUT",
            summary = "Update movie details",
            description = "Updates the details of a movie with the provided information.",
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
                            schema = @Schema(implementation = UpdateMovieDetailsRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Movie details updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = MovieDetailsDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Movie details not found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/{movieId}")
    public ResponseEntity<MovieDetailsDto> updateMovieDetails(@PathVariable @NotBlank(message = "Movie ID cannot be blank") String movieId,
                                                              @RequestBody UpdateMovieDetailsRequest request) {
        return ResponseEntity.ok(movieDetailsService.updateMovieDetails(movieId, request));
    }

    @Operation(
            method = "DELETE",
            summary = "Delete movie details",
            description = "Deletes the details of a movie by its ID.",
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
                            description = "Movie details not found",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovieDetails(@PathVariable @NotBlank(message = "Movie ID cannot be blank") String movieId) {
        movieDetailsService.deleteMovieDetails(movieId);
        return ResponseEntity.ok().build();
    }

}
