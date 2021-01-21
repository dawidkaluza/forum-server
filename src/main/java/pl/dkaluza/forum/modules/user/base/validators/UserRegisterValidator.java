package pl.dkaluza.forum.modules.user.base.validators;

import pl.dkaluza.forum.core.validator.apiRequest.ApiRequestValidator;
import pl.dkaluza.forum.modules.user.base.exceptions.EmailAlreadyExistException;
import pl.dkaluza.forum.modules.user.base.exceptions.InvalidPasswordException;
import pl.dkaluza.forum.modules.user.base.exceptions.NameAlreadyExistException;
import pl.dkaluza.forum.modules.user.base.models.register.UserRegisterModel;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;

public class UserRegisterValidator implements ApiRequestValidator {
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
            throw new EmailAlreadyExistException(email);
        }

        String name = userRegisterModel.getName();
        if (userRepository.findByName(name).isPresent()) {
            throw new NameAlreadyExistException(name);
        }

        String password = userRegisterModel.getPlainPassword();
        if (password.length() < 3 || password.length() > 32) {
            throw new InvalidPasswordException();
        }
    }
}
