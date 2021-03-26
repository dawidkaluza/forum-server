package pl.dkaluza.forum.modules.forum.topic.exceptions;

public class PostNotFoundException extends TopicException {
    private static final long serialVersionUID = -8430225351189241275L;

    public PostNotFoundException(String message) {
        super(message);
    }

    public PostNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
