package pl.dkaluza.forum.modules.user.confirmRegistration.exceptions;

public class InvalidTokenException extends ConfirmRegistrationException {
    private static final long serialVersionUID = 8231263683686380267L;

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
