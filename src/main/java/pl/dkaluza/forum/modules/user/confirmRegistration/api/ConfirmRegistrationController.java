package pl.dkaluza.forum.modules.user.confirmRegistration.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;
import pl.dkaluza.forum.modules.user.confirmRegistration.api.hateoas.ConfirmRegistrationModelAssembler;
import pl.dkaluza.forum.modules.user.confirmRegistration.services.ConfirmRegistrationService;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.confirm.ConfirmModel;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.resendToken.ResendTokenModel;

@RestController
public class ConfirmRegistrationController {
    private final ConfirmRegistrationService confirmRegistrationService;
    private final ConfirmRegistrationModelAssembler confirmRegistrationModelAssembler;

    @Autowired
    public ConfirmRegistrationController(ConfirmRegistrationService confirmRegistrationService, ConfirmRegistrationModelAssembler confirmRegistrationModelAssembler) {
        this.confirmRegistrationService = confirmRegistrationService;
        this.confirmRegistrationModelAssembler = confirmRegistrationModelAssembler;
    }

    @PutMapping("confirmRegistration/confirm")
    @ResponseStatus(HttpStatus.OK)
    public RepresentationModel<?> confirmRegistration(@RequestBody ConfirmModel model) {
        confirmRegistrationService.confirmRegistration(model);
        return confirmRegistrationModelAssembler.toModel(new RepresentationModel<>());
    }

    @PostMapping("confirmRegistration/resendToken")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void resendToken(@RequestBody ResendTokenModel model) {
        confirmRegistrationService.resendToken(model);
    }
}
