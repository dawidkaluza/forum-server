package pl.dkaluza.forum.modules.user.confirmRegistration.validators;

import pl.dkaluza.forum.core.validator.Validator;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationToken;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TokenNotFoundException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TooManyTokenResendsException;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.resendToken.ResendTokenModel;
import pl.dkaluza.forum.modules.user.confirmRegistration.properties.ConfirmRegistrationPropertiesSupplier;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationMailRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationTokenRepository;

import java.time.LocalDateTime;

public class ResendTokenValidator implements Validator<RuntimeException> {
    private final ResendTokenModel model;
    private final ConfirmRegistrationPropertiesSupplier propertiesSupplier;
    private final UserRepository userRepository;
    private final ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository;
    private final ConfirmRegistrationMailRepository confirmRegistrationMailRepository;

    private User user;

    public ResendTokenValidator(ResendTokenModel model, ConfirmRegistrationPropertiesSupplier propertiesSupplier, UserRepository userRepository, ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository, ConfirmRegistrationMailRepository confirmRegistrationMailRepository) {
        this.model = model;
        this.propertiesSupplier = propertiesSupplier;
        this.userRepository = userRepository;
        this.confirmRegistrationTokenRepository = confirmRegistrationTokenRepository;
        this.confirmRegistrationMailRepository = confirmRegistrationMailRepository;
    }

    @Override
    public void validate() throws RuntimeException {
        String email = model.getEmail();
        user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Can't find user with email " + email));

        ConfirmRegistrationToken token = confirmRegistrationTokenRepository
            .findById(user.getId())
            .orElseThrow(() -> new TokenNotFoundException(user.getId()));

        LocalDateTime expiresAt = LocalDateTime.now().minus(propertiesSupplier.tryExpiration());
        int triesNum = confirmRegistrationMailRepository.countAllByReceiverEmailAndSentAtAfter(email, expiresAt);
        if (triesNum >= propertiesSupplier.getMaxTries()) {
            throw new TooManyTokenResendsException("Resends limit has been reached");
        }
    }

    public User getUser() {
        return user;
    }
}
