package pl.dkaluza.forum.modules.index.api.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.security.LoginController;
import pl.dkaluza.forum.core.security.LoginModel;
import pl.dkaluza.forum.modules.index.api.IndexController;
import pl.dkaluza.forum.modules.index.models.IndexModel;
import pl.dkaluza.forum.modules.user.base.api.UserController;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class IndexModelAssembler implements RepresentationModelAssembler<IndexModel, IndexModel> {
    @Override
    @NonNull
    public IndexModel toModel(IndexModel model) {
        model.add(buildSelfLink());
        model.add(buildLoginLink());
        model.add(buildRegisterLink());
        model.add(buildUsersLink());
        return model;
    }

    private Link buildSelfLink() {
        return linkTo(methodOn(IndexController.class).index()).withSelfRel();
    }

    private Link buildLoginLink() {
        return linkTo(methodOn(LoginController.class).login(new LoginModel())).withRel("login");
    }

    private Link buildRegisterLink() {
        return linkTo(methodOn(UserController.class).register(new UserRegisterModel())).withRel("register");
    }

    private Link buildUsersLink() {
        return linkTo(methodOn(UserController.class).findAll(null)).withRel("users");
    }
}
