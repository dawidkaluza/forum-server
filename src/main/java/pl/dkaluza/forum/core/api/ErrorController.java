package pl.dkaluza.forum.core.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dkaluza.forum.core.api.response.LocaleFieldError;
import pl.dkaluza.forum.core.api.response.ResponseFieldsError;

@RestController
public class ErrorController {
    @GetMapping("/error")
    public ResponseEntity<?> error() {
        return new ResponseFieldsError(
            HttpStatus.BAD_REQUEST, "Example response error message"
        ).add(
            new LocaleFieldError("Some object name", "Some field", "Some message")
        ).toResponseEntity();
    }
}
