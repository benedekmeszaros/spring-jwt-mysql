package pmf.awp.project.exception;

import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends CustomException {

    public ExpiredTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
