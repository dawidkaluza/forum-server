package pl.dkaluza.forum.core.exceptions.entity;

import java.io.Serializable;

public class EntityNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -3025566621391895011L;
    private final Serializable id;

    public EntityNotFoundException(Class<?> entityClass, Serializable id) {
        super("Cant find entity " + entityClass.getCanonicalName() + " with id " + id);
        this.id = id;
    }

    public EntityNotFoundException(String reason, Serializable id) {
        super(reason);
        this.id = id;
    }

    public EntityNotFoundException(String reason, Serializable id, Throwable throwable) {
        super(reason, throwable);
        this.id = id;
    }

    public Serializable getId() {
        return id;
    }
}
