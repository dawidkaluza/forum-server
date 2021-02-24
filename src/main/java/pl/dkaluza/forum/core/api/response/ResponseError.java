package pl.dkaluza.forum.core.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDateTime;

@JsonRootName("error")
public class ResponseError {
    @JsonProperty
    @JsonSerialize(using = HttpStatusSerializer.class, as = HttpStatus.class)
    private final HttpStatus status;

    @JsonProperty
    private final String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private final LocalDateTime timestamp;

    protected ResponseError(HttpStatus status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ResponseError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static ResponseError of(HttpStatus status, String message) {
        return new ResponseError(
            status, message, LocalDateTime.now()
        );
    }

    public ResponseEntity<Object> toResponseEntity() {
        return new ResponseEntity<>(this, status);
    }

    private static class HttpStatusSerializer extends JsonSerializer<HttpStatus> {
        @Override
        public void serialize(HttpStatus status, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(status.value());
        }
    }
}
