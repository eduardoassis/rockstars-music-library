package com.rockstars.musiclibrary.handler;

import com.rockstars.musiclibrary.exception.ErrorMessage;
import com.rockstars.musiclibrary.exception.MusicLibraryException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class MusicLibraryHandler {

    @ExceptionHandler(value = { MusicLibraryException.class })
    public ResponseEntity<ErrorMessage> handle(final MusicLibraryException e, final HttpServletRequest request, final HttpServletResponse response) {
        return new ResponseEntity<>(ErrorMessage.builder().message(e.getMessage()).build(), e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
            .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(ErrorMessage.builder().messages(errors).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<ErrorMessage> handleGenericExceptions(final Exception e, final HttpServletRequest request, final HttpServletResponse response) {
        log.error("Internal error: ", e);
        return new ResponseEntity<>(ErrorMessage.builder().message("Internal error. Contact the admin.").build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { DataIntegrityViolationException.class })
    public ResponseEntity<ErrorMessage> handleDataIntegrityViolationException(final DataIntegrityViolationException e, final HttpServletRequest request, final HttpServletResponse response) {
        log.error("Error while persistent on data base", e);
        return new ResponseEntity<>(ErrorMessage.builder().message("There is one or more fields that are unique, please review the request values.").build(), HttpStatus.CONFLICT);
    }
}
