package pl.dkaluza.forum.modules.forum.topic.exceptions;

public class TopicClosedException extends TopicException {
    private static final long serialVersionUID = -5471705750680260013L;

    public TopicClosedException(String message) {
        super(message);
    }

    public TopicClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
