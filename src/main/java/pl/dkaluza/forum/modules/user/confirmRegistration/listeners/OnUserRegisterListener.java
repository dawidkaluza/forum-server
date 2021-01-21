package pl.dkaluza.forum.modules.user.confirmRegistration.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.dkaluza.forum.core.exceptions.PropertyNotFoundException;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.events.OnUserRegisterEvent;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationToken;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationTokenRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class OnUserRegisterListener {
    private final Environment environment;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository;

    @Autowired
    public OnUserRegisterListener(Environment environment, JavaMailSender mailSender, UserRepository userRepository, ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository) {
        this.environment = environment;
        this.mailSender = mailSender;
        this.userRepository = userRepository;
        this.confirmRegistrationTokenRepository = confirmRegistrationTokenRepository;
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onApplicationEvent(OnUserRegisterEvent event) {
        User user = event.getUser();
        user.setEnabled(false);
        userRepository.save(user);

        ConfirmRegistrationToken token = new ConfirmRegistrationToken();
        token.setUser(user);

        String generatedToken = UUID.randomUUID().toString();
        token.setToken(generatedToken);

        Integer expirationHours = environment.getProperty("user.confirm-registration.expiration-hours", Integer.class, 24);
        token.setExpiresAt(LocalDateTime.now().plus(Duration.ofHours(expirationHours)));
        confirmRegistrationTokenRepository.save(token);

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setSubject("Confirm your registration");

        String sender = environment.getProperty("spring.mail.username");
        if (sender == null) {
            throw new PropertyNotFoundException("spring.mail.username");
        }

        mail.setFrom(sender);
        mail.setText("To confirm your registration, just click here: http://localhost:8080/user/" + user.getId() + "/confirmRegistration/" + generatedToken);
        mailSender.send(mail);

    }
}
