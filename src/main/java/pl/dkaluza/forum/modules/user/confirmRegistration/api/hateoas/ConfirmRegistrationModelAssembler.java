package pl.dkaluza.forum.modules.user.confirmRegistration.api.hateoas;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ConfirmRegistrationModelAssembler implements RepresentationModelAssembler<RepresentationModel<?>, RepresentationModel<?>> {
    @Override
    @NonNull
    public RepresentationModel<?> toModel(@NonNull RepresentationModel<?> model) {
        //TODO
        // after successful registration add link to "login"
        // use RepresentationModel class to do this
        return model;
    }
}
