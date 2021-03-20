package pl.dkaluza.forum.modules.user.confirmRegistration.api;

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
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.InvalidTokenException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TokenExpiredException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TokenNotFoundException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TooManyTokenResendsException;

@RestControllerAdvice(assignableTypes = ConfirmRegistrationController.class)
public class ConfirmRegistrationExceptionsHandler implements ExceptionsHandler {
    private final MessageSource messageSource;
    private final LocaleFieldErrorMapper localeFieldErrorMapper;

    @Autowired
    public ConfirmRegistrationExceptionsHandler(MessageSource messageSource, LocaleFieldErrorMapper localeFieldErrorMapper) {
        this.messageSource = messageSource;
        this.localeFieldErrorMapper = localeFieldErrorMapper;
    }

    @ExceptionHandler({TokenNotFoundException.class, InvalidTokenException.class})
    public ResponseEntity<?> invalidTokenExceptionHandler(WebRequest request) {
        return new ResponseError(
            HttpStatus.UNPROCESSABLE_ENTITY,
            messageSource.getMessage("user.confirmRegistration.invalidToken", null, "Invalid token", request.getLocale())
        ).toResponseEntity();
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> tokenExpiredExceptionHandler(WebRequest request) {
        return new ResponseError(
            HttpStatus.GONE,
            messageSource.getMessage("user.confirmRegistration.tokenExpired", null, "Token expired", request.getLocale())
        ).toResponseEntity();
    }

    @ExceptionHandler(TooManyTokenResendsException.class)
    public ResponseEntity<?> tooManyTokenResendsExceptionHandler(WebRequest request) {
        return new ResponseError(
            HttpStatus.LOCKED,
            messageSource.getMessage("user.confirmRegistration.tooManyResends", null, "Too many email resends", request.getLocale())
        ).toResponseEntity();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundExceptionHandler(WebRequest request) {
        return new ResponseFieldsError(HttpStatus.CONFLICT, messageSource.getMessage("invalidFields", null, "Invalid fields", request.getLocale()))
            .add(localeFieldErrorMapper.map(
                "email", "user.notFound", request.getLocale()
            )).toResponseEntity();
    }
}
