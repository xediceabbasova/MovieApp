package com.company.movieapp.controller;

import com.company.movieapp.dto.MovieDto;
import com.company.movieapp.dto.UserDto;
import com.company.movieapp.dto.request.CreateUserRequest;
import com.company.movieapp.dto.request.UpdateUserRequest;
import com.company.movieapp.security.dto.AuthRequest;
import com.company.movieapp.security.service.JwtService;
import com.company.movieapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
@Validated
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Operation(
            method = "GET",
            summary = "Get all users",
            description = "Returns a list of all users.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(array = @ArraySchema(
                                    schema = @Schema(implementation = UserDto.class)))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "BAD request",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(
            method = "GET",
            summary = "Get user by email",
            description = "Returns the user information associated with the provided email address.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "BAD request",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/{mail}")
    public ResponseEntity<UserDto> getUserByMail(@PathVariable @NotBlank(message = "Mail is required") @Email(message = "Invalid email format") String mail) {
        return ResponseEntity.ok(userService.getUserByMail(mail));
    }

    @Operation(
            method = "POST",
            summary = "Create a new user",
            description = "Creates a new user with the provided information.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateUserRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class)
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
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest userRequest) {
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @Operation(
            method = "PUT",
            summary = "Update an existing user",
            description = "Updates an existing user with the provided information.",
            parameters = {
                    @Parameter(
                            name = "mail",
                            description = "The email address of the user to be updated",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UpdateUserRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "BAD request",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/{mail}")
    public ResponseEntity<UserDto> updateUser(@PathVariable @NotBlank(message = "Mail is required") @Email(message = "Invalid email format") String mail,
                                              @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(mail, request));
    }

    @Operation(
            method = "DELETE",
            summary = "Delete user",
            description = "Deletes the user with the specified email address.",
            parameters = {
                    @Parameter(
                            name = "mail",
                            description = "The email address of the user to be deleted",
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
                            description = "BAD request",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @DeleteMapping("/{mail}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotBlank(message = "Mail is required") @Email(message = "Invalid email format") String mail) {
        userService.deleteUser(mail);
        return ResponseEntity.ok().build();
    }

    @Operation(
            method = "PUT",
            summary = "Add movie to user",
            description = "Adds a movie to the user's movie list.",
            parameters = {
                    @Parameter(
                            name = "mail",
                            description = "The email address of the user to add the movie to",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string")
                    ),
                    @Parameter(
                            name = "movieId",
                            description = "The ID of the movie to be added",
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
                            description = "BAD request",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PutMapping("/{mail}/movies/{movieId}")
    public ResponseEntity<UserDto> addMovieToUser(@PathVariable @NotBlank(message = "Mail is required") @Email(message = "Invalid email format") String mail,
                                                  @PathVariable @NotBlank(message = "Movie ID cannot be blank") String movieId) {
        return ResponseEntity.ok(userService.addMovieToUser(mail, movieId));
    }

    @Operation(
            method = "GET",
            summary = "Get user's movie by ID",
            description = "Retrieves the details of a movie belonging to the user by its ID.",
            parameters = {
                    @Parameter(
                            name = "mail",
                            description = "The email address of the user",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string")
                    ),
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
                            description = "BAD request",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/{mail}/movies/{movieId}")
    public ResponseEntity<MovieDto> getUserMovieById(@PathVariable @NotBlank(message = "Mail is required") @Email(message = "Invalid email format") String mail,
                                                     @PathVariable @NotBlank(message = "Movie ID cannot be blank") String movieId) {
        return ResponseEntity.ok(userService.getUserMovieByMail(mail, movieId));
    }

    @Operation(
            method = "GET",
            summary = "Get user's movies",
            description = "Retrieves the list of movies belonging to the user.",
            parameters = {
                    @Parameter(
                            name = "mail",
                            description = "The email address of the user",
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
                                    array = @ArraySchema(schema = @Schema(implementation = MovieDto.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "BAD request",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping("/{mail}/movies")
    public ResponseEntity<List<MovieDto>> getUserMovies(@PathVariable @NotBlank(message = "Mail is required") @Email(message = "Invalid email format") String mail) {
        return ResponseEntity.ok(userService.getUserMovies(mail));
    }

    @Operation(
            method = "DELETE",
            summary = "Delete movie from user",
            description = "Deletes a movie from the user's movie list.",
            parameters = {
                    @Parameter(
                            name = "mail",
                            description = "The email address of the user",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "string")
                    ),
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
                            description = "BAD request",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @DeleteMapping("/{mail}/movies/{movieId}")
    public ResponseEntity<Void> deleteMovieFromUser(@PathVariable @NotBlank(message = "Mail is required") @Email(message = "Invalid email format") String mail,
                                                    @PathVariable @NotBlank(message = "Movie ID cannot be blank") String movieId) {
        userService.deleteMovieFromUser(mail, movieId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/generateToken")
    public String generateToken(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(request.username());
        }
        throw new UsernameNotFoundException("invalid username {} " + request.username());
    }

}
