package pl.dkaluza.forum.modules.forum.topic.exceptions;

public class TopicNotFoundException extends TopicException {
    private static final long serialVersionUID = 2797182754274405869L;

    public TopicNotFoundException(String message) {
        super(message);
    }

    public TopicNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
