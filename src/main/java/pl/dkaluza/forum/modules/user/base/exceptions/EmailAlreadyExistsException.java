package pl.dkaluza.forum.modules.user.base.exceptions;

public class EmailAlreadyExistsException extends UserException {
    private static final long serialVersionUID = -1980789797238052913L;

    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public static EmailAlreadyExistsException of(String email) {
        return new EmailAlreadyExistsException("Can't find user with email=" + email);
    }
}
