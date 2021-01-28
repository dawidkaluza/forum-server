package pl.dkaluza.forum.modules.user.confirmRegistration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dkaluza.forum.core.validator.ComposedValidatorsExecutor;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationToken;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationTokenRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.validators.ConfirmRegistrationValidator;

import javax.transaction.Transactional;

@Service
public class ConfirmRegistrationService {
    private final ComposedValidatorsExecutor validatorsExecutor;
    private final UserRepository userRepository;
    private final ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository;

    @Autowired
    public ConfirmRegistrationService(ComposedValidatorsExecutor validatorsExecutor, UserRepository userRepository, ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository) {
        this.validatorsExecutor = validatorsExecutor;
        this.userRepository = userRepository;
        this.confirmRegistrationTokenRepository = confirmRegistrationTokenRepository;
    }

    @Transactional
    public void confirmRegistration(Long id, String tokenCode) {
        ConfirmRegistrationValidator validator = new ConfirmRegistrationValidator(id, tokenCode, confirmRegistrationTokenRepository);
        validatorsExecutor.validate(validator);

        ConfirmRegistrationToken token = validator.getToken();
        confirmRegistrationTokenRepository.delete(token);

        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
    }
}
