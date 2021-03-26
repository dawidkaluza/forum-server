package pl.dkaluza.forum.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.dkaluza.forum.core.api.exceptionsHandler.ExceptionsHandler;
import pl.dkaluza.forum.core.api.response.RequestErrorCreator;

@RestControllerAdvice
public class SecurityExceptionsHandler implements ExceptionsHandler {
    private final RequestErrorCreator requestErrorCreator;

    @Autowired
    public SecurityExceptionsHandler(RequestErrorCreator requestErrorCreator) {
        this.requestErrorCreator = requestErrorCreator;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsExceptionHandler(WebRequest request) {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.UNAUTHORIZED)
            .withTimestampAsNow()
            .withMessage(request.getLocale(), "security.authentication.badCredentials", "Bad credentials")
            .build();
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> disabledExceptionHandler(WebRequest request) {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.UNAUTHORIZED)
            .withTimestampAsNow()
            .withMessage(request.getLocale(), "security.authentication.disabled", "Account is not enabled yet")
            .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> authenticationExceptionHandler(WebRequest request) {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.UNAUTHORIZED)
            .withTimestampAsNow()
            .withMessage(request.getLocale(), "security.authentication.error", "Authentication error")
            .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedExceptionHandler(WebRequest request) {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.FORBIDDEN)
            .withTimestampAsNow()
            .withMessage(request.getLocale(), "security.accessDenied", "Access denied")
            .build();
    }
}
