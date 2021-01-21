package pl.dkaluza.forum.modules.user.base.exceptions;

import pl.dkaluza.forum.core.exceptions.entity.EntityAlreadyExistsException;

public class EmailAlreadyExistException extends EntityAlreadyExistsException {
    private static final long serialVersionUID = -1980789797238052913L;

    public EmailAlreadyExistException(String email) {
        super("Email " + email + " already exists");
    }
}
