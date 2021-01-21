package pl.dkaluza.forum.modules.user.confirmRegistration.exceptions;

public class TokenExpiredException extends RuntimeException {
    private static final long serialVersionUID = -7885452732556454779L;

    public TokenExpiredException() {
        super("Confirmation token expired");
    }
}
