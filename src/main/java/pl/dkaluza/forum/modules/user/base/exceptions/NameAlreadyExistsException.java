package pl.dkaluza.forum.modules.user.base.exceptions;

import pl.dkaluza.forum.core.exceptions.entity.EntityAlreadyExistsException;

public class NameAlreadyExistsException extends EntityAlreadyExistsException {
    private static final long serialVersionUID = 1105974699151403021L;
    private final String name;

    public NameAlreadyExistsException(String name) {
        super("Name " + name + " already exists");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
