package pl.dkaluza.forum.modules.user.exceptions;

import org.springframework.http.HttpStatus;
import pl.dkaluza.forum.core.exceptions.ApiRequestException;

public class NameAlreadyExistException extends ApiRequestException {
    public NameAlreadyExistException(String name) {
        super(HttpStatus.CONFLICT, "Name " + name + " already exists");
    }
}
