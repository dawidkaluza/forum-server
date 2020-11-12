package pl.dkaluza.forum.modules.user.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.exceptions.EntityNotFoundException;
import pl.dkaluza.forum.core.mappers.ModelAndObjectMapper;
import pl.dkaluza.forum.modules.user.entities.User;
import pl.dkaluza.forum.modules.user.models.UserModel;
import pl.dkaluza.forum.modules.user.repository.UserRepository;

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
        return model;
    }
}
