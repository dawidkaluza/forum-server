package pl.dkaluza.forum.modules.user.confirmRegistration.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.dkaluza.forum.core.api.exceptionsHandler.ExceptionsHandler;

@RestControllerAdvice
public class ConfirmRegistrationExceptionsHandler implements ExceptionsHandler {
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> invalidTokenExceptionHandler(InvalidTokenException e) {
        return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> tokenExpiredExceptionHandler(TokenExpiredException e) {
        return new ResponseEntity<>("Token is expired", HttpStatus.GONE);
    }
}
