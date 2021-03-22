package pl.dkaluza.forum.modules.user.confirmRegistration.exceptions;

import pl.dkaluza.forum.modules.user.base.exceptions.UserException;

public abstract class ConfirmRegistrationException extends UserException {
    private static final long serialVersionUID = 4688783079407675811L;

    public ConfirmRegistrationException(String message) {
        super(message);
    }

    public ConfirmRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
