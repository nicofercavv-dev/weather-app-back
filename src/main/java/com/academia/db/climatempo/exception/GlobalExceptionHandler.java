package com.academia.db.climatempo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<ErrorResponse.ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> ErrorResponse.ValidationError.builder()
                        .field(err.getField())
                        .message(err.getDefaultMessage())
                        .build())
                .toList();

        return buildResponse(HttpStatus.BAD_REQUEST, "Erro de validação, há campos inválidos", errors);
    }

    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(org.springframework.web.bind.MissingServletRequestParameterException ex) {
        String msg = String.format("O parâmetro obrigatório '%s' não foi enviado.", ex.getParameterName());
        return buildResponse(HttpStatus.BAD_REQUEST, msg, null);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), null);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(org.springframework.web.servlet.NoHandlerFoundException ex) {
        String msg = String.format("A rota %s não foi encontrada", ex.getRequestURL());
        return buildResponse(HttpStatus.NOT_FOUND, msg, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ex.printStackTrace();

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno inesperado no servidor. Tente novamente mais tarde.",
                null
        );
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String msg, List<ErrorResponse.ValidationError> errs) {
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .message(msg)
                .timestamp(LocalDateTime.now())
                .errors(errs)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
