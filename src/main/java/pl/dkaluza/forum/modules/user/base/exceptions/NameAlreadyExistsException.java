package pl.dkaluza.forum.modules.user.base.exceptions;

public class NameAlreadyExistsException extends UserException {
    private static final long serialVersionUID = 1105974699151403021L;

    public NameAlreadyExistsException(String message) {
        super(message);
    }

    public NameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public static NameAlreadyExistsException of(String name) {
        return new NameAlreadyExistsException("Can't find user with name=" + name);
    }
}
