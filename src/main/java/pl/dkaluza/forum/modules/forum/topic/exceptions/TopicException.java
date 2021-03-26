package pl.dkaluza.forum.modules.forum.topic.exceptions;

public abstract class TopicException extends RuntimeException {
    private static final long serialVersionUID = 2621569759328205204L;

    public TopicException(String message) {
        super(message);
    }

    public TopicException(String message, Throwable cause) {
        super(message, cause);
    }
}
