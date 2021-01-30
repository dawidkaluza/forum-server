package pl.dkaluza.forum.modules.user.confirmRegistration.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dkaluza.forum.core.validator.ComposedValidatorsExecutor;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationToken;
import pl.dkaluza.forum.modules.user.confirmRegistration.mail.ConfirmRegistrationMailSender;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.confirm.ConfirmModel;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.resendToken.ResendTokenModel;
import pl.dkaluza.forum.modules.user.confirmRegistration.properties.ConfirmRegistrationPropertiesSupplier;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationMailRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationTokenRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.validators.ConfirmRegistrationValidator;
import pl.dkaluza.forum.modules.user.confirmRegistration.validators.ResendTokenValidator;

@Service
public class ConfirmRegistrationServiceImpl implements ConfirmRegistrationService {
    private final ComposedValidatorsExecutor validatorsExecutor;
    private final ConfirmRegistrationPropertiesSupplier propertiesSupplier;
    private final ConfirmRegistrationMailSender mailSender;
    private final UserRepository userRepository;
    private final ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository;
    private final ConfirmRegistrationMailRepository confirmRegistrationMailRepository;

    @Autowired
    public ConfirmRegistrationServiceImpl(ComposedValidatorsExecutor validatorsExecutor, ConfirmRegistrationPropertiesSupplier propertiesSupplier, ConfirmRegistrationMailSender mailSender, UserRepository userRepository, ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository, ConfirmRegistrationMailRepository confirmRegistrationMailRepository) {
        this.validatorsExecutor = validatorsExecutor;
        this.propertiesSupplier = propertiesSupplier;
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.confirmRegistrationTokenRepository = confirmRegistrationTokenRepository;
        this.confirmRegistrationMailRepository = confirmRegistrationMailRepository;
    }

    @Override
    @Transactional
    public void confirmRegistration(ConfirmModel model) {
        ConfirmRegistrationValidator validator = new ConfirmRegistrationValidator(
            model, confirmRegistrationTokenRepository
        );
        validatorsExecutor.validate(validator);

        ConfirmRegistrationToken token = validator.getToken();
        confirmRegistrationTokenRepository.delete(token);

        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resendToken(ResendTokenModel model) {
        ResendTokenValidator validator = new ResendTokenValidator(
            model, propertiesSupplier, userRepository,
            confirmRegistrationTokenRepository, confirmRegistrationMailRepository
        );
        validator.validate();

        User user = validator.getUser();
        mailSender.sendMail(user.getId());
    }
}
