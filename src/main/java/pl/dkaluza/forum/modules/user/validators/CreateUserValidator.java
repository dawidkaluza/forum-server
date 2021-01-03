package pl.dkaluza.forum.modules.user.validators;

import pl.dkaluza.forum.core.exceptions.ApiRequestException;
import pl.dkaluza.forum.core.validator.apiRequest.ApiRequestValidator;
import pl.dkaluza.forum.modules.user.UserRepository;
import pl.dkaluza.forum.modules.user.exceptions.EmailAlreadyExistException;
import pl.dkaluza.forum.modules.user.exceptions.InvalidPasswordException;
import pl.dkaluza.forum.modules.user.exceptions.NameAlreadyExistException;
import pl.dkaluza.forum.modules.user.models.create.UserCreationModel;

public class CreateUserValidator implements ApiRequestValidator {
    private final UserCreationModel userCreationModel;
    private final UserRepository userRepository;

    public CreateUserValidator(UserCreationModel userCreationModel, UserRepository userRepository) {
        this.userCreationModel = userCreationModel;
        this.userRepository = userRepository;
    }

    @Override
    public void validate() throws ApiRequestException {
        String email = userCreationModel.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistException(email);
        }

        String name = userCreationModel.getName();
        if (userRepository.findByName(name).isPresent()) {
            throw new NameAlreadyExistException(name);
        }

        String password = userCreationModel.getPlainPassword();
        if (password.length() < 3 || password.length() > 32) {
            throw new InvalidPasswordException();
        }
    }
}
