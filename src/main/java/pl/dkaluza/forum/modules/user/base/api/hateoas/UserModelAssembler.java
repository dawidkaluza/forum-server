package pl.dkaluza.forum.modules.user.base.api.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.modules.user.base.api.UserController;
import pl.dkaluza.forum.modules.user.base.models.basic.UserModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserModel, UserModel> {
    @Override
    @NonNull
    public UserModel toModel(UserModel model) {
        model.add(buildSelfLink(model));
        return model;
    }

    private Link buildSelfLink(UserModel model) {
        return linkTo(methodOn(UserController.class).get(model.getId())).withSelfRel();
    }
}
