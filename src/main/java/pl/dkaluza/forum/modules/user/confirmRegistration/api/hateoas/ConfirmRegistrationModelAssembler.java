package pl.dkaluza.forum.modules.user.confirmRegistration.api.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.security.LoginController;
import pl.dkaluza.forum.core.security.LoginModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ConfirmRegistrationModelAssembler implements RepresentationModelAssembler<RepresentationModel<?>, RepresentationModel<?>> {
    @Override
    @NonNull
    public RepresentationModel<?> toModel(@NonNull RepresentationModel<?> model) {
        model.add(buildLoginLink());
        return model;
    }

    private Link buildLoginLink() {
        return linkTo(methodOn(LoginController.class).login(new LoginModel())).withRel("login");
    }
}
