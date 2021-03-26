package pl.dkaluza.forum.modules.forum.topic.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import pl.dkaluza.forum.core.api.exceptionsHandler.ExceptionsHandler;
import pl.dkaluza.forum.core.api.response.RequestErrorCreator;
import pl.dkaluza.forum.modules.forum.topic.exceptions.PostNotFoundException;

@RestControllerAdvice
public class PostExceptionsHandler implements ExceptionsHandler {
    private final RequestErrorCreator requestErrorCreator;

    @Autowired
    public PostExceptionsHandler(RequestErrorCreator requestErrorCreator) {
        this.requestErrorCreator = requestErrorCreator;
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<?> postNotFoundExceptionHandler(WebRequest request) {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.NOT_FOUND)
            .withMessage(request.getLocale(), "forum.topic.postNotFound", "Post not found")
            .withTimestampAsNow()
            .build();
    }
}
