package pl.dkaluza.forum.modules.user.exceptions;

import org.springframework.http.HttpStatus;
import pl.dkaluza.forum.core.exceptions.ApiRequestException;

public class EmailAlreadyExistException extends ApiRequestException {
    public EmailAlreadyExistException(String email) {
        super(HttpStatus.CONFLICT, "Email " + email + " already exists");
    }
}
