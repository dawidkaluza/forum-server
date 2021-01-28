package pl.dkaluza.forum.modules.user.base.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.dkaluza.forum.core.api.exceptionsHandler.ExceptionsHandler;
import pl.dkaluza.forum.modules.user.base.exceptions.EmailAlreadyExistsException;
import pl.dkaluza.forum.modules.user.base.exceptions.InvalidPasswordException;
import pl.dkaluza.forum.modules.user.base.exceptions.NameAlreadyExistsException;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;

@RestControllerAdvice
public class UserExceptionsHandler implements ExceptionsHandler {
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<?> invalidPasswordExceptionHandler(InvalidPasswordException e) {
        return new ResponseEntity<>("Password must have between 3 and 32 signs", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundExceptionHandler(UserNotFoundException e) {
        return new ResponseEntity<>("Cant find user with id=" + e.getId(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NameAlreadyExistsException.class)
    public ResponseEntity<?> nameAlreadyExistsExceptionHandler(NameAlreadyExistsException e) {
        return new ResponseEntity<>("User with name=" + e.getName() + " already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> emailAlreadyExistsExceptionHandler(EmailAlreadyExistsException e) {
        return new ResponseEntity<>("User with email=" + e.getEmail() + " already exists", HttpStatus.CONFLICT);
    }
}
