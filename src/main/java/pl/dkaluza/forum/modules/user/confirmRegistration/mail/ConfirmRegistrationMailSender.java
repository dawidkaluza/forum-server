package pl.dkaluza.forum.modules.user.confirmRegistration.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dkaluza.forum.modules.user.base.entities.User;
import pl.dkaluza.forum.modules.user.base.exceptions.UserNotFoundException;
import pl.dkaluza.forum.modules.user.base.repositories.UserRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationMail;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationMailReceiver;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationToken;
import pl.dkaluza.forum.modules.user.confirmRegistration.exceptions.TokenNotFoundException;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationMailReceiverRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationMailRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationTokenRepository;

@Component
public class ConfirmRegistrationMailSender {
    private final JavaMailSender mailSender;
    private final TaskExecutor mailSenderExecutor;
    private final UserRepository userRepository;
    private final ConfirmRegistrationTokenRepository tokenRepository;
    private final ConfirmRegistrationMailReceiverRepository receiverRepository;
    private final ConfirmRegistrationMailRepository mailRepository;

    @Autowired
    public ConfirmRegistrationMailSender(JavaMailSender mailSender, @Qualifier("confirmRegistrationMailSenderExecutor") TaskExecutor mailSenderExecutor, UserRepository userRepository, ConfirmRegistrationTokenRepository tokenRepository, ConfirmRegistrationMailReceiverRepository receiverRepository, ConfirmRegistrationMailRepository mailRepository) {
        this.mailSender = mailSender;
        this.mailSenderExecutor = mailSenderExecutor;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.receiverRepository = receiverRepository;
        this.mailRepository = mailRepository;
    }

    @Transactional
    public void sendMail(long userId) {
        User user = userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        ConfirmRegistrationToken token = tokenRepository
            .findById(userId)
            .orElseThrow(() -> new TokenNotFoundException(userId));

        String email = user.getEmail();
        ConfirmRegistrationMailReceiver receiver = receiverRepository.findByEmail(email).orElseGet(() -> {
            ConfirmRegistrationMailReceiver newReceiver = new ConfirmRegistrationMailReceiver();
            newReceiver.setEmail(email);
            return receiverRepository.save(newReceiver);
        });

        ConfirmRegistrationMail mail = new ConfirmRegistrationMail();
        mail.setReceiver(receiver);
        mailRepository.save(mail);

        mailSenderExecutor.execute(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("Forum");
            message.setTo(user.getName() + " <" + email + ">");
            message.setSubject("Confirm your registration");
            message.setText(
                "To confirm your registration, just click here: http://localhost:8080/user/"
                    + userId
                    + "/confirmRegistration/"
                    + token.getToken()
            );
            mailSender.send(message);
        });
    }
}
