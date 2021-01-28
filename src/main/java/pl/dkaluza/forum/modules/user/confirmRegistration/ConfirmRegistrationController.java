package pl.dkaluza.forum.modules.user.confirmRegistration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfirmRegistrationController {
    private final ConfirmRegistrationService confirmRegistrationService;

    @Autowired
    public ConfirmRegistrationController(ConfirmRegistrationService confirmRegistrationService) {
        this.confirmRegistrationService = confirmRegistrationService;
    }

    @PostMapping("user/{id}/confirmRegistration/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmRegistration(@PathVariable("id") Long id, @PathVariable("token") String token) {
        confirmRegistrationService.confirmRegistration(id, token);
    }
}
