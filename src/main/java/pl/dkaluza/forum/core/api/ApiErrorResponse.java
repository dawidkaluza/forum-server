package pl.dkaluza.forum.core.api;

public class ApiErrorResponse {
    private final int statusCode;
    private final String message;

    public ApiErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
