package pmf.awp.project.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEntryException extends CustomException {

    public DuplicateEntryException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
