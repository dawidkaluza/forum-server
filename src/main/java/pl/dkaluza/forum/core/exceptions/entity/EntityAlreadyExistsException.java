package pl.dkaluza.forum.core.exceptions.entity;

import org.springframework.http.HttpStatus;


public class EntityAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 2434799681264521197L;

    public EntityAlreadyExistsException(String reason) {
        super(reason);
    }
}
