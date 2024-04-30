package com.company.movieapp.security.dto;

public record AuthRequest(
        String username,
        String password
) {
}
