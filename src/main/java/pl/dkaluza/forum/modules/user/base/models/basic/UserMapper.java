package pl.dkaluza.forum.modules.user.base.models.basic;

import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.mappers.ModelMapper;
import pl.dkaluza.forum.modules.user.base.entities.User;

@Component
public class UserMapper implements ModelMapper<User, UserModel> {
    @Override
    public UserModel toModel(User user) {
        UserModel model = new UserModel();
        model.setId(user.getId());
        model.setEmail(user.getEmail());
        model.setName(user.getName());
        model.setEnabled(user.isEnabled());
        return model;
    }
}
