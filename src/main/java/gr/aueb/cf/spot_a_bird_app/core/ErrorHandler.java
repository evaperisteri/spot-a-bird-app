package gr.aueb.cf.spot_a_bird_app.core;

import gr.aueb.cf.spot_a_bird_app.core.exceptions.*;
import gr.aueb.cf.spot_a_bird_app.dto.ResponseMessageDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String , String>> handleValidationException(ValidationException e) {
        BindingResult bindingResult = e.getBindingResult();

        Map<String , String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AppObjectNotFoundException.class})
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppObjectNotFoundException e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AppObjectAlreadyExists.class})
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppObjectAlreadyExists e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({AppObjectInvalidArgumentException.class})
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppObjectInvalidArgumentException e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AppObjectNotAuthorizedException.class})
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppObjectNotAuthorizedException e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AppServerException.class})
    public ResponseEntity<ResponseMessageDTO> handleConstraintViolationException(AppServerException e) {
        return new ResponseEntity<>(new ResponseMessageDTO(e.getCode(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessageDTO> handleAllExceptions(Exception e) {
        logger.error("Server error occurred", e);
        return new ResponseEntity<>(
                new ResponseMessageDTO("SERVER_ERROR", "An unexpected error occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({DataAccessException.class, DataAccessResourceFailureException.class})
    public ResponseEntity<ResponseMessageDTO> handleDataAccessException(DataAccessException ex) {
        logger.error("Database access error", ex);

        String rootCause = ex.getMostSpecificCause().getMessage();
        return new ResponseEntity<>(
                new ResponseMessageDTO(
                        "DATABASE_ERROR",
                        "Database operation failed: " + rootCause
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({EmptyResultDataAccessException.class, EntityNotFoundException.class})
    public ResponseEntity<ResponseMessageDTO> handleDataNotFoundExceptions(RuntimeException ex) {
        return new ResponseEntity<>(
                new ResponseMessageDTO(
                        "DATA_NOT_FOUND",
                        "Requested resource doesn't exist"
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseMessageDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String violation = ex.getMostSpecificCause().getMessage();
        return new ResponseEntity<>(
                new ResponseMessageDTO(
                        "DATA_CONFLICT",
                        "Data integrity violation: " + violation
                ),
                HttpStatus.CONFLICT
        );
    }
}
