package pmf.awp.project.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenOperationException extends CustomException {

    public ForbiddenOperationException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }

}
