package pl.dkaluza.forum.modules.user.base.exceptions;

public abstract class UserException extends RuntimeException {
    private static final long serialVersionUID = 3655916634221919120L;

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
