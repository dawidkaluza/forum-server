package pl.dkaluza.forum.modules.user.confirmRegistration.exceptions;

public class TooManyTokenResendsException extends RuntimeException {
    private static final long serialVersionUID = 591835183423061686L;

    public TooManyTokenResendsException(String message) {
        super(message);
    }

    public TooManyTokenResendsException(String message, Throwable cause) {
        super(message, cause);
    }
}
