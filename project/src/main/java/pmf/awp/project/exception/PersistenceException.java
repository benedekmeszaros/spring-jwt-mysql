package pmf.awp.project.exception;

import org.springframework.http.HttpStatus;

public class PersistenceException extends CustomException {

    public PersistenceException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
