package pl.dkaluza.forum.core.api.response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class RequestErrorCreator {
    private final MessageSource messageSource;

    @Autowired
    public RequestErrorCreator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public Builder builder() {
        return new Builder(messageSource);
    }

    public static class Builder {
        private final MessageSource messageSource;
        private HttpStatus status;
        private String message;
        private ZonedDateTime timestamp;
        private final List<RequestFieldError> fieldErrors;

        private Builder(MessageSource messageSource) {
            this.messageSource = messageSource;
            status = null;
            message = null;
            timestamp = null;
            fieldErrors = new ArrayList<>();
        }

        public Builder withStatus(HttpStatus status) {
            Assert.state(this.status == null, "Status has been initialized");
            this.status = status;
            return this;
        }

        public Builder withMessage(String message) {
            Assert.state(this.message == null, "Message has been initialized");
            this.message = message;
            return this;
        }

        public Builder withMessage(Locale locale, String code, String defaultMessage) {
            Assert.state(this.message == null, "Message has been initialized");
            this.message = messageSource.getMessage(code, null, defaultMessage, locale);
            return this;
        }

        public Builder withMessage(Locale locale, MessageSourceResolvable resolvable) {
            Assert.state(this.message == null, "Message has been initialized");
            this.message = messageSource.getMessage(resolvable, locale);
            return this;
        }

        public Builder withTimestamp(LocalDateTime timestamp) {
            Assert.state(this.timestamp == null, "Timestamp has been initialized");
            this.timestamp = timestamp.atZone(ZoneOffset.UTC);
            return this;
        }

        public Builder withTimestampAsNow() {
            Assert.state(this.timestamp == null, "Timestamp has been initialized");
            this.timestamp = ZonedDateTime.now(ZoneOffset.UTC);
            return this;
        }

        public Builder withFieldError(String field, String message) {
            fieldErrors.add(new RequestFieldError(field, message));
            return this;
        }

        public Builder withFieldError(String field, Locale locale, String code, String defaultMessage) {
            fieldErrors.add(
                new RequestFieldError(
                    field, messageSource.getMessage(code, null, defaultMessage, locale)
                )
            );
            return this;
        }

        public Builder withFieldError(String field, Locale locale, MessageSourceResolvable resolvable) {
            fieldErrors.add(
                new RequestFieldError(
                    field, messageSource.getMessage(resolvable, locale)
                )
            );
            return this;
        }

        public Builder withFieldError(Locale locale, FieldError fieldError) {
            fieldErrors.add(
                new RequestFieldError(
                    fieldError.getField(), messageSource.getMessage(fieldError, locale)
                )
            );
            return this;
        }

        public ResponseEntity<Object> build() {
            Assert.state(status != null, "Status is null");
            Assert.state(message != null, "Message is null");
            Assert.state(timestamp != null, "Timestamp is null");
            RequestError error = new RequestError(status, message, timestamp, fieldErrors.isEmpty() ? null : fieldErrors);
            return new ResponseEntity<>(error, status);
        }
    }
}
