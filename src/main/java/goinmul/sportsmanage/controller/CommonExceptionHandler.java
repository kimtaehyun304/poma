package goinmul.sportsmanage.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.thymeleaf.exceptions.TemplateInputException;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class CommonExceptionHandler {

    //널포인터 포함
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException() {
        return new ResponseEntity<>("에러가 발생했습니다", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> NoResourceFoundException() {
        return new ResponseEntity<>("존재하지 않는 페이지입니다", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> SQLIntegrityConstraintViolationException() {
        return new ResponseEntity<>("중복된 데이터입니다.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> MissingServletRequestParameterException() {
        return new ResponseEntity<>("에러가 발생했습니다", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TemplateInputException.class)
    public ResponseEntity<String> TemplateInputException() {
        return new ResponseEntity<>("에러가 발생했습니다", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleConflict(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
