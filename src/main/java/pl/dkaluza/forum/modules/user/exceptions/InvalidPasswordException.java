package pl.dkaluza.forum.modules.user.exceptions;

import org.springframework.http.HttpStatus;
import pl.dkaluza.forum.core.exceptions.ApiRequestException;

public class InvalidPasswordException extends ApiRequestException {
    public InvalidPasswordException() {
        super(HttpStatus.CONFLICT, "Password must have between 3 and 32 chars");
    }
}
