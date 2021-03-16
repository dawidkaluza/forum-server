package pl.dkaluza.forum.core.api.response;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResponseFieldsError extends ResponseError {
    @JsonSerialize(using = FieldErrorsSerializer.class, as = List.class)
    private final List<LocaleFieldError> fieldErrors;

    public ResponseFieldsError(HttpStatus status, String message) {
        super(status, message);
        fieldErrors = new ArrayList<>();
    }

    public ResponseFieldsError add(LocaleFieldError error) {
        fieldErrors.add(error);
        return this;
    }

    public ResponseFieldsError addAll(Collection<? extends LocaleFieldError> errors) {
        fieldErrors.addAll(errors);
        return this;
    }

    public List<LocaleFieldError> getFieldErrors() {
        return fieldErrors;
    }

    private static class FieldErrorsSerializer extends JsonSerializer<List<LocaleFieldError>> {
        @Override
        public void serialize(List<LocaleFieldError> errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartArray("fieldErrors");

            for (LocaleFieldError error : errors) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("field", error.getField());
                jsonGenerator.writeStringField("message", error.getMessage());
                jsonGenerator.writeEndObject();
            }

            jsonGenerator.writeEndArray();
        }
    }
}
