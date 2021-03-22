package pl.dkaluza.forum.modules.user.confirmRegistration.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.dkaluza.forum.core.api.exceptionsHandler.ExceptionsHandler;
import pl.dkaluza.forum.core.api.response.RequestErrorCreator;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.InvalidTokenException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TokenExpiredException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TokenNotFoundException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TooManyTokenResendsException;

@RestControllerAdvice
public class ConfirmRegistrationExceptionsHandler implements ExceptionsHandler {
    private final RequestErrorCreator requestErrorCreator;

    @Autowired
    public ConfirmRegistrationExceptionsHandler(RequestErrorCreator requestErrorCreator) {
        this.requestErrorCreator = requestErrorCreator;
    }

    @ExceptionHandler({TokenNotFoundException.class, InvalidTokenException.class})
    public ResponseEntity<?> invalidTokenExceptionHandler(WebRequest request) {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.NOT_FOUND)
            .withMessage(request.getLocale(), "user.confirmRegistration.tokenNotFound", "Token not found")
            .withTimestampAsNow()
            .build();
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> tokenExpiredExceptionHandler(WebRequest request) {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.GONE)
            .withMessage(request.getLocale(), "user.confirmRegistration.tokenExpired", "Token expired")
            .withTimestampAsNow()
            .build();
    }

    @ExceptionHandler(TooManyTokenResendsException.class)
    public ResponseEntity<?> tooManyTokenResendsExceptionHandler(WebRequest request) {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.LOCKED)
            .withMessage(request.getLocale(), "user.confirmRegistration.tooManyTries", "Too many tries")
            .withTimestampAsNow()
            .build();
    }
}
