package pl.dkaluza.forum.modules.user.confirmRegistration.schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dkaluza.forum.modules.user.confirmRegistration.properties.ConfirmRegistrationPropertiesSupplier;
import pl.dkaluza.forum.modules.user.confirmRegistration.repositories.ConfirmRegistrationTokenRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class RemoveExpiredTokensScheduler {
    private final ConfirmRegistrationPropertiesSupplier propertiesSupplier;
    private final ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository;

    @Autowired
    public RemoveExpiredTokensScheduler(ConfirmRegistrationPropertiesSupplier propertiesSupplier, ConfirmRegistrationTokenRepository confirmRegistrationTokenRepository) {
        this.propertiesSupplier = propertiesSupplier;
        this.confirmRegistrationTokenRepository = confirmRegistrationTokenRepository;
    }

    @Transactional
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void removeExpiredTokens() {
        LocalDateTime time = LocalDateTime.now().minus(propertiesSupplier.getTokenExpiration());
        confirmRegistrationTokenRepository.deleteAllByExpiresAtBefore(time);
    }
}
