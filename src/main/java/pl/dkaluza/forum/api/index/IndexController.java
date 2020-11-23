package pl.dkaluza.forum.api.index;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void index() {
    }
}
