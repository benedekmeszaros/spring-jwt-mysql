package pmf.awp.project.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pmf.awp.project.exception.CustomException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> exceptionHandler(CustomException exc) {
        return new ResponseEntity<String>(exc.getMessage(), exc.getStatus());
    }
}
