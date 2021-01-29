package pl.dkaluza.forum.modules.user.confirmRegistration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationMailReceiver;

import java.util.Optional;

@Repository
public interface ConfirmRegistrationMailReceiverRepository extends JpaRepository<ConfirmRegistrationMailReceiver, Long> {
    Optional<ConfirmRegistrationMailReceiver> findByEmail(String email);
}
