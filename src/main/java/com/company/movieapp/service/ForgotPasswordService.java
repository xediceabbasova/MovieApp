package com.company.movieapp.service;

import com.company.movieapp.dto.request.ChangePassword;
import com.company.movieapp.dto.request.MailBody;
import com.company.movieapp.dto.request.UpdateUserRequest;
import com.company.movieapp.exception.InvalidOtpException;
import com.company.movieapp.exception.OtpExpiredException;
import com.company.movieapp.exception.PasswordMismatchException;
import com.company.movieapp.model.ForgotPassword;
import com.company.movieapp.model.User;
import com.company.movieapp.repository.ForgotPasswordRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
public class ForgotPasswordService {

    private final ForgotPasswordRepository passwordRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public ForgotPasswordService(ForgotPasswordRepository passwordRepository, UserService userService, BCryptPasswordEncoder passwordEncoder, EmailService emailService) {
        this.passwordRepository = passwordRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void verifyEmail(String email) {
        User user = userService.findUserByMail(email);

        int otp = otpGenerator();
        MailBody mailBody = new MailBody(email, "OTP for Forgot Password request", "This is the OTP for your Forgot Password request : " + otp);
        emailService.sendMessage(mailBody);

        ForgotPassword forgotPassword = new ForgotPassword(otp, new Date(System.currentTimeMillis() + 5 * 60 * 1000), user);
        passwordRepository.save(forgotPassword);
    }

    public void verifyOtp(Integer otp, String email) {
        User user = userService.findUserByMail(email);

        ForgotPassword forgotPassword = passwordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new InvalidOtpException("Invalid OTP for email: " + email));

        if (forgotPassword.getExpirationTime().before(Date.from(Instant.now()))) {
            passwordRepository.deleteById(Objects.requireNonNull(forgotPassword.getFpid()));
            throw new OtpExpiredException("OTP has expired!");
        }
    }

    public void changePassword(ChangePassword changePassword, String email) {
        if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
            throw new PasswordMismatchException("Passwords do not match!");
        }

        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userService.updateUser(email, new UpdateUserRequest(encodedPassword));
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
