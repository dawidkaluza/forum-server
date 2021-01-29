package pl.dkaluza.forum.modules.user.confirmRegistration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationToken;

import java.time.LocalDateTime;

@Repository
public interface ConfirmRegistrationTokenRepository extends JpaRepository<ConfirmRegistrationToken, Long> {
    void deleteAllByExpiresAtBefore(LocalDateTime time);
}
