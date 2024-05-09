package com.company.movieapp.dto.request;

public record ChangePassword(
        String password,
        String repeatPassword
) {
}
