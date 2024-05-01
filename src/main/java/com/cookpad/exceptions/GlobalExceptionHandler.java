package com.cookpad.exceptions;

import com.cookpad.responses.ErrorDetailsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // handle resource specific exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetailsResponse> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                                WebRequest webRequest) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(LocalDateTime.now(), exception.getMessage(),
                webRequest.getDescription(false));

        log.info("ErrorDetailsResponse: " + errorDetails);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // handle Recipe API exceptions
    @ExceptionHandler(RecipeAPIException.class)
    public ResponseEntity<ErrorDetailsResponse> handleRecipeAPIException(RecipeAPIException exception,
                                                                       WebRequest webRequest) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(LocalDateTime.now(), exception.getMessage(),
                webRequest.getDescription(false));

        log.info("ErrorDetailsResponse: " + errorDetails);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // handle Unauthorized exceptions
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetailsResponse> handleUnAuthorized(AccessDeniedException  exception,
                                                                   WebRequest webRequest) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(LocalDateTime.now(), exception.getMessage(),
                webRequest.getDescription(false));

        log.info("ErrorDetailsResponse: " + errorDetails);
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    // handle global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailsResponse> handleGlobalException(Exception exception,
                                                                      WebRequest webRequest) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(LocalDateTime.now(), exception.getMessage(),
                webRequest.getDescription(false));

        log.info("ErrorDetailsResponse: " + errorDetails);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // handle invalid data which does not meet the rules it expects
    // Eg) @NotNull, @Size, @Email
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach((error) -> {
                    // import org.springframework.validation.FieldError
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    errors.put(fieldName, message);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
