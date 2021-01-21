package pl.dkaluza.forum.modules.user.confirmRegistration.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import pl.dkaluza.forum.modules.user.base.events.OnUserDeleteEvent;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationTokenRepository;

@Component
public class OnUserDeleteListener {
    private final ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository;

    @Autowired
    public OnUserDeleteListener(ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository) {
        this.confirmRegistrationTokenRepository = confirmRegistrationTokenRepository;
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onApplicationEvent(OnUserDeleteEvent event) {
        confirmRegistrationTokenRepository.deleteById(event.getUserId());
    }
}
