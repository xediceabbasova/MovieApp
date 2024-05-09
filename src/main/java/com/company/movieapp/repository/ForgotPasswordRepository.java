package com.company.movieapp.repository;

import com.company.movieapp.model.ForgotPassword;
import com.company.movieapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {

    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);
}
