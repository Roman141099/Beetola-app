package org.beetola.app.rest;

import lombok.extern.slf4j.Slf4j;
import org.beetola.app.exception.CurrencyException;
import org.beetola.app.model.dto.InvalidInputRs;
import org.beetola.app.model.dto.ServerError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(CurrencyException.class)
    public ResponseEntity<?> handle(CurrencyException ex) {
        log.debug("Bad input: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new InvalidInputRs().message(ex.getMessage()));
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleConstraintViolation(RuntimeException ex) {
        log.debug("Bad input: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new InvalidInputRs().message("Bad input parameters!"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAppException(Exception ex) {
        log.error("Application error : {}", ex.getMessage(), ex);
        return ResponseEntity.internalServerError().body(new ServerError().message("Oops, smth went wrong :("));
    }

}
