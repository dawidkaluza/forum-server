package pl.dkaluza.forum.modules.user.confirmRegistration.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationToken;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.InvalidTokenException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TokenExpiredException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TokenNotFoundException;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TooManyTokenResendsException;
import pl.dkaluza.forum.modules.user.confirmRegistration.mail.ConfirmRegistrationMailSender;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.confirm.ConfirmModel;
import pl.dkaluza.forum.modules.user.confirmRegistration.models.resendToken.ResendTokenModel;
import pl.dkaluza.forum.modules.user.confirmRegistration.properties.ConfirmRegistrationPropertiesSupplier;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationMailRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationTokenRepository;

import java.time.LocalDateTime;

@Service
public class ConfirmRegistrationServiceImpl implements ConfirmRegistrationService {
    private final ConfirmRegistrationPropertiesSupplier propertiesSupplier;
    private final ConfirmRegistrationMailSender mailSender;
    private final UserRepository userRepository;
    private final ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository;
    private final ConfirmRegistrationMailRepository confirmRegistrationMailRepository;

    @Autowired
    public ConfirmRegistrationServiceImpl(ConfirmRegistrationPropertiesSupplier propertiesSupplier, ConfirmRegistrationMailSender mailSender, UserRepository userRepository, ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository, ConfirmRegistrationMailRepository confirmRegistrationMailRepository) {
        this.propertiesSupplier = propertiesSupplier;
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.confirmRegistrationTokenRepository = confirmRegistrationTokenRepository;
        this.confirmRegistrationMailRepository = confirmRegistrationMailRepository;
    }

    @Override
    @Transactional
    public void confirmRegistration(ConfirmModel model) {
        long id = model.getId();
        ConfirmRegistrationToken token = confirmRegistrationTokenRepository
            .findById(id)
            .orElseThrow(() -> new TokenNotFoundException(id));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }

        if (!token.getToken().equals(model.getToken())) {
            throw new InvalidTokenException();
        }

        confirmRegistrationTokenRepository.delete(token);

        User user = token.getUser();
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resendToken(ResendTokenModel model) {
        String email = model.getEmail();
        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Can't find user with email " + email));

        long id = user.getId();
        if (!confirmRegistrationTokenRepository.existsById(id)) {
            throw new TokenNotFoundException(id);
        }

        LocalDateTime expiresAt = LocalDateTime.now().minus(propertiesSupplier.tryExpiration());
        int triesNum = confirmRegistrationMailRepository.countAllByReceiverEmailAndSentAtAfter(email, expiresAt);
        if (triesNum >= propertiesSupplier.getMaxTries()) {
            throw new TooManyTokenResendsException("Resends limit has been reached");
        }

        mailSender.sendMail(user.getId());
    }
}
