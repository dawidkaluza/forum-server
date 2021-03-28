package pl.dkaluza.forum.core.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class RequestError {
    @JsonProperty
    @JsonSerialize(using = HttpStatusSerializer.class, as = HttpStatus.class)
    private final HttpStatus status;

    @JsonProperty
    private final String message;

    private final LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = FieldErrorsSerializer.class, as = List.class)
    private final List<RequestFieldError> fieldErrors;

    RequestError(HttpStatus status, String message, LocalDateTime timestamp, List<RequestFieldError> fieldErrors) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.fieldErrors = fieldErrors;
    }

    @Override
    public String toString() {
        return "RequestError{" +
            "status=" + status +
            ", message='" + message + '\'' +
            ", timestamp=" + timestamp +
            ", fieldErrors=" + fieldErrors +
            '}';
    }

    private static class HttpStatusSerializer extends JsonSerializer<HttpStatus> {
        @Override
        public void serialize(HttpStatus status, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(status.value());
        }
    }

    private static class FieldErrorsSerializer extends JsonSerializer<List<RequestFieldError>> {
        @Override
        public void serialize(List<RequestFieldError> errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartArray("fieldErrors");
            for (RequestFieldError error : errors) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("field", error.getField());
                jsonGenerator.writeStringField("message", error.getMessage());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
        }
    }
}
