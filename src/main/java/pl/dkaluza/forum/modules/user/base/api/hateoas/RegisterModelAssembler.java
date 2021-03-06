package pl.dkaluza.forum.modules.user.base.api.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.modules.user.base.api.UserController;
import pl.dkaluza.forum.modules.user.base.models.basic.UserModel;
import pl.dkaluza.forum.modules.user.confirmRegistration.api.ConfirmRegistrationController;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.confirm.ConfirmModel;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.resendToken.ResendTokenModel;

import java.lang.reflect.Method;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RegisterModelAssembler implements RepresentationModelAssembler<UserModel, UserModel> {
    @Override
    public UserModel toModel(UserModel model) {
        model.add(buildSelfLink(model));
        model.add(buildConfirmRegistrationLink());
        model.add(buildResendTokenLink());
        return model;
    }

    private Link buildSelfLink(UserModel model) {
        return linkTo(methodOn(UserController.class).get(model.getId())).withSelfRel();
    }

    private Link buildConfirmRegistrationLink() {
        Method method;
        try {
            method = (ConfirmRegistrationController.class).getMethod("confirmRegistration", ConfirmModel.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return linkTo(method, new ConfirmModel()).withRel("confirmRegistration");
    }

    private Link buildResendTokenLink() {
        Method method;
        try {
            method = (ConfirmRegistrationController.class).getMethod("resendToken", ResendTokenModel.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return linkTo(method, new ResendTokenModel()).withRel("resendRegistrationToken");
    }
}
