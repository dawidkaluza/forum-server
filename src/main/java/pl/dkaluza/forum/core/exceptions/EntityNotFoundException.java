package pl.dkaluza.forum.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

public class EntityNotFoundException extends ApiRequestException {
    public EntityNotFoundException(Class<?> entityClass, Serializable id) {
        super(HttpStatus.NOT_FOUND, "Cant find entity " + entityClass.getSimpleName() + " with id " + id);
    }
}
