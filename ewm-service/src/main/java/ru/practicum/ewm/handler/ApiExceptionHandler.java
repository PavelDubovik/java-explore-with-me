package ru.practicum.ewm.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice("ru.practicum.ewm")
public class ApiExceptionHandler {

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Incorrectly made request.")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.warn("Exception: {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.CONFLICT.name())
                .reason("Integrity constraint has been violated.")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        log.warn("Exception: {}", ex.getMessage());
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.NOT_FOUND.name())
                .reason("The required object was not found.")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
}
