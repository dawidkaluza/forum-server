package pl.dkaluza.forum.core.api.exceptionsHandler.entity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.dkaluza.forum.core.api.exceptionsHandler.ExceptionsHandler;
import pl.dkaluza.forum.core.api.exceptionsHandler.ExceptionsHandlerOrder;
import pl.dkaluza.forum.core.exceptions.entity.EntityAlreadyExistsException;
import pl.dkaluza.forum.core.exceptions.entity.EntityNotFoundException;

@RestControllerAdvice
public class EntityExceptionsHandler implements ExceptionsHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<?> entityAlreadyExistsException(EntityAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @Override
    public ExceptionsHandlerOrder getHandlerOrder() {
        return ExceptionsHandlerOrder.ENTITY;
    }
}
