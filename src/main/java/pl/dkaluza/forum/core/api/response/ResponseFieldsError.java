package pl.dkaluza.forum.core.api.response;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResponseFieldsError extends ResponseError {
    @JsonSerialize(using = FieldErrorsSerializer.class, as = List.class)
    private final List<FieldError> fieldErrors;

    public ResponseFieldsError(HttpStatus status, String message) {
        super(status, message);
        fieldErrors = new ArrayList<>();
    }

    public ResponseFieldsError add(FieldError error) {
        fieldErrors.add(error);
        return this;
    }

    public ResponseFieldsError addAll(Collection<? extends FieldError> errors) {
        fieldErrors.addAll(errors);
        return this;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    private static class FieldErrorsSerializer extends JsonSerializer<List<FieldError>> {
        @Override
        public void serialize(List<FieldError> errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartArray("fieldErrors");

            for (FieldError error : errors) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("object", getObjectName(error));
                jsonGenerator.writeStringField("field", error.getField());
                //TODO translate this message
                jsonGenerator.writeStringField("message", error.getDefaultMessage());
                jsonGenerator.writeEndObject();
            }

            jsonGenerator.writeEndArray();
        }


        private String getObjectName(FieldError error) {
            String objectName = error.getObjectName();
            if (objectName.endsWith("Model")) {
                int objectNameLen = objectName.length();
                return objectName.substring(0, objectNameLen - 5);
            }

            return objectName;
        }
    }
}
