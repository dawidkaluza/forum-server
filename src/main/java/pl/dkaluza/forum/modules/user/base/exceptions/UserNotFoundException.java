package pl.dkaluza.forum.modules.user.base.exceptions;

public class UserNotFoundException extends UserException {
    private static final long serialVersionUID = -8530426275711530505L;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public static UserNotFoundException of(Long id) {
        return new UserNotFoundException("Can't find user with id=" + id);
    }
}
