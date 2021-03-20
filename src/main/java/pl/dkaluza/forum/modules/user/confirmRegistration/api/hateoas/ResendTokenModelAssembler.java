package pl.dkaluza.forum.modules.user.confirmRegistration.api.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.modules.user.confirmRegistration.api.ConfirmRegistrationController;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.confirm.ConfirmModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ResendTokenModelAssembler implements RepresentationModelAssembler<RepresentationModel<?>, RepresentationModel<?>> {
    @Override
    @NonNull
    public RepresentationModel<?> toModel(@NonNull RepresentationModel<?> model) {
        model.add(buildConfirmRegistrationLink());
        return model;
    }

    private Link buildConfirmRegistrationLink() {
        return linkTo(methodOn(ConfirmRegistrationController.class).confirmRegistration(new ConfirmModel())).withRel("confirmRegistration");
    }
}
