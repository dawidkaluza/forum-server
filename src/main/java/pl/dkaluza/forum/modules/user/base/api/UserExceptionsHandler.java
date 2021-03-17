package pl.dkaluza.forum.modules.user.base.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.dkaluza.forum.core.api.exceptionsHandler.ExceptionsHandler;
import pl.dkaluza.forum.core.api.response.LocaleFieldErrorMapper;
import pl.dkaluza.forum.core.api.response.ResponseError;
import pl.dkaluza.forum.core.api.response.ResponseFieldsError;
import pl.dkaluza.forum.modules.user.base.exceptions.EmailAlreadyExistsException;
import pl.dkaluza.forum.modules.user.base.exceptions.NameAlreadyExistsException;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;

@RestControllerAdvice
public class UserExceptionsHandler implements ExceptionsHandler {
    private final MessageSource messageSource;
    private final LocaleFieldErrorMapper localeFieldErrorMapper;

    @Autowired
    public UserExceptionsHandler(MessageSource messageSource, LocaleFieldErrorMapper localeFieldErrorMapper) {
        this.messageSource = messageSource;
        this.localeFieldErrorMapper = localeFieldErrorMapper;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundExceptionHandler() {
        return new ResponseError(HttpStatus.NOT_FOUND, "User not found")
            .toResponseEntity();
    }

    @ExceptionHandler(NameAlreadyExistsException.class)
    public ResponseEntity<?> nameAlreadyExistsExceptionHandler(WebRequest request) {
        return new ResponseFieldsError(HttpStatus.CONFLICT, messageSource.getMessage("invalidParams", null, request.getLocale()))
            .add(localeFieldErrorMapper.map(
                "name", "user.register.nameAlreadyExists", request.getLocale()
            )).toResponseEntity();
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> emailAlreadyExistsExceptionHandler(WebRequest request) {
        return new ResponseFieldsError(HttpStatus.CONFLICT, messageSource.getMessage("invalidParams", null, request.getLocale()))
            .add(localeFieldErrorMapper.map(
                "email", "user.register.emailAlreadyExists", request.getLocale()
            )).toResponseEntity();
    }
}
