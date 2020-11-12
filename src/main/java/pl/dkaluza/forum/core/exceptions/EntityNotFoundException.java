package pl.dkaluza.forum.core.exceptions;

import java.io.Serializable;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(Class<?> entityClass, Serializable id) {
        super(
            String.format("Cant find entity %s with id=%s", entityClass, id)
        );
    }
}
