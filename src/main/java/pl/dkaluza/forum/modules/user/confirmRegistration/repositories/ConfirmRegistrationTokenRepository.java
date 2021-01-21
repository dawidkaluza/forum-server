package pl.dkaluza.forum.modules.user.confirmRegistration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationToken;

@Repository
public interface ConfirmRegistrationTokenRepository extends JpaRepository<ConfirmRegistrationToken, Long> {
}
