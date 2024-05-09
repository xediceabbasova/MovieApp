package com.company.movieapp.dto.request;

public record MailBody(
        String to,
        String subject,
        String text
) {
}
