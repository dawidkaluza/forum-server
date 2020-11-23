package pl.dkaluza.forum.modules.user.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.api.user.UserController;
import pl.dkaluza.forum.core.exceptions.EntityNotFoundException;
import pl.dkaluza.forum.core.mappers.ModelAndObjectMapper;
import pl.dkaluza.forum.modules.user.entities.User;
import pl.dkaluza.forum.modules.user.models.UserModel;
import pl.dkaluza.forum.modules.user.repository.UserRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserMapper implements ModelAndObjectMapper<User, UserModel> {
    private final UserRepository userRepository;

    @Autowired
    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User toObject(UserModel model) throws EntityNotFoundException {
        User user = userRepository
            .findById(model.getId())
            .orElseThrow(() -> new EntityNotFoundException(User.class, model.getId()));

        user.setName(model.getName());
        user.setEmail(model.getEmail());
        user.setEncodedPassword(model.getEncodedPassword());
        return user;
    }

    @Override
    public UserModel toModel(User user) {
        UserModel model = new UserModel();
        model.setId(user.getId());
        model.setEmail(user.getEmail());
        model.setName(user.getName());
        model.setEncodedPassword(user.getEncodedPassword());
        model.add(buildSelfLink(user));
//        model.add(buildUsersLink());
        return model;
    }

    private Link buildSelfLink(User user) {
        try {
            return linkTo(methodOn(UserController.class).get(user.getId())).withSelfRel()
                .andAffordance(
                    afford(
                        methodOn(UserController.class).update(null)
                    )
                )
                .andAffordance(
                    afford(
                        methodOn(UserController.class).delete(user.getId())
                    )
                );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Link buildUsersLink() {
        return linkTo(methodOn(UserController.class).findAll(Pageable.unpaged())).withRel("users");
    }
}
