package pl.dkaluza.forum.modules.user.confirmRegistration.exceptions;

public class TokenNotFoundException extends ConfirmRegistrationException {
    private static final long serialVersionUID = 2795683501236076484L;

    public TokenNotFoundException(Long id) {
        super("Can't find token with id=" + id);
    }

    public TokenNotFoundException(String message) {
        super(message);
    }

    public TokenNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public static TokenNotFoundException of(Long id) {
        return new TokenNotFoundException("Can't find token with id=" + id);
    }
}
