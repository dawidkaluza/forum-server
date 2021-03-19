package pl.dkaluza.forum.core.api.exceptionsHandler.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.mail.MailException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.dkaluza.forum.core.api.exceptionsHandler.ExceptionsHandler;
import pl.dkaluza.forum.core.api.exceptionsHandler.ExceptionsHandlerOrder;
import pl.dkaluza.forum.core.api.response.LocaleFieldError;
import pl.dkaluza.forum.core.api.response.LocaleFieldErrorMapper;
import pl.dkaluza.forum.core.api.response.ResponseFieldsError;

import java.sql.SQLException;
import java.util.List;

@RestControllerAdvice
public class GeneralExceptionsHandler extends ResponseEntityExceptionHandler implements ExceptionsHandler {
    private final MessageSource messageSource;
    private final LocaleFieldErrorMapper localeFieldErrorMapper;

    @Autowired
    public GeneralExceptionsHandler(MessageSource messageSource, LocaleFieldErrorMapper localeFieldErrorMapper) {
        this.messageSource = messageSource;
        this.localeFieldErrorMapper = localeFieldErrorMapper;
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        List<LocaleFieldError> localeErrors = localeFieldErrorMapper.map(ex.getFieldErrors(), request.getLocale());
        return new ResponseFieldsError(HttpStatus.UNPROCESSABLE_ENTITY, messageSource.getMessage("invalidFields", null, request.getLocale()))
            .addAll(localeErrors)
            .toResponseEntity();
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> sqlExceptionHandler(SQLException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<?> mailExceptionHandler(MailException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Override
    public ExceptionsHandlerOrder getHandlerOrder() {
        return ExceptionsHandlerOrder.GENERAL;
    }
}
