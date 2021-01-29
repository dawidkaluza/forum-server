package pl.dkaluza.forum.modules.user.confirmRegistration.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationMail;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationMailReceiver;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationMailReceiverRepository;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationMailRepository;

@Component
public class ConfirmRegistrationMailSender {
    private final ConfirmRegistrationMailReceiverRepository receiverRepository;
    private final ConfirmRegistrationMailRepository mailRepository;

    @Autowired
    public ConfirmRegistrationMailSender(ConfirmRegistrationMailReceiverRepository receiverRepository, ConfirmRegistrationMailRepository mailRepository) {
        this.receiverRepository = receiverRepository;
        this.mailRepository = mailRepository;
    }

    @Transactional
    //TODO rename to sendToken and get email, userId and token as a params
    public void sendMail(String email, String message) {
        //TODO implement sending message here
        System.out.println("Send message to " + email + ": " + message);

        ConfirmRegistrationMailReceiver receiver = receiverRepository.findByEmail(email).orElseGet(() -> {
            ConfirmRegistrationMailReceiver newReceiver = new ConfirmRegistrationMailReceiver();
            newReceiver.setEmail(email);
            return receiverRepository.save(newReceiver);
        });

        ConfirmRegistrationMail mail = new ConfirmRegistrationMail();
        mail.setReceiver(receiver);
        mailRepository.save(mail);
    }
}
