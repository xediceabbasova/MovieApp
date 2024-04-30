package com.company.movieapp;

import com.company.movieapp.model.Role;
import com.company.movieapp.model.User;
import com.company.movieapp.repository.UserRepository;
import com.company.movieapp.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MovieappApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public MovieappApplication(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(MovieappApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        User user = new User("Natiq123@gmail.com", encoder.encode("Nx0605!"));
//        user.addRole(Role.ROLE_ADMIN);
//        userRepository.save(user);
    }
}
