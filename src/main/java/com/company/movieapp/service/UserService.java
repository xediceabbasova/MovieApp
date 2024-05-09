package com.company.movieapp.service;

import com.company.movieapp.dto.MovieDto;
import com.company.movieapp.dto.UserDto;
import com.company.movieapp.dto.converter.MovieDtoConverter;
import com.company.movieapp.dto.converter.UserDtoConverter;
import com.company.movieapp.dto.request.CreateUserRequest;
import com.company.movieapp.dto.request.UpdateUserRequest;
import com.company.movieapp.exception.MovieNotFoundException;
import com.company.movieapp.exception.UserNotFoundException;
import com.company.movieapp.model.Movie;
import com.company.movieapp.model.Role;
import com.company.movieapp.model.User;
import com.company.movieapp.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoConverter userDtoConverter;
    private final MovieDtoConverter movieDtoConverter;
    private final MovieService movieService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserDtoConverter userDtoConverter, MovieDtoConverter movieDtoConverter, MovieService movieService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDtoConverter = userDtoConverter;
        this.movieDtoConverter = movieDtoConverter;
        this.movieService = movieService;
        this.passwordEncoder = passwordEncoder;
    }

    @Cacheable(value = "users", key = "#mail")
    public UserDto getUserByMail(final String mail) {
        return userDtoConverter.convert(findUserByMail(mail));
    }

    @Cacheable(value = "users")
    public List<UserDto> getAllUsers() {
        return userDtoConverter.convert(userRepository.findAll());
    }

    @CachePut(value = "users", key = "#request.mail")
    public UserDto createUser(final CreateUserRequest request) {
        User user = new User(request.mail(), passwordEncoder.encode(request.password()));
        user.addRole(Role.ROLE_USER);
        return userDtoConverter.convert(userRepository.save(user));
    }

    @CacheEvict(value = "users", allEntries = true)
    public UserDto updateUser(final String mail, final UpdateUserRequest updateUserRequest) {
        User user = findUserByMail(mail);
        User updatedUser = new User(
                user.getId(),
                user.getMail(),
                passwordEncoder.encode(updateUserRequest.password()),
                user.getMovies()
        );
        return userDtoConverter.convert(userRepository.save(updatedUser));
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteUser(final String mail) {
        userRepository.delete(findUserByMail(mail));
    }

    @CacheEvict(value = "users", allEntries = true)
    public UserDto addMovieToUser(final String mail, final String movieId) {
        User user = findUserByMail(mail);
        Movie movie = movieService.findMovieById(movieId);
        if (user.getMovies().contains(movie)) {
            throw new RuntimeException("The movie is already in the user's list.");
        }
        user.getMovies().add(movie);
        return userDtoConverter.convert(userRepository.save(user));
    }

    public MovieDto getUserMovieByMail(final String mail, final String movieId) {
        User user = findUserByMail(mail);
        return movieDtoConverter.convert(findUserMovieByMail(user, movieId));
    }

    public List<MovieDto> getUserMovies(final String mail) {
        User user = findUserByMail(mail);
        return user.getMovies()
                .stream()
                .map(movieDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteMovieFromUser(final String mail, final String movieId) {
        User user = findUserByMail(mail);
        Movie movie = findUserMovieByMail(user, movieId);
        user.getMovies().remove(movie);
        userRepository.save(user);

    }

    protected User findUserByMail(final String mail) {
        return userRepository.findByMail(mail)
                .orElseThrow(() -> new UserNotFoundException("User couldn't be found by following mail:" + mail));
    }

    private Movie findUserMovieByMail(final User user, String movieId) {
        return user.getMovies().stream()
                .filter(m -> Objects.equals(m.getId(), movieId))
                .findFirst()
                .orElseThrow(() -> new MovieNotFoundException("Movie not found with id: " + movieId));

    }

}
