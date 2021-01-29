package pl.dkaluza.forum.core.exceptions.entity;

import java.io.Serializable;

public class EntityNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -3025566621391895011L;

    public EntityNotFoundException(Class<?> entityClass, Serializable id) {
        super("Cant find entity " + entityClass.getCanonicalName() + " with id " + id);
    }

    public EntityNotFoundException(String reason) {
        super(reason);
    }

    public EntityNotFoundException(String reason, Throwable throwable) {
        super(reason, throwable);
    }
}
