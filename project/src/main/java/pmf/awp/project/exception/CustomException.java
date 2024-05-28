package pmf.awp.project.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {
    private HttpStatus status;

    public HttpStatus getStatus() {
        return status;
    }

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
