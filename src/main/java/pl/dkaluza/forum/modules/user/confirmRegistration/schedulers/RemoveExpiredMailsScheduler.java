package pl.dkaluza.forum.modules.user.confirmRegistration.schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dkaluza.forum.modules.user.confirmRegistration.properties.ConfirmRegistrationPropertiesSupplier;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationMailRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class RemoveExpiredMailsScheduler {
    private final ConfirmRegistrationPropertiesSupplier propertiesSupplier;
    private final ConfirmRegistrationMailRepository confirmRegistrationMailRepository;

    @Autowired
    public RemoveExpiredMailsScheduler(ConfirmRegistrationPropertiesSupplier propertiesSupplier, ConfirmRegistrationMailRepository confirmRegistrationMailRepository) {
        this.propertiesSupplier = propertiesSupplier;
        this.confirmRegistrationMailRepository = confirmRegistrationMailRepository;
    }

    @Transactional
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void removeExpiredMails() {
        LocalDateTime time = LocalDateTime.now(ZoneOffset.UTC).minus(propertiesSupplier.getTryExpiration());
        confirmRegistrationMailRepository.deleteAllBySentAtBefore(time);
    }
}
