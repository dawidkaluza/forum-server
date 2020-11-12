package pl.dkaluza.forum.modules.user.exceptions;

public class EmailAlreadyExistException extends Exception {
    public EmailAlreadyExistException(String email) {
        super(
            String.format("Email %s already exists in database", email)
        );
    }
}
