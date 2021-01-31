package pl.dkaluza.forum.modules.user.base.models.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.exceptions.entity.EntityNotFoundException;
import pl.dkaluza.forum.core.mappers.ModelAndObjectMapper;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;

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
            .orElseThrow(() -> new UserNotFoundException(model.getId()));

        user.setName(model.getName());
        user.setEmail(model.getEmail());
        user.setEncodedPassword(model.getEncodedPassword());
        user.setEnabled(model.isEnabled());
        return user;
    }

    @Override
    public UserModel toModel(User user) {
        UserModel model = new UserModel();
        model.setId(user.getId());
        model.setEmail(user.getEmail());
        model.setName(user.getName());
        model.setEncodedPassword(user.getEncodedPassword());
        model.setEnabled(user.isEnabled());
        return model;
    }
}
