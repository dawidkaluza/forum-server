package pl.dkaluza.forum.modules.user.base.exceptions;

import pl.dkaluza.forum.core.exceptions.entity.EntityAlreadyExistsException;

public class EmailAlreadyExistsException extends EntityAlreadyExistsException {
    private static final long serialVersionUID = -1980789797238052913L;
    private final String email;

    public EmailAlreadyExistsException(String email) {
        super("Email " + email + " already exists");
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
