package pl.dkaluza.forum.core.api.response;

public class LocaleFieldError {
    private final String objectName;
    private final String field;
    private final String message;

    public LocaleFieldError(String objectName, String field, String message) {
        if (objectName.endsWith("Model")) {
            this.objectName = objectName.substring(0, objectName.length() - 5);
        } else {
            this.objectName = objectName;
        }

        this.field = field;
        this.message = message;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "LocaleFieldError{" +
            "objectName='" + objectName + '\'' +
            ", field='" + field + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
