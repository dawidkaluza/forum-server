package pl.dkaluza.forum.modules.user.confirmRegistration.exceptions;

public class TokenExpiredException extends ConfirmRegistrationException {
    private static final long serialVersionUID = -7885452732556454779L;

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
