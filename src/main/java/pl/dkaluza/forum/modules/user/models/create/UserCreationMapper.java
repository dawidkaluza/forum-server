package pl.dkaluza.forum.modules.user.models.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.mappers.ObjectMapper;
import pl.dkaluza.forum.modules.user.entities.User;

@Component
public class UserCreationMapper implements ObjectMapper<User, UserCreationModel> {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserCreationMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User toObject(UserCreationModel model) {
        User user = new User();
        user.setName(model.getName());
        user.setEmail(model.getEmail());
        user.setEncodedPassword(
            passwordEncoder.encode(model.getPlainPassword())
        );
        return user;
    }
}
