package pl.dkaluza.forum.core.exceptions;

import org.springframework.http.HttpStatus;
import pl.dkaluza.forum.core.api.ApiErrorResponse;

/**
 * Abstract class used to be extended by exceptions
 * that should send error to client because error is caused because of him.
 */
public abstract class ApiRequestException extends Exception {
    private final HttpStatus status;

    public ApiRequestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ApiErrorResponse toErrorResponse() {
        return new ApiErrorResponse(status.value(), getMessage());
    }
}
