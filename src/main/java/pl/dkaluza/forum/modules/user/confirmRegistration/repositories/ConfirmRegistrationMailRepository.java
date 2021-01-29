package pl.dkaluza.forum.modules.user.confirmRegistration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dkaluza.forum.modules.user.confirmRegistration.entities.ConfirmRegistrationMail;

import java.time.LocalDateTime;

@Repository
public interface ConfirmRegistrationMailRepository extends JpaRepository<ConfirmRegistrationMail, Long> {
    int countAllByReceiverEmailAndSentAtAfter(String email, LocalDateTime sentAt);
    void deleteAllBySentAtBefore(LocalDateTime time);
}
