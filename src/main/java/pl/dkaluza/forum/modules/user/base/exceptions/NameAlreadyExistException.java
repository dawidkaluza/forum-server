package pl.dkaluza.forum.modules.user.base.exceptions;

import pl.dkaluza.forum.core.exceptions.entity.EntityAlreadyExistsException;

public class NameAlreadyExistException extends EntityAlreadyExistsException {
    private static final long serialVersionUID = 1105974699151403021L;

    public NameAlreadyExistException(String name) {
        super("Name " + name + " already exists");
    }
}
