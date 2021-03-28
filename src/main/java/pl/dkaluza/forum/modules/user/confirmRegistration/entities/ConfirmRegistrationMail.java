package pl.dkaluza.forum.modules.user.confirmRegistration.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
public class ConfirmRegistrationMail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime sentAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ConfirmRegistrationMailReceiver receiver;

    @PrePersist
    protected void prePersist() {
        sentAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public ConfirmRegistrationMailReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(ConfirmRegistrationMailReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfirmRegistrationMail)) return false;

        ConfirmRegistrationMail that = (ConfirmRegistrationMail) o;
        return id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
