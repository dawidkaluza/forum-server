package pl.dkaluza.forum.modules.user.base.exceptions;

public class InvalidPasswordException extends RuntimeException {
    private static final long serialVersionUID = -1542907228548756522L;

    public InvalidPasswordException() {
        super("Password must have between 3 and 32 signs");
    }
}
