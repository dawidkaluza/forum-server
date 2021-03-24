package pl.dkaluza.forum.modules.user.confirmRegistration.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.dkaluza.forum.modules.user.confirmRegistration.api.hateoas.ConfirmRegistrationModelAssembler;
import pl.dkaluza.forum.modules.user.confirmRegistration.api.hateoas.ResendTokenModelAssembler;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.confirm.ConfirmModel;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.resendToken.ResendTokenModel;
import pl.dkaluza.forum.modules.user.confirmRegistration.services.ConfirmRegistrationService;

@RestController
public class ConfirmRegistrationController {
    private final ConfirmRegistrationService confirmRegistrationService;
    private final ConfirmRegistrationModelAssembler confirmRegistrationModelAssembler;
    private final ResendTokenModelAssembler resendTokenModelAssembler;

    @Autowired
    public ConfirmRegistrationController(ConfirmRegistrationService confirmRegistrationService, ConfirmRegistrationModelAssembler confirmRegistrationModelAssembler, ResendTokenModelAssembler resendTokenModelAssembler) {
        this.confirmRegistrationService = confirmRegistrationService;
        this.confirmRegistrationModelAssembler = confirmRegistrationModelAssembler;
        this.resendTokenModelAssembler = resendTokenModelAssembler;
    }

    @PutMapping("confirmRegistration")
    @ResponseStatus(HttpStatus.OK)
    public RepresentationModel<?> confirmRegistration(@RequestBody ConfirmModel model) {
        confirmRegistrationService.confirmRegistration(model);
        return confirmRegistrationModelAssembler.toModel(new RepresentationModel<>());
    }

    @PostMapping("confirmRegistration/resendToken")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RepresentationModel<?> resendToken(@RequestBody ResendTokenModel model) {
        confirmRegistrationService.resendToken(model);
        return resendTokenModelAssembler.toModel(new RepresentationModel<>());
    }
}
