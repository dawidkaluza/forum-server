package pl.dkaluza.forum.modules.user.base.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.dkaluza.forum.core.api.exceptionsHandler.ExceptionsHandler;
import pl.dkaluza.forum.core.api.response.RequestErrorCreator;
import pl.dkaluza.forum.modules.user.base.exceptions.EmailAlreadyExistsException;
import pl.dkaluza.forum.modules.user.base.exceptions.NameAlreadyExistsException;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;

@RestControllerAdvice
public class UserExceptionsHandler implements ExceptionsHandler {
    private final RequestErrorCreator requestErrorCreator;

    @Autowired
    public UserExceptionsHandler(RequestErrorCreator requestErrorCreator) {
        this.requestErrorCreator = requestErrorCreator;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundExceptionHandler(WebRequest request) {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.NOT_FOUND)
            .withMessage(request.getLocale(), "user.notFound", "User not found")
            .withTimestampAsNow()
            .build();
    }

    @ExceptionHandler(NameAlreadyExistsException.class)
    public ResponseEntity<?> nameAlreadyExistsExceptionHandler(WebRequest request) {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.CONFLICT)
            .withMessage(request.getLocale(), "user.nameAlreadyExists", "Name already exists")
            .withTimestampAsNow()
            .build();
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> emailAlreadyExistsExceptionHandler(WebRequest request) {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.CONFLICT)
            .withMessage(request.getLocale(), "user.emailAlreadyExists", "Email already exists")
            .withTimestampAsNow()
            .build();
    }
}
