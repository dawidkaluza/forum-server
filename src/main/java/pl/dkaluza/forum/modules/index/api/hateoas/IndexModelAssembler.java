package pl.dkaluza.forum.modules.index.api.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
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
        model.add(buildRegisterLink());
        return model;
    }

    private Link buildRegisterLink() {
        return linkTo(methodOn(UserController.class).register(new UserRegisterModel())).withRel("register");
    }
}
