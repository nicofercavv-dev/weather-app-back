package com.academia.db.climatempo.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private List<ValidationError> errors;

    @Getter
    @Builder
    public static class ValidationError {
        private String field;
        private String message;
    }
}