package pl.dkaluza.forum.modules.forum.topic.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.dkaluza.forum.core.api.exceptionsHandler.ExceptionsHandler;
import pl.dkaluza.forum.core.api.response.RequestErrorCreator;
import pl.dkaluza.forum.modules.forum.topic.exceptions.TopicClosedException;
import pl.dkaluza.forum.modules.forum.topic.exceptions.TopicNotFoundException;

@RestControllerAdvice
public class TopicExceptionsHandler implements ExceptionsHandler {
    private final RequestErrorCreator requestErrorCreator;

    @Autowired
    public TopicExceptionsHandler(RequestErrorCreator requestErrorCreator) {
        this.requestErrorCreator = requestErrorCreator;
    }

    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<?> topicNotFoundExceptionHandler(WebRequest request) {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.NOT_FOUND)
            .withTimestampAsNow()
            .withMessage(request.getLocale(), "forum.topic.topicNotFound", "Topic not found")
            .build();
    }

    @ExceptionHandler(TopicClosedException.class)
    public ResponseEntity<?> topicClosedExceptionHandler(WebRequest request) {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.LOCKED)
            .withTimestampAsNow()
            .withMessage(request.getLocale(), "forum.topic.topicClosed", "Topic closed")
            .build();
    }
}
