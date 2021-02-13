package pl.dkaluza.forum.modules.user.confirmRegistration.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.dkaluza.forum.modules.user.confirmRegistration.services.ConfirmRegistrationService;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.confirm.ConfirmModel;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.resendToken.ResendTokenModel;

@RestController
public class ConfirmRegistrationController {
    private final ConfirmRegistrationService confirmRegistrationService;

    @Autowired
    public ConfirmRegistrationController(ConfirmRegistrationService confirmRegistrationService) {
        this.confirmRegistrationService = confirmRegistrationService;
    }

    @PutMapping("confirmRegistration/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmRegistration(@RequestBody ConfirmModel model) {
        confirmRegistrationService.confirmRegistration(model);
    }

    @PostMapping("confirmRegistration/resendToken")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void resendToken(@RequestBody ResendTokenModel model) {
        confirmRegistrationService.resendToken(model);
    }
}
