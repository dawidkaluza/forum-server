package pl.dkaluza.forum.modules.user.base.models.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.dkaluza.forum.core.mappers.ObjectMapper;
import pl.dkaluza.forum.modules.user.base.entities.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class UserRegisterMapper implements ObjectMapper<User, UserRegisterModel> {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegisterMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User toObject(UserRegisterModel model) {
        User user = new User();
        user.setName(model.getName());
        user.setEmail(model.getEmail());
        user.setEncodedPassword(
            passwordEncoder.encode(model.getPlainPassword())
        );
        user.setEnabled(false);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
