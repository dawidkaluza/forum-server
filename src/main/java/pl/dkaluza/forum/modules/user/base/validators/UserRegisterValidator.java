package pl.dkaluza.forum.modules.user.base.validators;

import pl.dkaluza.forum.core.validator.Validator;
import pl.dkaluza.forum.modules.user.base.exceptions.EmailAlreadyExistsException;
import pl.dkaluza.forum.modules.user.base.exceptions.InvalidPasswordException;
import pl.dkaluza.forum.modules.user.base.exceptions.NameAlreadyExistsException;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterModel;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;

public class UserRegisterValidator implements Validator<RuntimeException> {
    private final UserRegisterModel userRegisterModel;
    private final UserRepository userRepository;

    public UserRegisterValidator(UserRegisterModel userRegisterModel, UserRepository userRepository) {
        this.userRegisterModel = userRegisterModel;
        this.userRepository = userRepository;
    }

    @Override
    public void validate() throws RuntimeException {
        //TODO check its email here or via annotations
        String email = userRegisterModel.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(email);
        }

        String name = userRegisterModel.getName();
        if (userRepository.findByName(name).isPresent()) {
            throw new NameAlreadyExistsException(name);
        }

        String password = userRegisterModel.getPlainPassword();
        if (password.length() < 3 || password.length() > 32) {
            throw new InvalidPasswordException();
        }
    }
}
