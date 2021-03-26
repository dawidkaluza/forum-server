package pl.dkaluza.forum.core.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dkaluza.forum.core.api.response.RequestErrorCreator;

@RestController
public class ErrorController {
    private final RequestErrorCreator requestErrorCreator;

    @Autowired
    public ErrorController(RequestErrorCreator requestErrorCreator) {
        this.requestErrorCreator = requestErrorCreator;
    }

    @GetMapping("/error/example")
    public ResponseEntity<?> error() {
        return requestErrorCreator.builder()
            .withStatus(HttpStatus.I_AM_A_TEAPOT)
            .withMessage("Example of request error message")
            .withTimestampAsNow()
            .withFieldError("some.field", "Some message")
            .build();
    }
}
