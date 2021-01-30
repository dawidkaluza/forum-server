package pl.dkaluza.forum.modules.user.confirmRegistration.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.events.OnUserRegisterEvent;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationToken;
import pl.dkaluza.forum.modules.user.confirmRegistration.mail.ConfirmRegistrationMailSender;
import pl.dkaluza.forum.modules.user.confirmRegistration.properties.ConfirmRegistrationPropertiesSupplier;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class OnUserRegisterListener {
    private final ConfirmRegistrationPropertiesSupplier propertiesSupplier;
    private final ConfirmRegistrationMailSender mailSender;
    private final UserRepository userRepository;
    private final ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository;

    @Autowired
    public OnUserRegisterListener(ConfirmRegistrationPropertiesSupplier propertiesSupplier, ConfirmRegistrationMailSender mailSender, UserRepository userRepository, ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository) {
        this.propertiesSupplier = propertiesSupplier;
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.confirmRegistrationTokenRepository = confirmRegistrationTokenRepository;
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onApplicationEvent(OnUserRegisterEvent event) {
        try {
            ConfirmRegistrationToken token = new ConfirmRegistrationToken();

            long userId = event.getUserId();
            User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
            token.setUser(user);

            String generatedToken = UUID.randomUUID().toString();
            token.setToken(generatedToken);

            token.setExpiresAt(LocalDateTime.now().plus(propertiesSupplier.tokenExpiration()));
            confirmRegistrationTokenRepository.save(token);

            mailSender.sendMail(userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
