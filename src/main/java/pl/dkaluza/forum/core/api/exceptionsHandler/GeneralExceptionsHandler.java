package pl.dkaluza.forum.core.api.exceptionsHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.dkaluza.forum.core.api.exceptionsHandler.ExceptionsHandler;
import pl.dkaluza.forum.core.api.exceptionsHandler.ExceptionsHandlerOrder;
import pl.dkaluza.forum.core.api.response.RequestErrorCreator;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
public class GeneralExceptionsHandler extends ResponseEntityExceptionHandler implements ExceptionsHandler {
    private final RequestErrorCreator requestErrorCreator;

    @Autowired
    public GeneralExceptionsHandler(RequestErrorCreator requestErrorCreator) {
        this.requestErrorCreator = requestErrorCreator;
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        Locale locale = request.getLocale();
        RequestErrorCreator.Builder builder = requestErrorCreator.builder()
            .withStatus(HttpStatus.UNPROCESSABLE_ENTITY)
            .withTimestampAsNow()
            .withMessage(locale, "invalidFields", "Invalid fields");

        List<FieldError> fieldErrorsList = ex.getFieldErrors();
        for(FieldError fieldError : fieldErrorsList) {
            builder.withFieldError(locale, fieldError);
        }

        return builder.build();
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
