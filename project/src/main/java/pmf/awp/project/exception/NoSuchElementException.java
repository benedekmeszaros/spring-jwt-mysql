package pmf.awp.project.exception;

import org.springframework.http.HttpStatus;

public class NoSuchElementException extends CustomException {

    public NoSuchElementException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
