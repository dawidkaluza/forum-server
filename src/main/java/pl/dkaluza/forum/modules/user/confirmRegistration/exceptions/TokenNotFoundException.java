package pl.dkaluza.forum.modules.user.confirmRegistration.exceptions;

import pl.dkaluza.forum.core.exceptions.entity.EntityNotFoundException;

public class TokenNotFoundException extends EntityNotFoundException {
    private static final long serialVersionUID = 2795683501236076484L;

    public TokenNotFoundException(Long id) {
        super("Can't find token with id=" + id, id);
    }
}
