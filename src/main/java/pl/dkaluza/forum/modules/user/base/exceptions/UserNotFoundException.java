package pl.dkaluza.forum.modules.user.base.exceptions;

import pl.dkaluza.forum.core.exceptions.entity.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    private static final long serialVersionUID = -8530426275711530505L;

    public UserNotFoundException(Long id) {
        super("Can't find user with id=" + id, id);
    }
}
