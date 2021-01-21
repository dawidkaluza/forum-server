package pl.dkaluza.forum.modules.user.confirmRegistration.exceptions;

public class InvalidTokenException extends RuntimeException {
    private static final long serialVersionUID = 8231263683686380267L;

    public InvalidTokenException() {
        super("Invalid token");
    }
}
