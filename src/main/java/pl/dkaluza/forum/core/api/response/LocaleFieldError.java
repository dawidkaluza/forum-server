package pl.dkaluza.forum.core.api.response;

public class LocaleFieldError {
    private final String field;
    private final String message;

    public LocaleFieldError(String field, String message) {
        this.field = field;
        this.message = message;
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
            "field='" + field + '\'' +
            ", message='" + message + '\'' +
            '}';
    }
}
